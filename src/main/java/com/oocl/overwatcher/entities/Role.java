package com.oocl.overwatcher.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
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
    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public Role(Long id, String name) {
        this.id=id;
        this.name=name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
