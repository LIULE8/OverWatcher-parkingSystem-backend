package com.oocl.overwatcher.controller;

import com.oocl.overwatcher.converter.Order2OrderDTOConverter;
import com.oocl.overwatcher.dto.OrdersDTO;
import com.oocl.overwatcher.entities.Orders;
import com.oocl.overwatcher.entities.ParkingLot;
import com.oocl.overwatcher.entities.User;
import com.oocl.overwatcher.enums.OrderStatusEnum;
import com.oocl.overwatcher.repositories.ParkingLotRepository;
import com.oocl.overwatcher.repositories.UserRepository;
import com.oocl.overwatcher.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author LIULE9
 */
@RestController
@RequestMapping("/orders")
@Slf4j
public class OrdersController {

  private final OrdersService ordersService;

  private final UserRepository userRepository;

  private final ParkingLotRepository parkingLotRepository;

  @Autowired
  public OrdersController(OrdersService ordersService, UserRepository userRepository, ParkingLotRepository parkingLotRepository) {
    this.ordersService = ordersService;
    this.userRepository = userRepository;
    this.parkingLotRepository = parkingLotRepository;
  }

  /**
   * 查询所有订单
   *
   * @return
   */
  @GetMapping
  public ResponseEntity<List<Orders>> getOrders() {
    return ResponseEntity.ok(ordersService.getOrders());
  }

  /**
   * 查询所有订单,分页查询
   *
   * @param pageSize
   * @param curPage
   * @return
   */
  @GetMapping("/{pageSize}/{curPage}")
  public ResponseEntity<List<Orders>> getOrdersByPage(@PathVariable("pageSize") Integer pageSize,
                                                      @PathVariable("curPage") Integer curPage) {
    PageRequest pageRequest = PageRequest.of(curPage, pageSize);
    return ResponseEntity.ok(ordersService.getOrders(pageRequest).getContent());
  }

  /**
   * 根据ID查询订单
   *
   * @param orderId
   * @return
   */
  @GetMapping("/{id}")
  public ResponseEntity<List<Orders>> getOrdersByOrderId(@PathVariable("id") Integer orderId) {
    Optional<Orders> orderOptional = ordersService.findById(orderId);
    if (orderOptional.isPresent()) {
      return ResponseEntity.ok(Collections.singletonList(orderOptional.get()));
    }
    log.error("【查询订单详情】 订单id不正确, orderId={}", orderId);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }


  /**
   * 查询所有抢单后的订单
   *
   * @param boyId
   * @return
   */
  @GetMapping("/after/{boyId}")
  public ResponseEntity<List<OrdersDTO>> findAfterOrder(@PathVariable("boyId") Integer boyId) {
    return ResponseEntity.ok(Order2OrderDTOConverter.convert(ordersService.findAfterOrder(boyId)));
  }

  /**
   * 根据 车牌 carid 查询还在停车场的订单
   *
   * @param carId
   * @return
   */
  @GetMapping("/carId")
  public ResponseEntity<Orders> findByCarId(String carId) {
    if (StringUtils.isNotBlank(carId)) {
      Optional<Orders> orderOptional = ordersService.findOrderWhichCarInParkingLotByCarId(carId);
      if (orderOptional.isPresent()) {
        return ResponseEntity.ok(orderOptional.get());
      }
    }
    log.error("【根据车牌查询车仍在停车场的订单】 carId错误或者该车已不在停车场, carId={}", carId);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }


  /**
   * 创建停车订单
   *
   * @param orders
   * @return
   */
  @PostMapping
  public List<Orders> addParkOrders(@RequestBody Orders orders) {
    if (ordersService.isExistInParkingLotCarId(orders.getCarId())) {
      return null;
    } else {
      return ordersService.addOrders(orders);
    }
  }

  //根据车牌carid查询该车牌的所有订单
  @GetMapping("/carIds")
  public List<Orders> findByCarIds(String carId) {
    return ordersService.findByCarIds(carId);
  }

  //根据状态STATUS查询订单
  @GetMapping("/status")
  public List<Orders> findByStatus(String status) {
    return ordersService.findByStatus(status);
  }

  //根据类型type查询订单
  @GetMapping("/type")

  public List<Orders> findByType(String type) {
    return ordersService.findByType(type);
  }

  //根据条件查询
  @GetMapping("condition")
  public List<Orders> findByCondition(String condition, String value) {
    return ordersService.findByCondition(condition, value);
  }


  //停车：指定停车员给订单
  @PutMapping("/{OrderId}/parkingBoy/{BoyId}")
  public OrdersDTO setUsersToOrders(@PathVariable int OrderId, @PathVariable Long BoyId) {
    User boy = userRepository.findById(BoyId).get();
    List<ParkingLot> parkingLots = boy.getParkingLotList();
    Orders orders = ordersService.findById(OrderId).get();
    orders.setUser(boy);
    if (parkingLots.stream().filter(x -> x.getSize() != 0).collect(Collectors.toList()).size() != 0) {
      ordersService.updateUserIdById(OrderId, BoyId);
      ordersService.updateStatusById(OrderId, OrderStatusEnum.YES.getMessage());
    }
    return Order2OrderDTOConverter.convert(orders);
  }

  //停车：指定停车场给订单
  @PutMapping("/{OrderId}/parkingLot/{ParkingLotId}")
  public OrdersDTO setParkingLotToOrders(@PathVariable int OrderId, @PathVariable Long ParkingLotId) {
    ordersService.updateParkingLotIdById(OrderId, ParkingLotId);
    int size = parkingLotRepository.findById(ParkingLotId).get().getSize() - 1;
    parkingLotRepository.updateSizeById(ParkingLotId, size);
    ordersService.updateStatusById(OrderId, OrderStatusEnum.PARK_DONE.getMessage());
    return Order2OrderDTOConverter.convert(ordersService.findById(OrderId).get());
  }

  //用户取车，订单变为取车
  @PostMapping("/userUnParkCarId")
  public OrdersDTO addUnParkOrders(String userUnParkCarId) {
//    if (ordersService.isExistInParkingLotCarId(userUnParkCarId)) {
//      Orders orders = ordersService.findOrderWhichCarInParkingLotByCarId(userUnParkCarId);
//      orders.setOrderType(OrderTypeEnum.UNPARK.getMessage());
//      orders.setOrderStatus(OrderStatusEnum.YES.getMessage());
//      ordersService.addOrders(orders);
//    }
    Optional<Orders> orderOptional = ordersService.findOrderWhichCarInParkingLotByCarId(userUnParkCarId);
    return Order2OrderDTOConverter.convert(orderOptional.orElse(null));
  }

  //停车员取车
  @PutMapping("/boyUnParkCarId")
  public OrdersDTO unPark(String boyUnParkCarId) {
//    Orders orders = ordersService.findByCarId(boyUnParkCarId);
//    ParkingLot parkingLot = parkingLotRepository.findById(ordersService.getParkingLotId(orders.getOrderId())).get();
//    Long parkingLotId = parkingLot.getId();
//    int size = parkingLotRepository.findById(parkingLotId).get().getSize() + 1;
//    parkingLotRepository.updateSizeById(parkingLotId, size);
//    ordersService.updateStatusById(orders.getOrderId(), OrderStatusEnum.UNPARK_DONE.getMessage());
//    orders.setOrderStatus(OrderStatusEnum.UNPARK_DONE.getMessage());
//    orders.setParkingLot(parkingLot);
    //TODO 下面这句可以删除
    Optional<Orders> orderOptional = ordersService.findOrderWhichCarInParkingLotByCarId(boyUnParkCarId);
    return Order2OrderDTOConverter.convert(orderOptional.orElse(null));
  }

  //根据订单ID查看停车场ID
//    @GetMapping("/id")
//    public Long findParkingLotIdByOrderId(int id) {
//        return ordersService.getParkingLotId(id);
//    }

  //根据停车员ID查看历史订单
  @GetMapping("/parkingBoy/{userId}")
  public List<Orders> getHistoryByUserId(@PathVariable Long userId) {
    return ordersService.getHistoryByUserId(userId);
  }

}
