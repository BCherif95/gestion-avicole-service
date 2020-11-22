package com.gestvicole.gestionavicole.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gestvicole.gestionavicole.entities.parents.AbstractEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stock_entry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StockEntry extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "quantity_entry")
    private Double quantityEntry = 0D;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date date;
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;
    private String observation;
}
