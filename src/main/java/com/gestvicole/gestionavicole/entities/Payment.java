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
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Payment extends Auditable<Payment> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payment_date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date paymentDate;
    private String reference;
    private Double amount;
    @Column(name = "method_of_payment")
    @Enumerated(EnumType.STRING)
    private Enumeration.METHOD_OF_PAYMENT methodOfPayment;
    @Enumerated(EnumType.STRING)
    private Enumeration.PAYMENT_STATE state;
    @Column(name = "balance_before")
    private Double balanceBefore;
    @Column(name = "balance_after")
    private Double balanceAfter;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date createDate;
    private boolean canceled = false;
    @ManyToOne
    private Invoice invoice;
}
