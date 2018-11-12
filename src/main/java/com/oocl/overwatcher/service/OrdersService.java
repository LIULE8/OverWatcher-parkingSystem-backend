package com.oocl.overwatcher.service;

import com.oocl.overwatcher.converter.Order2OrderDTOConverter;
import com.oocl.overwatcher.dto.OrderDTO;
import com.oocl.overwatcher.entities.Order;
import com.oocl.overwatcher.entities.ParkingLot;
import com.oocl.overwatcher.entities.User;
import com.oocl.overwatcher.enums.OrderStatusEnum;
import com.oocl.overwatcher.enums.OrderTypeEnum;
import com.oocl.overwatcher.repositories.OrdersRepository;
import com.oocl.overwatcher.repositories.ParkingLotRepository;
import com.oocl.overwatcher.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author LIULE9
 */
@Service
public class OrdersService {

  private final OrdersRepository ordersRepository;

  private final UserRepository userRepository;

  private final ParkingLotRepository parkingLotRepository;

  @Autowired
  public OrdersService(OrdersRepository ordersRepository, UserRepository userRepository, ParkingLotRepository parkingLotRepository) {
    this.ordersRepository = ordersRepository;
    this.userRepository = userRepository;
    this.parkingLotRepository = parkingLotRepository;
  }

  public Optional<Order> findOrderByOrderId(Integer orderId) {
    return ordersRepository.findById(orderId);
  }

  public Optional<Order> findOrderWhichCarInParkingLotByCarId(String carId) {
    return ordersRepository.findByCarIdAndOrderStatusNot(carId, "取车成功");
  }

  public boolean isExistInParkingLotCarId(String carId) {
    Optional<Order> orderOptional = findOrderWhichCarInParkingLotByCarId(carId);
    return orderOptional.isPresent();
  }

  public List<Order> findAllOrderWhichCarIdIs(String carId) {
    return ordersRepository.findByCarId(carId);
  }

  public List<Order> findAfterOrder(Long boyId) {
    User parkingBoy = new User();
    parkingBoy.setId(boyId);
    return ordersRepository.findByUserAndOrderStatus(parkingBoy, "存取中");
  }

  public List<Order> findByCondition(String condition, String value, Pageable pageable) {
    return ordersRepository.findAll((root, query, criteriaBuilder) -> {
      Predicate predicate = null;
      if ("type".equals(condition)) {
        predicate = criteriaBuilder.equal(root.get("type"), value);
      } else if ("status".equals(condition)) {
        predicate = criteriaBuilder.equal(root.get("status"), value);
      }
      return predicate;
    }, pageable).getContent();
  }

  public List<Order> showHistoryOrdersByUserId(Long userId, String status) {
    User parkingBoy = new User();
    parkingBoy.setId(userId);
    return ordersRepository.findByUserAndOrderStatus(parkingBoy, status);
  }

  public Page<Order> getOrders(PageRequest pageRequest) {
    return ordersRepository.findAll(pageRequest);
  }

  @Transactional
  public List<Order> addOrders(Order order) {
    ordersRepository.save(order);
    return ordersRepository.findAll();
  }

  @Transactional
  public OrderDTO assignOrderToParkingBoy(Integer orderId, Long boyId) {
    Optional<User> userOptional = userRepository.findById(boyId);
    if (userOptional.isPresent()) {
      Optional<Order> orderOptional = ordersRepository.findById(orderId);
      if (orderOptional.isPresent()) {
        User parkingBoy = userOptional.get();
        List<ParkingLot> parkingLots = parkingBoy.getParkingLotList();
        if (parkingLots.stream().filter(parkingLot -> parkingLot.getSize() != 0).collect(Collectors.toList()).size() != 0) {
          Order order = orderOptional.get();
          order.setUser(parkingBoy);
          order.setOrderStatus(OrderStatusEnum.YES.getMessage());
          return Order2OrderDTOConverter.convert(order);
        }
        throw new RuntimeException("该停车员管理的停车场全部停满，分配失败");
      }
    }
    throw new RuntimeException("参数错误");
  }

  @Transactional(rollbackOn = RuntimeException.class)
  public OrderDTO finishParkOrder(Integer orderId, Long parkingLotId) {
    //1. 停车场的容量减一
    Optional<ParkingLot> parkingLogOptional = parkingLotRepository.findById(parkingLotId);
    if (parkingLogOptional.isPresent()) {
      ParkingLot parkingLot = parkingLogOptional.get();
      if (parkingLot.getSize() > 1) {
        parkingLot.setSize(parkingLot.getSize() - 1);
        Optional<Order> orderOptional = ordersRepository.findById(orderId);
        if (orderOptional.isPresent()) {
          Order order = orderOptional.get();
          //2. 设置订单的停车场
          order.setParkingLot(parkingLot);
          //3. 设置订单的状态
          order.setOrderStatus(OrderStatusEnum.PARK_DONE.getMessage());
          //4. 返回该订单DTO
          return Order2OrderDTOConverter.convert(order);
        }
        throw new RuntimeException("没有该订单");
      }
      throw new RuntimeException("该停车场容量不足");
    }
    throw new RuntimeException("参数错误");
  }

  @Transactional
  public OrderDTO createUnParkOrders(String carId) {
    if (StringUtils.isNotBlank(carId)) {
      throw new RuntimeException("参数错误");
    }
    Optional<Order> orderOptional = findOrderWhichCarInParkingLotByCarId(carId);
    if (orderOptional.isPresent()) {
      Order parkOrder = orderOptional.get();
      Order unParkOrder = new Order();
      BeanUtils.copyProperties(unParkOrder, parkOrder);
      unParkOrder.setOrderId(null);
      unParkOrder.setOrderType(OrderTypeEnum.UNPARK.getMessage());
      unParkOrder.setOrderStatus(OrderStatusEnum.YES.getMessage());
      ordersRepository.save(unParkOrder);
      return Order2OrderDTOConverter.convert(unParkOrder);
    }
    throw new RuntimeException("carId错误或者该车已不在停车场");
  }

  @Transactional
  public OrderDTO finishUnParkOrder(String carId) {
    if (StringUtils.isNotBlank(carId)) {
      throw new RuntimeException("参数错误");
    }

    //1. 修改订单状态
    Optional<Order> orderOptional = findOrderWhichCarInParkingLotByCarId(carId);
    if (orderOptional.isPresent()) {
      Order unParkOrder = orderOptional.get();
      ParkingLot parkingLot = unParkOrder.getParkingLot();
      //2. 停车场容量加1
      parkingLot.setSize(parkingLot.getSize() + 1);
      unParkOrder.setOrderStatus(OrderStatusEnum.UNPARK_DONE.getMessage());
    }

    throw new RuntimeException("carId错误或者该车已不在停车场");
  }
}
