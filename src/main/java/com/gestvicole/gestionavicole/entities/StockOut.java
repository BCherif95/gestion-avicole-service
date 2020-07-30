package com.gestvicole.gestionavicole.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gestvicole.gestionavicole.parents.AuditableParent;
import com.gestvicole.gestionavicole.utils.Enumeration;
import lombok.*;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stock_out")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StockOut extends AuditableParent<StockOut> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "quantity_out")
    private Integer quantityOut = 0;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date date;
    @Enumerated(EnumType.STRING)
    private Enumeration.STOCK_OUT_STATE state = Enumeration.STOCK_OUT_STATE.WAITING;
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}
