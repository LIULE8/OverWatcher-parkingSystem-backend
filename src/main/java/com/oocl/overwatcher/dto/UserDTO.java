package com.oocl.overwatcher.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author LIULE9
 */
@Data
@NoArgsConstructor
@ToString
public class UserDTO {

  /**
   * 员工id
   */
  private Long id;

  /**
   * 员工姓名
   */
  private String name;

  /**
   * 员工状态
   */
  private String status;

  /**
   * 员工邮箱
   */
  private String email;

  /**
   * 员工手机号
   */
  private String phone;

  /**
   * 员工用户名
   */
  private String username;

  /**
   * 员工密码
   */
  private String password;

  /**
   * 员工账号是否激活 冻结
   */
  private Boolean alive;

  /**
   * 角色列表
   */
  private List<String> roleList;

  /**
   * 员工管理的停车场id列表
   */
  private List<Long> parkingLotIdList;

}
