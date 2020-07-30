package com.gestvicole.gestionavicole.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gestvicole.gestionavicole.parents.AuditableParent;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stock_entry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StockEntry extends AuditableParent<StockEntry> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "quantity_entry")
    private Integer quantityEntry = 0;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date date;
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;
    private String observation;
}
