package com.gestvicole.gestionavicole.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@XmlRootElement
@Table(name = "privilege")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Privilege implements GrantedAuthority, Serializable {

    @Id
    private String name;
    private String description;

    @Override
    public String getAuthority() {
        return name;
    }
}
