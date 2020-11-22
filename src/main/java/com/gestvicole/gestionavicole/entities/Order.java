package com.gestvicole.gestionavicole.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gestvicole.gestionavicole.entities.parents.AbstractEntity;
import com.gestvicole.gestionavicole.utils.Enumeration;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private String reference;
    @Column(name = "order_date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date orderDate;
    @Enumerated(EnumType.STRING)
    private Enumeration.ORDER_STATE state = Enumeration.ORDER_STATE.WAITING;
    private Integer quantity;
    @Column(name = "unit_price")
    private Double unitPrice;
    private Double amount; // montant Hors Tax
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Production production;

}
