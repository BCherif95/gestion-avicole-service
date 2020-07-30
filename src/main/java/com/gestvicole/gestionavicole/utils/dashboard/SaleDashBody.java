package com.gestvicole.gestionavicole.utils.dashboard;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class SaleDashBody {
    private int sumAmountPaid = 0;
    private int sumStayToPay = 0;
    private int totalCustomerCount = 0;
    private int totalOrderCount = 0;
    List<GraphBody> saleGraphBodies = new ArrayList<>();

}
