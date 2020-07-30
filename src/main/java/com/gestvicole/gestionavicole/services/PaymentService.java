package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.entities.Invoice;
import com.gestvicole.gestionavicole.entities.Payment;
import com.gestvicole.gestionavicole.repositories.InvoiceRepository;
import com.gestvicole.gestionavicole.repositories.PaymentRepository;
import com.gestvicole.gestionavicole.utils.Enumeration;
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

    public ResponseBody create(Payment payment){
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
            payment.setState(Enumeration.PAYMENT_STATE.WAITING);
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
            Optional<Invoice> invoice = invoiceRepository.findById(payment.getInvoice().getId());
            if (!invoice.isPresent()){
                return ResponseBody.error("Cette facture n'existe pas");
            }
            Invoice invoiceToCanceled = invoice.get();
            List<Payment> payments = paymentRepository.findAllByIdGreaterThanAndInvoiceIdAndCanceledAndStateEquals(
                    payment.getId(),invoiceToCanceled.getId(),false, Enumeration.PAYMENT_STATE.WAITING);
            if (payments !=null && !payments.isEmpty()) {
                return ResponseBody.error("Impossible d'annuler ce paiement\nVous devez d'abord annuler les enregistrements plus recents");
            }
            double newBalance = payment.getAmount() + invoiceToCanceled.getStayToPay();
            invoiceToCanceled.setStayToPay(newBalance);
            invoiceToCanceled.setAmountPaid(invoiceToCanceled.getAmountPaid() - payment.getAmount());
            invoiceToCanceled.setPaid(false);
            if (invoiceToCanceled.getAmountPaid() == 0){
                invoiceToCanceled.setLastPaymentDate(null);
                invoiceToCanceled.setState(Enumeration.INVOICE_STATE.VALIDATE);
            }else {
                invoiceToCanceled.setState(Enumeration.INVOICE_STATE.IN_PAYMENT);
            }
            invoiceRepository.save(invoiceToCanceled);


            paymentRepository.delete(payment);

            return ResponseBody.success("Paiement annule avec succes !");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }
}

