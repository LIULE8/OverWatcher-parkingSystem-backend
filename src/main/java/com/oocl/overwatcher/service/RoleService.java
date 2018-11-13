package com.oocl.overwatcher.service;

import com.oocl.overwatcher.entities.Role;

/**
 * 描述: 角色服务接口
 *
 * @author LIULE9
 * @create 2018-11-13 3:08 PM
 */
public interface RoleService {

  /**
   * 根据角色名查找角色对象
   * @param roleName
   * @return
   */
  Role findRoleByName(String roleName);

}