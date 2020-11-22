package com.gestvicole.gestionavicole.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gestvicole.gestionavicole.entities.parents.AbstractEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "charge")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Charge extends AbstractEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer price;
    private Integer dailyCharge;
    private Integer costOfDay;

    //    private Integer prodBuild1;
//    private Integer prodBuild2;
//    private Integer totalProdBuild1;
//    private Integer totalProdBuild2;
//    private Integer totalProd;
//    private Integer dayEffectiveBuild1;
//    private Integer dayEffectiveBuild2;
//    private Integer totalEffective;
//    private Integer consumption;
//    private Integer totalLoad;
//    private Integer netMargin;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date date;
}
