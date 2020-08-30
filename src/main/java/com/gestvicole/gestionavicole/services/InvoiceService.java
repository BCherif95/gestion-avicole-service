package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.components.ParameterComponent;
import com.gestvicole.gestionavicole.entities.Customer;
import com.gestvicole.gestionavicole.entities.Invoice;
import com.gestvicole.gestionavicole.entities.Order;
import com.gestvicole.gestionavicole.repositories.CustomerRepository;
import com.gestvicole.gestionavicole.repositories.InvoiceRepository;
import com.gestvicole.gestionavicole.repositories.OrderRepository;
import com.gestvicole.gestionavicole.utils.Enumeration;
import com.gestvicole.gestionavicole.utils.ProjectUtils;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import com.gestvicole.gestionavicole.utils.SearchBody;
import com.gestvicole.gestionavicole.utils.dashboard.GraphBody;
import com.gestvicole.gestionavicole.utils.dashboard.SaleDashBody;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ParameterComponent parameterComponent;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          ParameterComponent parameterComponent,
                          OrderRepository orderRepository,
                          CustomerRepository customerRepository) {
        this.invoiceRepository = invoiceRepository;
        this.parameterComponent = parameterComponent;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public ResponseBody findAll(){
        try {
            return ResponseBody.with(invoiceRepository.findAll(),"Les factures disponibles");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody getInvoice(Long id) {
        try {
            Optional<Invoice> invoice = invoiceRepository.findById(id);
            return invoice.map(invoice1 -> {
                String amountInLetter = ProjectUtils.convertNumberToWord(invoice1.getAmount()) + " (" + new String(ProjectUtils.formatNumber(invoice1.getAmount())).replaceAll("\\s", ".") + ")".toLowerCase();
                invoice1.setAmountInLetter(amountInLetter);
                return ResponseBody.with(invoice1, "Facture recuperer avec succès");
            }).orElseGet(() -> ResponseBody.error("Cette facture n'existe pas"));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody create(Invoice invoice) {
        try {
            invoice.setNumber(parameterComponent.generateInvoiceNumber());
            invoice.setAmount(invoice.getQuantity()*invoice.getUnitPrice());
            invoice.setStayToPay(invoice.getAmount());
            Optional<Order> order = orderRepository.findById(invoice.getOrder().getId());
            if (order.isPresent()){
                Order newOrder = order.get();
                newOrder.setState(Enumeration.ORDER_STATE.MADE);
                orderRepository.save(newOrder);
            }
            //save invoice
            Invoice newInvoice = invoiceRepository.saveAndFlush(invoice);
            parameterComponent.updateInvoiceNumber();
            return ResponseBody.with(newInvoice, "Ajouter avec succes !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody update(Invoice invoice) {
        try {
            Optional<Invoice> newInvoice = invoiceRepository.findById(invoice.getId());
            if (newInvoice.isPresent()){
                Invoice invoiceUpdating = newInvoice.get();
                invoiceUpdating.setAmount(invoiceUpdating.getQuantity()*invoiceUpdating.getUnitPrice());
                invoiceRepository.save(invoiceUpdating);
                return ResponseBody.with(invoiceUpdating,"Modifier avec succes !");
            }
            return ResponseBody.error("Une erreur est survenue");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody validateAnInvoice(Invoice invoice) {
        try {

            invoice.setEditable(false);
            invoice.setState(Enumeration.INVOICE_STATE.VALIDATE);
            Invoice validatedInvoice = invoiceRepository.save(invoice);

            return ResponseBody.with(validatedInvoice, "Facture validee avec succes !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody initInvoiceDash(SearchBody searchBody) {
        try {
            SaleDashBody saleDashBody = new SaleDashBody();

            // retreive uncanceled invoice
            List<Invoice> invoices;
            // retreive uncanceled invoice
            List<Order> orders;
            // retreive uncanceled invoice
            List<Customer> customers = customerRepository.findAll();
            if (searchBody.getDate() !=null){
                invoices = invoiceRepository.findAllInvoiceByDate(searchBody.getDate());
                orders = orderRepository.findAllOrderByDate(searchBody.getDate());
            }else {
                invoices = invoiceRepository.findAll();
                orders = orderRepository.findAllOrderByState(Enumeration.ORDER_STATE.MADE);
            }

            int sumAmountPaid = 0;
            int sumStayToPay = 0;
            int totalCustomerCount = 0;
            int totalOrderCount = 0;
            for (Invoice invoice : invoices) {
                sumAmountPaid += invoice.getAmountPaid() == null ? 0 : invoice.getAmountPaid();
                sumStayToPay += invoice.getStayToPay() == null ? 0 : invoice.getStayToPay();
            }
            for (Order order : orders){
                totalOrderCount++;
            }
            for (Customer customer : customers){
                totalCustomerCount++;
            }
            saleDashBody.setSumAmountPaid(sumAmountPaid);
            saleDashBody.setSumStayToPay(sumStayToPay);
            saleDashBody.setTotalOrderCount(totalOrderCount);
            saleDashBody.setTotalCustomerCount(totalCustomerCount);

            // graph body
            initSaleGraphBody(saleDashBody);
            return ResponseBody.with(saleDashBody, "Statistique de la Vente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }

    }

    private void initSaleGraphBody(SaleDashBody saleDashBody) {
        try {
            List<GraphBody> graphBodies = new ArrayList<>();
            List<Date> last12months = ProjectUtils.getLast12Months();
            for (Date date : last12months) {
                String month = ProjectUtils.toString(date, "MMMM-yyyy", Locale.FRENCH);
                Integer sumTotal = invoiceRepository.sumTotalByInvoiceDate(date);
                graphBodies.add(new GraphBody(month.toUpperCase(), sumTotal == null ? 0 : sumTotal));
            }
            saleDashBody.setSaleGraphBodies(graphBodies);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
