package com.oocl.overwatcher.controller;

import com.oocl.overwatcher.converter.ParkingLot2ParkingLotDTOConverter;
import com.oocl.overwatcher.converter.User2UserDTOConverter;
import com.oocl.overwatcher.dto.ChangeParkingLotDTO;
import com.oocl.overwatcher.dto.ParkingLotDTO;
import com.oocl.overwatcher.dto.UserDTO;
import com.oocl.overwatcher.entities.ParkingLot;
import com.oocl.overwatcher.entities.User;
import com.oocl.overwatcher.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author LIULE9
 */
@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

  private static final String CONDITION_LEFT = "left";
  private static final String CONDITION_RIGHT = "right";

  private final UserServiceImpl userServiceImpl;

  @Autowired
  public UserController(UserServiceImpl userServiceImpl) {
    this.userServiceImpl = userServiceImpl;
  }

  /**
   * 分页查询所有用户
   *
   * @param pageSize
   * @param curPage
   * @return
   */
  @GetMapping
  public ResponseEntity<List<UserDTO>> findAllUserByPage(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                         @RequestParam(value = "curPage", defaultValue = "1") Integer curPage) {
    PageRequest pageRequest = PageRequest.of(curPage, pageSize);
    List<User> userList = userServiceImpl.findAllUserByPage(pageRequest).getContent();
    return ResponseEntity.ok(User2UserDTOConverter.convert(userList));
  }

  @GetMapping("{id}")
  public ResponseEntity<UserDTO> findOne(@PathVariable("id") Long id) {
    try {
      User user = userServiceImpl.findOne(id).orElseThrow(() -> new Exception("找不到该用户"));
      return ResponseEntity.ok(User2UserDTOConverter.convert(user));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  /**
   * 找到指定停车员管理的所有停车场
   *
   * @param userId
   * @return
   */
  @GetMapping("{id}/parkingLots")
  public ResponseEntity<List<ParkingLot>> findAllParkingLotByUserId(@PathVariable("id") Long userId) {
    Optional<User> userOptional = userServiceImpl.findOne(userId);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      return ResponseEntity.ok(user.getParkingLotList());
    }
    log.error("【找到指定停车员管理的所有停车场】 该停车员不存在或停车员id错误, userId={}", userId);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  /**
   * 新增一个用户
   *
   * @param user
   * @return
   */
  @PostMapping
  public ResponseEntity<User> addUser(@RequestBody User user) {

    //1. 手动双向绑定
    user.getRoleList().forEach(role -> role.getUsers().add(user));

    //2. 新增一个用户
    User savedUser = userServiceImpl.addUser(user);

    if (savedUser != null) {
      return ResponseEntity.ok(savedUser);
    }

    log.error("【新增一个用户】 新增失败 user={}", user);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  /**
   * 指定某个停车场给停车员
   *
   * @param parkingBoyId
   * @param parkingLotId
   * @return
   */
  @PostMapping("{boyId}/parkingLot/{lotId}")
  public ResponseEntity assignParkingLotToParkingBoy(@PathVariable("boyId") Long parkingBoyId,
                                                     @PathVariable("lotId") Long parkingLotId) {
    try {
      userServiceImpl.assignParkingLotToParkingBoy(parkingBoyId, parkingLotId);
      log.info("【指定某个停车场给停车员】 指定成功, parkingBoyId={}, parkingLotId={}", parkingBoyId, parkingLotId);
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } catch (Exception e) {
      log.error("【指定某个停车场给停车员】"
          .concat(e.getMessage())
          .concat(", parkingBoyId={}, parkingLotId={}"), parkingBoyId, parkingLotId);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }


  /**
   * 员工上下班打卡
   *
   * @param userId
   * @return
   */
  @PutMapping("status/on")
  public ResponseEntity<UserDTO> userClockOutWhenOnWorkOrAfterWork(@RequestParam("userId") Long userId) {
    try {
      User user = userServiceImpl.userClockOutWhenOnWorkOrAfterWork(userId);
      return ResponseEntity.ok(User2UserDTOConverter.convert(user));
    } catch (Exception e) {
      log.error("【员工上下班打卡】 打卡失败, "
          .concat(e.getMessage())
          .concat(", userId={}"), userId);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  /**
   * 用户条件查询
   *
   * @param condition
   * @param value
   * @param pageSize
   * @param curPage
   * @return
   */
  @GetMapping("criteria")
  public ResponseEntity<List<UserDTO>> findAllUsersByConditionAndPage(@RequestParam("condition") String condition,
                                                                      @RequestParam("value") String value,
                                                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                      @RequestParam(value = "curPage", defaultValue = "1") Integer curPage) {
    if (StringUtils.isNotBlank(condition) && StringUtils.isNotBlank(value)) {
      List<User> userList = userServiceImpl.findAllUsersByConditionAndPage(condition, value, PageRequest.of(curPage, pageSize)).getContent();
      return ResponseEntity.ok(User2UserDTOConverter.convert(userList));
    }
    log.error("【用户条件查询】参数错误, condition={}, value={}", condition, value);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

  }


  /**
   * 激活或冻结某个用户
   *
   * @param userId
   * @return
   */
  @PutMapping("alive/{id}")
  public ResponseEntity aliveOrFreezeUser(@PathVariable("id") Long userId) {
    try {
      userServiceImpl.aliveOrFreezeUser(userId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (Exception e) {
      log.error("【激活或冻结某个账户】"
          .concat(e.getMessage())
          .concat(", userId={}"), userId);
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  /**
   * 改变停车场管理员
   *
   * @param changeParkingLotDTO
   * @return
   */
  @PutMapping("/assignParkingLotOwner")
  public ResponseEntity<List<ParkingLotDTO>> assignParkingLotOwner(@RequestBody ChangeParkingLotDTO changeParkingLotDTO) {
    String direction = changeParkingLotDTO.getDirection();
    if (StringUtils.isNotBlank(direction)) {
      if (CONDITION_LEFT.equals(direction)) {
        //1.把停车场设为无人管理
        List<ParkingLot> parkingLots = userServiceImpl.assignParkingLotNoOwner(changeParkingLotDTO);
        return ResponseEntity.ok(ParkingLot2ParkingLotDTOConverter.convert(parkingLots));
      } else if (CONDITION_RIGHT.equals(direction)) {
        //2.把无人管理的停车场交由当前停车员管理
        List<ParkingLot> parkingLots = userServiceImpl.assignParkingLotToAnotherParkingBoy(changeParkingLotDTO);
        return ResponseEntity.ok(ParkingLot2ParkingLotDTOConverter.convert(parkingLots));
      }
    }
    log.error("【改变停车场管理员】参数错误, changeParkingLotDTO={}", changeParkingLotDTO);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  /**
   * 修改用户信息
   *
   * @param userId
   * @param user
   * @return
   */
  @PutMapping("{id}")
  public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long userId, @RequestBody User user) {
    try {
      User newer = userServiceImpl.updateUser(userId, user);
      return ResponseEntity.ok(User2UserDTOConverter.convert(newer));
    } catch (Exception e) {
      log.error("【修改用户信息】"
          .concat(e.getMessage())
          .concat(", userId={}, user={}"), userId, user);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }
}
