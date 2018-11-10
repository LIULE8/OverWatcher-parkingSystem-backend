package com.oocl.overwatcher.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 描述: 订单状态
 *
 * @author LIULE9
 * @create 2018-11-07 2:21 PM
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
  /**
   * 存取中
   */
  YES(0, "存取中"),
  /**
   * 无人处理
   */
  NO(0, "无人处理"),
  /**
   * 停车完成
   */
  PARK_DONE(2, "停车完成"),
  /**
   * 取车完成
   */
  UNPARK_DONE(3, "取车完成");

  private Integer code;
  private String message;
}