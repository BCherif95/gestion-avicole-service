package com.gestvicole.gestionavicole.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gestvicole.gestionavicole.entities.parents.AbstractEntity;
import com.gestvicole.gestionavicole.utils.Enumeration;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Payment extends AbstractEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payment_date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date paymentDate = new Date();
    private String reference;
    private String number;
    private Double amount;
    @Column(name = "net_to_pay")
    private Double netToPay;
    @Column(name = "method_of_payment")
    @Enumerated(EnumType.STRING)
    private Enumeration.METHOD_OF_PAYMENT methodOfPayment;
    @Enumerated(EnumType.STRING)
    private Enumeration.PAYMENT_STATE state;
    @Column(name = "balance_before")
    private Double balanceBefore;
    @Column(name = "balance_after")
    private Double balanceAfter;
    private Double accountPaidBefore = 0D; // acount verser
    @Column(name = "account_paid_after")
    private Double accountPaidAfter = 0D;
    @Column(name = "amount_in_letter")
    private transient String amountInLetter;
    private boolean canceled = false;
    @ManyToOne
    private Invoice invoice;
    @ManyToOne
    @JoinColumn(name = "create_by")
    private User createBy;
    @ManyToOne
    @JoinColumn(name = "validate_by")
    private User validateBy;
    @ManyToOne
    @JoinColumn(name = "cancel_by")
    private User cancelBy;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date createDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "cancel_date")
    private Date cancelDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "validate_date")
    private Date validateDate;
}
