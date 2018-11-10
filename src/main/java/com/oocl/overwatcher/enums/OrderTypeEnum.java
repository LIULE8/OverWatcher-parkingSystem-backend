package com.oocl.overwatcher.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 描述: 订单状态
 *
 * @author LIULE9
 * @create 2018-11-07 2:06 PM
 */
@Getter
@AllArgsConstructor
public enum OrderTypeEnum {
  /**
   * 停车
   */
  PARK(0, "停车"),

  /**
   * 取车
   */
  UNPARK(1, "取车");

  private Integer code;
  private String message;

}
