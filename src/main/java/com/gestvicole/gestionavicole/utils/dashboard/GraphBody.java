package com.gestvicole.gestionavicole.utils.dashboard;

import lombok.Data;

import java.io.Serializable;

@Data
public class GraphBody implements Serializable {

    private String name;
    private Integer value;

    public GraphBody(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}
