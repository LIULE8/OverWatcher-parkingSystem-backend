package com.oocl.overwatcher.service.impl;

import com.oocl.overwatcher.dto.ChangeParkingLotDTO;
import com.oocl.overwatcher.entities.ParkingLot;
import com.oocl.overwatcher.entities.User;
import com.oocl.overwatcher.enums.UserStatusEnum;
import com.oocl.overwatcher.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * @author LIULE9
 */
@Service
@Slf4j
public class UserServiceImpl {

  /**
   * 限制分钟内不能连续打卡两次
   */
  private static final Integer LIMIT_MINUTE = 10;
  /**
   * 打卡条件是比较分钟数
   */
  private static final Integer CLOCK_OUT_LIMIT_CONDITION = 1000 * 60;
  private static final Integer LIMIT_BEGIN_CLOCK_OUT = 0;
  private static final Integer ON_DUTY_BEGIN_CLOCK_OUT = 7;
  private static final Integer ON_DUTY_END_CLOCK_OUT = 10;
  private static final Integer OFF_DUTY_BEGIN_CLOCK_OUT = 18;
  private static final Integer OFF_DUTY_END_CLOCK_OUT = 25;
  private static final String CONDITION_STATUS = "status";
  private static final String CONDITION_NAME = "name";
  private static final String CONDITION_EMAIL = "email";
  private static final String CONDITION_PHONE = "phone";


  private final UserRepository userRepository;

  private final ParkingLotServiceImpl parkingLotServiceImpl;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, ParkingLotServiceImpl parkingLotServiceImpl) {
    this.userRepository = userRepository;
    this.parkingLotServiceImpl = parkingLotServiceImpl;
  }

  public Optional<User> findOne(Long id) {
    return userRepository.findById(id);
  }

  public Page<User> findAllUserByPage(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  public Page<User> findAllUsersByConditionAndPage(String condition, String value, Pageable pageable) {
    return userRepository.findAll(((root, query, criteriaBuilder) -> {
      Predicate predicate = null;
      switch (condition) {
        case CONDITION_STATUS:
          predicate = criteriaBuilder.equal(root.get("status"), value);
          break;
        case CONDITION_NAME:
          predicate = criteriaBuilder.equal(root.get("name"), "%" + value + "%");
          break;
        case CONDITION_EMAIL:
          predicate = criteriaBuilder.equal(root.get("email"), "%" + value + "%");
          break;
        case CONDITION_PHONE:
          predicate = criteriaBuilder.equal(root.get("phone"), "%" + value + "%");
          break;
        default:
          break;
      }
      return predicate;
    }), pageable);
  }

  @Transactional
  public User addUser(User user) {
    User result = new User();
    user.setUserName(getRandomString(3));
    user.setPassword(getRandomString(3));
    log.info("===生成的用户名和密码===, username={}, password={}", user.getUserName(), user.getPassword());
    BeanUtils.copyProperties(user, result);
    String encodePassword = new BCryptPasswordEncoder().encode(user.getPassword());
    user.setPassword(encodePassword);
    user.setStatus(UserStatusEnum.ON_DUTY.getMessage());
    userRepository.save(user);
    return result;
  }

  @Transactional
  public void aliveOrFreezeUser(Long userId) {
    Optional<User> userOptional = findOne(userId);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      user.setAlive(!user.getAlive());
    }
    throw new RuntimeException("参数错误");
  }

  @Transactional(rollbackOn = RuntimeException.class)
  public List<ParkingLot> assignParkingLotNoOwner(ChangeParkingLotDTO changeParkingLotDTO) throws RuntimeException {
    List<Long> parkingLotIdList = changeParkingLotDTO.getParkingLotId();
    if (parkingLotIdList == null) {
      throw new RuntimeException("为指定需要设置的停车场id");
    }
    for (Long id : parkingLotIdList) {
      ParkingLot parkingLot = parkingLotServiceImpl.findOne(id).orElseThrow(() -> new RuntimeException("未找到该停车场"));
      parkingLot.setUser(null);
    }
    return parkingLotServiceImpl.findAll();
  }

  @Transactional(rollbackOn = RuntimeException.class)
  public List<ParkingLot> assignParkingLotToAnotherParkingBoy(ChangeParkingLotDTO changeParkingLotDTO) {
    List<Long> parkingLotIdList = changeParkingLotDTO.getParkingLotId();
    if (parkingLotIdList == null) {
      throw new RuntimeException("为指定需要设置的停车场id");
    }

    for (Long id : parkingLotIdList) {
      ParkingLot parkingLot = parkingLotServiceImpl.findOne(id).orElseThrow(() -> new RuntimeException("未找到该停车场"));
      User user = findOne(changeParkingLotDTO.getUserId()).orElseThrow(() -> new RuntimeException("未找到该用户"));
      parkingLot.setUser(user);
    }
    return parkingLotServiceImpl.findAll();
  }

  @Transactional
  public void assignParkingLotToParkingBoy(Long parkingBoyId, Long parkingLotId) {
    User parkingBoy = userRepository.findById(parkingBoyId).orElseThrow(() -> new RuntimeException("未找到该用户"));
    ParkingLot parkingLot = parkingLotServiceImpl.findOne(parkingLotId).orElseThrow(() -> new RuntimeException("未找到该停车场"));
    if (parkingLot.getUser() == null) {
      parkingLot.setUser(parkingBoy);
      parkingBoy.getParkingLotList().add(parkingLot);
      userRepository.save(parkingBoy);
    }
    throw new RuntimeException("该停车场已有人管理");
  }

  @Transactional
  public User userClockOutWhenOnWorkOrAfterWork(Long id) {
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Optional<User> userOptional = findOne(id);
    if (!userOptional.isPresent()) {
      throw new RuntimeException("该用户不存在");
    }
    User user = userOptional.get();
    Date curTime;
    Date signTime;
    ZonedDateTime now;

    try {
      now = ZonedDateTime.now();
      curTime = sdf.parse(now.format(df));
      signTime = sdf.parse(user.getSignTime().format(df));
    } catch (ParseException e) {
      throw new RuntimeException("时间转换出错, signTime=" + user.getSignTime());
    }

    //频繁打卡问题：10分钟内不允许打卡两次
    if ((curTime.getTime() - signTime.getTime()) / CLOCK_OUT_LIMIT_CONDITION < LIMIT_MINUTE) {
      throw new RuntimeException("该员工频繁打卡");
    }

    //非打卡时间打卡
    if (now.getHour() > LIMIT_BEGIN_CLOCK_OUT && now.getHour() < ON_DUTY_BEGIN_CLOCK_OUT) {
      throw new RuntimeException("非打卡时间打卡");
    }

    //1. 上班
    String userStatus = user.getStatus();
    if (userStatus.equals(UserStatusEnum.OFF_DUTY.getMessage()) || userStatus.equals(UserStatusEnum.LEAVE_EARLY.getMessage())) {
      String newStatus = now.getHour() < ON_DUTY_END_CLOCK_OUT ? UserStatusEnum.ON_DUTY.getMessage() : UserStatusEnum.LATE.getMessage();
      user.setStatus(newStatus);
    }

    //2. 下班
    if (userStatus.equals(UserStatusEnum.ON_DUTY.getMessage()) || userStatus.equals(UserStatusEnum.LATE.getMessage())) {
      String newStatus = now.getHour() < OFF_DUTY_END_CLOCK_OUT && now.getHour() > OFF_DUTY_BEGIN_CLOCK_OUT ? UserStatusEnum.OFF_DUTY.getMessage() : UserStatusEnum.LEAVE_EARLY.getMessage();
      user.setStatus(newStatus);
    }

    user.setSignTime(now);
    return user;
  }

  @Transactional
  public User updateUser(Long userId, User user) {
    User dbUser = findOne(userId).orElseThrow(() -> new RuntimeException("找不到该用户"));
    dbUser.setName(user.getName());
    dbUser.setEmail(user.getEmail());
    dbUser.setPhone(user.getPhone());
    dbUser.setUserName(user.getUserName());
    dbUser.setRoleList(user.getRoleList());
    return dbUser;
  }

  /**
   * 随机生成长度为 length 的字符串
   *
   * @param length length表示生成字符串的长度
   * @return
   */
  @SuppressWarnings("all")
  private String getRandomString(int length) {
    String base = "abcdefghijklmnopqrstuvwxyz";
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      int number = random.nextInt(base.length());
      sb.append(base.charAt(number));
    }
    return sb.toString();
  }

}
