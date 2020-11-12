package com.gestvicole.gestionavicole.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gestvicole.gestionavicole.parents.AuditableParent;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Stock extends AuditableParent<Stock> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "quantity_entry")
    private Double quantityEntry = 0D;
    @Column(name = "quantity_out")
    private Double quantityOut = 0D;
    private Double solde = 0D;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date date;
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;
}
