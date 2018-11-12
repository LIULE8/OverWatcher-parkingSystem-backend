package com.oocl.overwatcher.dto;

import lombok.Data;

/**
 * @author LIULE9
 */
@Data
public class ParkingLotDTO {

  /**
   * 停车场id
   */
  private Long parkingLotId;

  /**
   * 停车员名字
   */
  private String parkingLotName;

  /**
   * 停车场现容量
   */
  private Integer size;

  /**
   * 停车场初始容量
   */
  private Integer initSize;

  /**
   * 停车场状态
   */
  private String status;

  /**
   * 用户id
   */
  private Long userId;
}
