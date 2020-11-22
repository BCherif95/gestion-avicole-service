package com.gestvicole.gestionavicole.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gestvicole.gestionavicole.entities.parents.AbstractEntity;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Table(name = "user")
public class User extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lastname;
    private String firstname;
    @Column(name = "user_function")
    private String function;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String telephone;
    private String email;
    private String address;
    @Column(name = "turnover_target")
    private Long turnoverTarget;
    private boolean enabled = true;
    @Column(name = "logged_in")
    private boolean loggedIn = false;
    @Lob
    private byte[] datas;
    private transient String token;
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id")
            },
            inverseJoinColumns = @JoinColumn(name = "role_name"))
    private Set<Role> roles = new HashSet<>();

    public User(User oldUser, User updateUser) {
        this.id = oldUser.getId();
        this.lastname = updateUser.getLastname();
        this.firstname = updateUser.getFirstname();
        this.function = updateUser.getFunction();
        this.username = updateUser.getUsername();
        this.telephone = updateUser.getTelephone();
        this.email = updateUser.getEmail();
        this.address = updateUser.getAddress();
        this.turnoverTarget = updateUser.getTurnoverTarget();
        this.setCreateDate(updateUser.getCreateDate());
        this.setUpdateDate(updateUser.getUpdateDate());
        this.enabled = updateUser.isEnabled();
        this.loggedIn = updateUser.isLoggedIn();
        this.datas = updateUser.getDatas();
        this.token = updateUser.getToken();
        this.roles = updateUser.getRoles();
    }
}
