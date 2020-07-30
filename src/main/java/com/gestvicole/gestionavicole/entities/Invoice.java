package com.gestvicole.gestionavicole.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gestvicole.gestionavicole.parents.Auditable;
import com.gestvicole.gestionavicole.utils.Enumeration;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Invoice extends Auditable<Invoice> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reference;
    private String number;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "invoice_date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date invoiceDate;
    @Enumerated(EnumType.STRING)
    private Enumeration.INVOICE_STATE state = Enumeration.INVOICE_STATE.OPEN;
    @Column(name = "method_of_payment")
    @Enumerated(EnumType.STRING)
    private Enumeration.METHOD_OF_PAYMENT methodOfPayment;
    private Integer quantity;
    @Column(name = "unit_price")
    private Double unitPrice;
    private Double amount; // montant Hors Tax
    @Column(name = "amount_in_letter")
    private transient String amountInLetter;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Order order;
    @Column(name = "amount_paid")
    private Double amountPaid = 0D; // montant payer
    @Column(name = "stay_to_pay")
    private Double stayToPay = 0D; // balance = reste a payer
    private boolean paid = false;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_payment_date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date lastPaymentDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "due_date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date dueDate;
}
