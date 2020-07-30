package com.gestvicole.gestionavicole.utils.dashboard;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ProdDashBody implements Serializable {
    private int totalProd;
    private int totalEffective;
    private int totalMortality;
    private int totalAlveolusBroken;
    List<LineGraphBody> lineGraphBodies = new ArrayList<>();
}
