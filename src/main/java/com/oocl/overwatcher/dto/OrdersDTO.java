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

}
