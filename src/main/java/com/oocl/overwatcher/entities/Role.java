package com.oocl.overwatcher.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LIULE9
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JoinTable(
            name = "user_role",
            joinColumns =
            @JoinColumn(name="role_id", referencedColumnName="id"),
            inverseJoinColumns =
            @JoinColumn(name="user_id",referencedColumnName="id"))
    @ManyToMany(targetEntity = User.class, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<User> users = new ArrayList<>();
}
