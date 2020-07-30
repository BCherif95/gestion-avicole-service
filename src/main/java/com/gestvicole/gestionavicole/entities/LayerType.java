package com.gestvicole.gestionavicole.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "layer_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LayerType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    @ManyToOne
    private Building building;
}
