package com.gestvicole.gestionavicole.utils.dashboard;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChargeGraphBody {
    private int sumTotalProd = 0;
    private int sumTotalLoad = 0;
    private int sumNetMargin = 0;
    private int sumTotalEffective = 0;
    List<LineGraphBody> lineGraphBodies = new ArrayList<>();
}
