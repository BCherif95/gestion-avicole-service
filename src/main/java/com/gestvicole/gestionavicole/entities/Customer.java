package com.gestvicole.gestionavicole.entities;

import com.gestvicole.gestionavicole.entities.parents.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "mobile_phone")
    private String mobilePhone;
    @Column(name = "home_phone")
    private String homePhone;
    private String address;
}
