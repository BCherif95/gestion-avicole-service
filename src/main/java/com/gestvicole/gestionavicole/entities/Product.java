package com.gestvicole.gestionavicole.entities;

import com.gestvicole.gestionavicole.entities.parents.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product extends AbstractEntity {
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
