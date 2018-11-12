package com.oocl.overwatcher.controller;

import com.oocl.overwatcher.converter.User2UserDTOConverter;
import com.oocl.overwatcher.dto.UserDTO;
import com.oocl.overwatcher.entities.User;
import com.oocl.overwatcher.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LIULE9
 */
@RestController
@RequestMapping("/roles")
public class RoleController {

  private static final String CONDITION_NAME = "name";
  private static final String CONDITION_EMAIL = "email";
  private static final String CONDITION_PHONE = "phone";
  private static final String CONDITION_STATUS = "status";
  private static final String CONDITION_PARKINGBOY = "员工";

  private final RoleService roleService;

  @Autowired
  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  /**
   * 根据角色名找用户
   *
   * @return
   */
  @GetMapping("/{roleName}")
  public ResponseEntity<List<UserDTO>> findAllUsersByRoleName(@PathVariable("roleName") String roleName) {
    List<User> parkingBoys = roleService.findRoleByName(roleName).getUsers();
    return ResponseEntity.ok(User2UserDTOConverter.convert(parkingBoys));
  }

  /**
   * 停车员的条件查询
   *
   * @param condition
   * @param value
   * @return
   */
  @GetMapping("/parkingBoys/condition")
  public List<UserDTO> findAllParkingBoysByCondition(String condition, String value) {
    List<User> userList = roleService.findRoleByName(CONDITION_PARKINGBOY).getUsers();
    if (StringUtils.isNotBlank(condition)) {
      switch (condition) {
        case CONDITION_NAME:
          userList = userList.stream().filter(parkingBoy -> parkingBoy.getName().contains(value)).collect(Collectors.toList());
          break;
        case CONDITION_EMAIL:
          userList = userList.stream().filter(parkingBoy -> parkingBoy.getEmail().contains(value)).collect(Collectors.toList());
          break;
        case CONDITION_PHONE:
          userList = userList.stream().filter(parkingBoy -> parkingBoy.getPhone().contains(value)).collect(Collectors.toList());
          break;
        case CONDITION_STATUS:
          userList = userList.stream().filter(parkingBoy -> parkingBoy.getStatus().contains(value)).collect(Collectors.toList());
          break;
        default:
          break;
      }
    }
    return User2UserDTOConverter.convert(userList);
  }
}
