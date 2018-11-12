package com.oocl.overwatcher.converter;

import com.oocl.overwatcher.dto.RoleDTO;
import com.oocl.overwatcher.entities.Role;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述: role 转 roleDTO
 *
 * @author LIULE9
 * @create 2018-11-12 10:48 AM
 */
public class Role2RoleDTOConverter {

  public static RoleDTO convert(Role role) {
    RoleDTO roleDTO = new RoleDTO();
    BeanUtils.copyProperties(role, roleDTO);
    roleDTO.setUserDTOList(User2UserDTOConverter.convert(role.getUsers()));
    return roleDTO;
  }

  public static List<RoleDTO> convert(List<Role> roleList) {
    return roleList.stream().map(Role2RoleDTOConverter::convert).collect(Collectors.toList());
  }
}