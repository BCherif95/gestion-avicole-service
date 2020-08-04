package com.gestvicole.gestionavicole.entities;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@XmlRootElement
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Role implements GrantedAuthority, Serializable {

    @Id
    private String name;
    private String description;

    public Role(String name) {
        this.name = name;
    }

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(
            name = "role_privilege",
            joinColumns = {
                    @JoinColumn(name = "role_name", referencedColumnName = "name")
            },
            inverseJoinColumns = @JoinColumn(name = "privilege_name"))
    private Set<Privilege> privileges = new HashSet<>();

    @Override
    public String getAuthority() {
        return name;
    }
}
