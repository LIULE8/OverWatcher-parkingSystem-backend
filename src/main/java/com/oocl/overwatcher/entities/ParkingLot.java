package com.oocl.overwatcher.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class ParkingLot {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long parkingLotId;

  private String parkingLotName;

  private int size;

  private int initSize;

  private String status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @JsonIgnore
  private User user;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "user")
  @JsonIgnore
  private List<Order> orderList = new ArrayList<>();

  public ParkingLot(String parkingLotName, int size) {
    this.parkingLotName = parkingLotName;
    this.size = size;
  }

}
