package com.gestvicole.gestionavicole.entities;

import com.gestvicole.gestionavicole.parents.Auditable;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "building")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Building extends Auditable<Building> implements Serializable {
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
