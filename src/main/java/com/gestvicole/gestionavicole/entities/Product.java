package com.gestvicole.gestionavicole.entities;

import com.gestvicole.gestionavicole.parents.AuditableParent;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product extends AuditableParent<Product> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String designation;
    private String description;
    private String reference;
    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;
}
