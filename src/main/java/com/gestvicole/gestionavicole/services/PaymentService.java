package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.entities.Invoice;
import com.gestvicole.gestionavicole.entities.Payment;
import com.gestvicole.gestionavicole.repositories.InvoiceRepository;
import com.gestvicole.gestionavicole.repositories.PaymentRepository;
import com.gestvicole.gestionavicole.utils.Enumeration;
import com.gestvicole.gestionavicole.utils.ProjectUtils;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          InvoiceRepository invoiceRepository) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public ResponseBody findAll() {
        try {
            return ResponseBody.with(paymentRepository.findAllByCanceled(false),"Liste de paiements");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }


    public ResponseBody validate(Payment payment) {
        try {
            Invoice invoice = invoiceRepository.findInvoiceById(payment.getInvoice().getId());
            if (invoice == null) {
                return ResponseBody.error("Cette facture n'existe pas");
            }

            List<Payment> payments = paymentRepository.findAllByIdLessThanAndInvoiceIdAndCanceledAndStateEquals(
                    payment.getId(), invoice.getId(), false, Enumeration.PAYMENT_STATE.PENDING);
            if (payments != null && !payments.isEmpty()) {
                return ResponseBody.error("Impossible de valider ce paiement\nVous devez d'abord valider les enregistrements plus anciens");
            }

            Payment oldPayment = paymentRepository.findPaymentById(payment.getId());
            if (oldPayment == null) {
                return ResponseBody.error("Ce paiement n'existe pas");
            }
            oldPayment.setReference(payment.getReference());
            oldPayment.setPaymentDate(payment.getPaymentDate());
            oldPayment.setMethodOfPayment(payment.getMethodOfPayment());
            oldPayment.setState(Enumeration.PAYMENT_STATE.VALIDATE);
            oldPayment.setValidateDate(new Date());
            oldPayment.setValidateBy(payment.getValidateBy());
            oldPayment.setAccountPaidAfter(invoice.getAmountPaid());
            paymentRepository.save(oldPayment);

            invoice.setUpdateDate(new Date());
            invoiceRepository.save(invoice);

            return ResponseBody.with(oldPayment, "Paiement enregistre avec succes");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody create(Payment payment) {
        try {
            String message = "Paiement effectue avec succes";

            Invoice invoice = invoiceRepository.findInvoiceById(payment.getInvoice().getId());
            if (invoice == null) {
                return ResponseBody.error("Cette facture n'existe pas");
            }

            payment.setInvoice(invoice);
            payment.setAmount(payment.getNetToPay());
            payment.setBalanceBefore(invoice.getStayToPay());

            double newBalance = payment.getBalanceBefore() - payment.getAmount();
            payment.setBalanceAfter(newBalance);
            payment.setState(Enumeration.PAYMENT_STATE.PENDING);
            payment.setCreateDate(new Date());
            payment.setAccountPaidBefore(invoice.getAmountPaid());
            Payment newPayment = paymentRepository.save(payment);

            // update invoice after payment
            invoice.setStayToPay(newBalance);
            invoice.setAmountPaid(newPayment.getAmount() + invoice.getAmountPaid());
            invoice.setLastPaymentDate(new Date());
            invoice.setUpdateDate(new Date());
            if (invoice.getStayToPay() == 0) {
                invoice.setPaid(true);
                invoice.setState(Enumeration.INVOICE_STATE.PAID);
                message += ", la facture est totalement soldee";
            } else {
                invoice.setState(Enumeration.INVOICE_STATE.IN_PAYMENT);
            }

            // update invoice payment count
            if (invoice.getPaymentCount()==0) {
                // generate payment number
                newPayment.setNumber(invoice.getNumber());
            } else {
                newPayment.setNumber(invoice.getNumber() + "_" + invoice.getPaymentCount());
            }
            invoice.setPaymentCount(invoice.getPaymentCount() + 1);
            invoiceRepository.save(invoice);

            paymentRepository.save(newPayment);

            return ResponseBody.with(newPayment, message + " !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }


    public ResponseBody save(Payment payment){
        try {
            String message = "Paiement effectué avec succes";
            Optional<Invoice> invoice = invoiceRepository.findById(payment.getInvoice().getId());
            if (!invoice.isPresent()){
                return ResponseBody.error("Cette facture n'existe pas");
            }
            Invoice invoiceToPaid = invoice.get();
            payment.setInvoice(invoiceToPaid);
            payment.setBalanceBefore(invoiceToPaid.getStayToPay());

            double newBalance = payment.getBalanceBefore() - payment.getAmount();
            payment.setBalanceAfter(newBalance);
            payment.setState(Enumeration.PAYMENT_STATE.PENDING);
            payment.setCreateDate(new Date());
            Payment newPayment = paymentRepository.save(payment);

            //update invoice after payment
            invoiceToPaid.setStayToPay(newBalance);
            invoiceToPaid.setAmountPaid(newPayment.getAmount() + invoiceToPaid.getAmountPaid());
            invoiceToPaid.setLastPaymentDate(new Date());
            invoiceToPaid.setUpdateDate(new Date());
            if (invoiceToPaid.getStayToPay() == 0){
                invoiceToPaid.setPaid(true);
                invoiceToPaid.setState(Enumeration.INVOICE_STATE.PAID);
                newPayment.setState(Enumeration.PAYMENT_STATE.VALIDATE);
                message += ", la facture est totalement soldée";
            }else {
                invoiceToPaid.setState(Enumeration.INVOICE_STATE.IN_PAYMENT);
            }
            invoiceRepository.save(invoiceToPaid);
            paymentRepository.save(newPayment);
            return ResponseBody.with(newPayment, message + " !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody update(Payment payment){
        try {
            if (!paymentRepository.findById(payment.getId()).isPresent()){
                return ResponseBody.error("Cet Paiement n'existe pas !");
            }
            return ResponseBody.with(create(payment),"Paiement modifiee avec succes !");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody cancelPayment(Payment payment) {
        try {

            Invoice invoice = invoiceRepository.findInvoiceById(payment.getInvoice().getId());
            if (invoice == null) {
                return ResponseBody.error("Cette facture n'existe pas");
            }

            List<Payment> invoicePayments = paymentRepository.findAllByIdGreaterThanAndInvoiceIdAndCanceledAndStateEquals(
                    payment.getId(), invoice.getId(), false, Enumeration.PAYMENT_STATE.PENDING);
            if (invoicePayments != null && !invoicePayments.isEmpty()) {
                return ResponseBody.error("Impossible d'annuler ce paiement\nVous devez d'abord annuler les enregistrements plus recents");
            }

            double newBalance = payment.getAmount() + invoice.getStayToPay();
            invoice.setStayToPay(newBalance);

            invoice.setAmountPaid(invoice.getAmountPaid() - payment.getAmount());
            invoice.setPaid(false);

            if (invoice.getAmountPaid() == 0) {
                invoice.setLastPaymentDate(null);
                invoice.setPaymentCount(0);
                invoice.setState(Enumeration.INVOICE_STATE.VALIDATE);
            } else {
                // update payment count
                invoice.setPaymentCount(invoice.getPaymentCount() - 1);
                invoice.setState(Enumeration.INVOICE_STATE.IN_PAYMENT);
            }
            invoiceRepository.save(invoice);


            paymentRepository.delete(payment);

            return ResponseBody.success("Paiement annulé avec succes !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody getById(Long id) {
        try {
            Payment invoicePayment = paymentRepository.findPaymentById(id);
            String amountInLetter = ProjectUtils.convertNumberToWord(invoicePayment.getNetToPay()) + " (" + new String(ProjectUtils.formatNumber(invoicePayment.getNetToPay())).replaceAll("\\s", ".") + ")".toLowerCase();
            invoicePayment.setAmountInLetter(amountInLetter);
            return ResponseBody.with(invoicePayment, "Paiement recuperee avec succes !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }
}

