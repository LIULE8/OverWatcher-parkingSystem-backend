package com.oocl.overwatcher.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 描述: 登录类型
 *
 * @author LIULE9
 * @create 2018-11-21 7:22 PM
 */
@Getter
@AllArgsConstructor
public enum LoginStatusEnum {

  /**
   * 登录失败
   */
  FAIL_USERNAME(0, "登录失败，用户不存在或已冻结"),

  /**
   * 登录失败
   */
  FAIL_PASSWORD(1, "登录失败，密码错误"),

  /**
   * 登录成功
   */
  SUCCESS(2, "登录成功");

  private Integer code;

  private String message;

}
