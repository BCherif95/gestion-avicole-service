package com.gestvicole.gestionavicole.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gestvicole.gestionavicole.parents.Auditable;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "production")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Production extends Auditable<Production> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "overall_production")
    private Integer overallProduction;
    private Integer mortality;
    @Column(name = "general_effective")
    private Integer generalEffective;
    @Column(name = "commercial_productions")
    private Integer commercialProductions;
    @Column(name = "sold_production")
    private Integer soldProduction = 0;
    @Column(name = "alveolus_broken")
    private Integer alveolusBroken;
    @Column(name = "prod_double_yellow")
    private Integer prodDoubleYellow;
    @Column(name = "prod_small_alveolus")
    private Integer prodSmallAlveolus;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date date;
    @ManyToOne
    private Building building;
}

