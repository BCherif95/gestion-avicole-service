package com.gestvicole.gestionavicole.entities;

import com.gestvicole.gestionavicole.entities.parents.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "layer_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LayerType extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    @ManyToOne
    private Building building;
}
