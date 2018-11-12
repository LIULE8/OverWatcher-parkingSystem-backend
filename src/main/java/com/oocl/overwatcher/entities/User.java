package com.oocl.overwatcher.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LIULE9
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String userName;

  private String password;

  private String status;

  private String email;

  private String phone;

  private Boolean alive = true;

  private ZonedDateTime signTime;

  @ManyToMany(cascade = CascadeType.ALL, mappedBy = "users", fetch = FetchType.LAZY, targetEntity = Role.class)
  private List<Role> roleList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
  private List<ParkingLot> parkingLotList = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Order> orderList = new ArrayList<>();

  public User(String name) {
    this.name = name;
  }


  public User(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }

  public User(Long id, String name, String password) {
    this.name = name;
    this.id = id;
    this.password = password;
  }

  public User(String name, List<Role> roleList) {
    this.name = name;
    this.roleList = roleList;
  }
}
