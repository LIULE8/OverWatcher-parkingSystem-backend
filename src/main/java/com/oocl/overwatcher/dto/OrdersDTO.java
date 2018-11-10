package com.oocl.overwatcher.dto;

import lombok.Data;

import java.time.ZonedDateTime;

/**
 * @author LIULE9
 */
@Data
public class OrdersDTO {
  private Integer orderId;
  private String orderType;
  private String orderStatus;
  private String carId;
  private String name;
  private Long usersId;
  private ZonedDateTime time;

//  public OrdersDTO(Orders orders) {
//    this.orderId = orders.getOrderId();
//    this.orderType = orders.getOrderType();
//    this.orderStatus = orders.getOrderStatus();
//    this.carId = orders.getCarId();
//    this.time = orders.getCreatedDate();
//    this.name = orders.getParkingLot() != null ? orders.getParkingLot().getName() : null;
//    this.usersId = orders.getUser().getId();
//  }
}
