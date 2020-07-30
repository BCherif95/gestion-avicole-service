package com.gestvicole.gestionavicole.wrapper;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ibrahim Maiga <maiga.ibrm@gmail.com>
 */
@Data
@Builder
public class ChargeItem implements Serializable {
    private String buildingName;
    private Integer production;
    private Integer total;
    private Integer effective;

    private ChargeItem(String buildingName, Integer production, Integer total, Integer effective) {
        this.buildingName = buildingName;
        this.production = production;
        this.total = total;
        this.effective = effective;
    }

    public static ChargeItem create(String buildingName, Integer production, Integer total, Integer effective) {
        return new ChargeItem(buildingName, production, total, effective);
    }
}
