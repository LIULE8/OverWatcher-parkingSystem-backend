package com.oocl.overwatcher.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 描述: 用工上班状态
 *
 * @author LIULE9
 * @create 2018-11-12 11:17 AM
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {

  /**
   * 上班
   */
  ON_DUTY(0, "上班"),

  /**
   * 下班
   */
  OFF_DUTY(1, "下班"),

  /**
   * 早退
   */
  LEAVE_EARLY(2, "早退"),

  /**
   * 迟到
   */
  LATE(3, "迟到"),

  /**
   * 忘记打卡
   */
  FORGET_TO_CLOCK_OUT(4, "忘记打卡");

  private Integer code;

  private String message;
}
