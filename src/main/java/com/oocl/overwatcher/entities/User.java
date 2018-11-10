package com.oocl.overwatcher.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String userName;
    private String password;
    private String status;
    private Boolean alive=true;
    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "users",fetch = FetchType.LAZY,targetEntity = Role.class)
    private List<Role> roleList;
    private String email;
    private String phone;
    @OneToMany(cascade = CascadeType.ALL,mappedBy="user",fetch = FetchType.LAZY)
    private List<ParkingLot> parkingLotList = new ArrayList<>();
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    @JsonIgnore
    @OneToMany(mappedBy="user",fetch = FetchType.LAZY)
    private List<Orders> ordersList = new ArrayList<>();
    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public User(Long id,String name,String password) {
        this.name = name;
        this.id=id;
        this.password=password;
    }


    public User(String name, List<Role> roleList) {
        this.name = name;
        this.roleList = roleList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }

    public List<ParkingLot> getParkingLotList() {
        return parkingLotList;
    }

    public void setParkingLotList(List<ParkingLot> parkingLotList) {
        this.parkingLotList = parkingLotList;
    }

    public Boolean getAlive() {
        return alive;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }
}
