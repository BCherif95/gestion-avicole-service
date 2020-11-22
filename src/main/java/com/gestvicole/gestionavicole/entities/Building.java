package com.gestvicole.gestionavicole.entities;

import com.gestvicole.gestionavicole.entities.parents.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "building")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Building extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private String description;
    @Column(name = "total_layer")
    private Integer totalLayer;
    private boolean active = true;
}
