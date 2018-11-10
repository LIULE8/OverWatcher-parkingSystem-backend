package com.oocl.overwatcher.dto;

import com.oocl.overwatcher.entities.Role;
import com.oocl.overwatcher.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeDto {
    private Long id;
    private String name;
    private String status;
    private List<String> roleList=new ArrayList<>();
    private String email;
    private String phone;
    private String username;
    private List<Long> parkingLotIdList;
    private String password;
    private Boolean alive;
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public List<Long> getParkingLotIdList() {
        return parkingLotIdList;
    }

    public String getPassword() {
        return password;
    }


    public Boolean getAlive() {
        return alive;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }

    public EmployeeDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.status = user.getStatus();
        if (user.getRoleList()!=null&&user.getRoleList().size()>0){
            this.roleList = user.getRoleList().stream().map(role -> role.getName()).collect(Collectors.toList());
        }

        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.alive=user.getAlive();
        this.parkingLotIdList = user.getParkingLotList().stream().map(parkingLot -> parkingLot.getId()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "EmployeeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", roleList=" + roleList +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", parkingLotIdList=" + parkingLotIdList +
                ", password='" + password + '\'' +
                ", alive=" + alive +
                '}';
    }
}
