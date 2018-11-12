package com.oocl.overwatcher.controller;

import com.oocl.overwatcher.converter.ParkingLot2ParkingLotDTOConverter;
import com.oocl.overwatcher.converter.User2UserDTOConverter;
import com.oocl.overwatcher.dto.ChangeParkingLotDTO;
import com.oocl.overwatcher.dto.ParkingLotDTO;
import com.oocl.overwatcher.dto.UserDTO;
import com.oocl.overwatcher.entities.ParkingLot;
import com.oocl.overwatcher.entities.Role;
import com.oocl.overwatcher.entities.User;
import com.oocl.overwatcher.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LIULE9
 */
@RestController
@RequestMapping("/users")
public class UserController {

  private static final String CONDITION_LEFT = "left";
  private static final String CONDITION_RIGHT = "right";

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping

  public ResponseEntity<List<UserDTO>> findAllUser() {
    List<User> userList = userService.findAllUser();
    List<UserDTO> userDTOS = User2UserDTOConverter.convert(userList);
    return ResponseEntity.ok(userDTOS);
  }

  @PostMapping

  public ResponseEntity<User> addUser(@RequestBody User user) {
    user.getRoleList().forEach(role -> {
      role.getUsers().add(user);
    });
    User afterSaveUser = userService.addUser(user);
    if (afterSaveUser != null) {
      return ResponseEntity.ok(afterSaveUser);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @PostMapping("/{parkingBoyId}/parkingLotId/{parkingLotId}")

  public ResponseEntity addParkingLotToParkingBoy(@PathVariable Long parkingBoyId, @PathVariable Long parkingLotId) {
    if (userService.addParkingLotToParkingBoy(parkingBoyId, parkingLotId)) {
      return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @GetMapping("{id}/parkingLots")

  public ResponseEntity<List<ParkingLot>> finAllParkingLotByEmployeeId(@PathVariable Long id) {
    return ResponseEntity.ok(userService.finAllParkingLotByEmployeeId(id));
  }

  @PutMapping("/status/{id}")

  public User updateUserStatus(@PathVariable Long id) {
    userService.updateStatus(id);
    return userService.findUserById(id);
  }

  @GetMapping("/onWork")

  public List<User> findAllEmployeesOnWork() {
    return userService.findAllEmployeesOnWork();
  }

  @GetMapping("/{id}")

  public ResponseEntity<UserDTO> findOne(@PathVariable("id") Long id) {
    try {
      User user = userService.findOne(id).orElseThrow(() -> new Exception("找不到该用户"));
      return ResponseEntity.ok(User2UserDTOConverter.convert(user));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @GetMapping("/name")

  public ResponseEntity<List<UserDTO>> findUsersByName(String name) {
    try {
      if (StringUtils.isNotBlank(name)) {
        List<User> userList = userService.findByName(name);
        return ResponseEntity.ok(User2UserDTOConverter.convert(userList));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }


  @GetMapping("/email")

  public ResponseEntity<List<UserDTO>> findUsersByEmail(String email) {
    try {
      if (StringUtils.isNotBlank(email)) {
        List<User> userList = userService.findByEmail(email);
        return ResponseEntity.ok(User2UserDTOConverter.convert(userList));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }


  @GetMapping("/phone")

  public ResponseEntity<List<UserDTO>> findUsersByPhone(String phone) {
    try {
      if (StringUtils.isNotBlank(phone)) {
        List<User> userList = userService.findByPhone(phone);
        return ResponseEntity.ok(User2UserDTOConverter.convert(userList));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @GetMapping("/role")

  public ResponseEntity<List<UserDTO>> findUsersByRole(String role) {
    try {
      if (StringUtils.isNotBlank(role)) {
        List<User> userList = userService.findAllUser().stream().filter(user -> whetherUserHasThisRole(user.getRoleList(), role)).collect(Collectors.toList());
        return ResponseEntity.ok(User2UserDTOConverter.convert(userList));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  /**
   * 判断用户是否有这个角色
   *
   * @param roleList
   * @param roleName
   * @return
   */
  private boolean whetherUserHasThisRole(List<Role> roleList, String roleName) {
    if (roleList != null && roleList.size() > 0) {
      for (Role role : roleList) {
        if (roleName.equals(role.getName())) {
          return true;
        }
      }
    }
    return false;
  }

  @PutMapping("/{id}/alive")
  public ResponseEntity updateAliveMessageOfEmployee(@RequestBody User user) {
    if (StringUtils.isNotBlank(user.getId() + "")) {
      if (userService.updateAlive(user)) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
      }
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @PutMapping("/changeParkingLotOwner")
  public ResponseEntity<List<ParkingLotDTO>> findAllParkingBoysByCondition(@RequestBody ChangeParkingLotDTO changeParkingLotDTO) {
    String direction = changeParkingLotDTO.getDirection();
    List<Long> parkingLotIds = changeParkingLotDTO.getParkingLotId();
    Long parkingBoyId = changeParkingLotDTO.getUserId();
    if (StringUtils.isNotBlank(direction)) {
      if (CONDITION_LEFT.equals(direction)) {
        // 把该停车员管理的parkingLotId的停车场的userId改null
        List<ParkingLot> parkingLots = userService.changeParking(parkingLotIds);
        return ResponseEntity.ok(ParkingLot2ParkingLotDTOConverter.convert(parkingLots));
      } else if (CONDITION_RIGHT.equals(direction)) {
        //把无人管理的parkingLotId的停车场改为当前停车员管理
        List<ParkingLot> parkingLots = userService.addParkingLotToPakingBoy(parkingBoyId, parkingLotIds);
        return ResponseEntity.ok(ParkingLot2ParkingLotDTOConverter.convert(parkingLots));
      }
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
    User newer = userService.updateBasicMessageOfEmployees(id, user);
    if (newer != null) {
      return ResponseEntity.ok(User2UserDTOConverter.convert(newer));
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }
}
