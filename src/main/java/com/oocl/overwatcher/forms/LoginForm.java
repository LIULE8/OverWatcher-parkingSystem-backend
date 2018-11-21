package com.oocl.overwatcher.forms;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 描述: 登录表单对象
 *
 * @author LIULE9
 * @create 2018-11-21 7:32 PM
 */
@Data
public class LoginForm implements Serializable {

  private String username;

  private String password;

}