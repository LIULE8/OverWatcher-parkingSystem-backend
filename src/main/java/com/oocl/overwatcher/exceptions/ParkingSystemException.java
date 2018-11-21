package com.oocl.overwatcher.exceptions;

import lombok.Getter;

/**
 * 描述: 停车场系统自定义异常
 *
 * @author LIULE9
 * @create 2018-11-21 7:15 PM
 */
@Getter
public class ParkingSystemException extends RuntimeException {

  private Integer code;

  public ParkingSystemException(Integer code, String message) {
    super(message);
    this.code = code;
  }


}