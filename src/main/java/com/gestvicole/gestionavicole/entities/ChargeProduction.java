package com.gestvicole.gestionavicole.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "charge_productions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargeProduction {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "charge_id", referencedColumnName = "id")
    private Charge charge;

    @ManyToOne
    @JoinColumn(name = "production_id", referencedColumnName = "id")
    private Production production;
}
