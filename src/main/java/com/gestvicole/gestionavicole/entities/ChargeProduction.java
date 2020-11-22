package com.gestvicole.gestionavicole.entities;

import com.gestvicole.gestionavicole.entities.parents.AbstractEntity;
import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "charge_productions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChargeProduction extends AbstractEntity {
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
