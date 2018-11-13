package com.oocl.overwatcher.service;

import com.oocl.overwatcher.dto.ChangeParkingLotDTO;
import com.oocl.overwatcher.entities.ParkingLot;
import com.oocl.overwatcher.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 描述: 用户服务接口
 *
 * @author LIULE9
 * @create 2018-11-13 3:08 PM
 */
public interface UserService {

  /**
   * 查找一个user
   * @param id
   * @return
   */
  Optional<User> findOne(Long id);

  /**
   *  把停车场设为无人管理
   * @param changeParkingLotDTO
   * @return
   * @throws RuntimeException
   */
  List<ParkingLot> assignParkingLotNoOwner(ChangeParkingLotDTO changeParkingLotDTO) throws RuntimeException;

  /**
   * 把无人管理的停车场交由当前停车员管理
   * @param changeParkingLotDTO
   * @return
   */
  List<ParkingLot> assignParkingLotToAnotherParkingBoy(ChangeParkingLotDTO changeParkingLotDTO);

  /**
   * 分页查询所有用户
   * @param pageable
   * @return
   */
  Page<User> findAllUserByPage(Pageable pageable);

  /**
   * 分页，条件查询所有用户
   * @param condition
   * @param value
   * @param pageable
   * @return
   */
  Page<User> findAllUsersByConditionAndPage(String condition, String value, Pageable pageable);

  /**
   * 新增一个用户，并且返回
   * @param user
   * @return
   */
  User addUser(User user);

  /**
   * 激活或者冻结用户
   * @param userId
   */
  void aliveOrFreezeUser(Long userId);

  /**
   * 修改用户信息
   * @param userId
   * @param user
   * @return
   */
  User updateUser(Long userId, User user);

  /**
   * 员工上下班打卡
   * @param id
   * @return
   */
  User userClockOutWhenOnWorkOrAfterWork(Long id);

}