package com.gestvicole.gestionavicole.entities;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Map;

@XmlRootElement
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class AuthzRole implements Serializable {

    private String name;
    private String type;
    private Map<String, Boolean> authorizations;

    public AuthzRole(String name) {
        this.name = name;
    }
}
