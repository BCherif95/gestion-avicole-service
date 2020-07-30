package com.gestvicole.gestionavicole.utils.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class LineGraphBody implements Serializable {
    private String name;
    private Integer datasets1;
    private Integer datasets2;

}
