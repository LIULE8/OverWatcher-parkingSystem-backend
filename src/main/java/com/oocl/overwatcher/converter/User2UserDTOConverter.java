package com.oocl.overwatcher.converter;

import com.oocl.overwatcher.dto.UserDTO;
import com.oocl.overwatcher.entities.ParkingLot;
import com.oocl.overwatcher.entities.Role;
import com.oocl.overwatcher.entities.User;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述: user 转 userDTO
 *
 * @author LIULE9
 * @create 2018-11-12 10:05 AM
 */
public class User2UserDTOConverter {

  public static UserDTO convert(User user) {
    UserDTO userDTO = new UserDTO();
    BeanUtils.copyProperties(user, userDTO);

    if (user.getRoleList() != null && user.getRoleList().size() > 0) {
      userDTO.setRoleList(user.getRoleList().stream().map(Role::getName).collect(Collectors.toList()));
    }

    userDTO.setParkingLotIdList(user.getParkingLotList().stream().map(ParkingLot::getParkingLotId).collect(Collectors.toList()));
    return userDTO;
  }

  public static List<UserDTO> convert(List<User> userList) {
    return userList.stream().map(User2UserDTOConverter::convert).collect(Collectors.toList());
  }

}