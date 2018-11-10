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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author LIULE9
 */
@RestController
@RequestMapping("orders")
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
   * 查询所有订单,分页查询
   *
   * @param pageSize
   * @param curPage
   * @return
   */
  @GetMapping
  public ResponseEntity<List<Orders>> getOrdersByPage(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                      @RequestParam(value = "curPage", defaultValue = "1") Integer curPage) {
    PageRequest pageRequest = PageRequest.of(curPage, pageSize);
    return ResponseEntity.ok(ordersService.getOrders(pageRequest).getContent());
  }

  /**
   * 根据ID查询订单
   *
   * @param orderId
   * @return
   */
  @GetMapping("{orderId}")
  public ResponseEntity<Orders> getOrdersByOrderId(@PathVariable("orderId") Integer orderId) {
    Optional<Orders> orderOptional = ordersService.findOrderByOrderId(orderId);
    if (orderOptional.isPresent()) {
      return ResponseEntity.ok(orderOptional.get());
    }
    log.error("【查询订单详情】 订单id不正确, orderId={}", orderId);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }


  /**
   * 根据车牌carId查询该车牌的所有订单
   *
   * @param carId
   * @return
   */
  @GetMapping("carId")
  public ResponseEntity<List<Orders>> findAllOrderWhichCarIdIs(@RequestParam("carId") String carId) {
    if (StringUtils.isNotBlank(carId)) {
      return ResponseEntity.ok(ordersService.findAllOrderWhichCarIdIs(carId));
    }
    log.error("【根据车牌carId查询该车牌的所有订单】 carId 错误, carId={}", carId);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }


  /**
   * 根据车牌 carId 查询还在停车场的订单
   *
   * @param carId
   * @return
   */
  @GetMapping("carId/{carId}")
  public ResponseEntity<Orders> findOrderWhichCarInParkingLotByCarId(@PathVariable("carId") String carId) {
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
   * 根据条件分页查询所有订单
   *
   * @param condition
   * @param value
   * @param pageSize
   * @param curPage
   * @return
   */
  @GetMapping("criteria")
  public ResponseEntity<List<Orders>> findAllOrdersByConditionAndPage(@RequestParam("condition") String condition,
                                                                      @RequestParam("value") String value,
                                                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                      @RequestParam(value = "curPage", defaultValue = "1") Integer curPage) {
    if (StringUtils.isNotBlank(condition) && StringUtils.isNotBlank(value)) {
      return ResponseEntity.ok(ordersService.findByCondition(condition, value, PageRequest.of(curPage, pageSize)));
    }
    log.error("【条件查询】参数错误, condition={}, value={}", condition, value);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

  /**
   * 查询所有抢单后的订单
   *
   * @param boyId
   * @return
   */
  @GetMapping("after/{boyId}")
  public ResponseEntity<List<OrdersDTO>> findAfterOrder(@PathVariable("boyId") Integer boyId) {
    return ResponseEntity.ok(Order2OrderDTOConverter.convert(ordersService.findAfterOrder(boyId)));
  }


  /**
   * 创建停车订单
   *
   * @param orders
   * @return
   */
  @PostMapping
  public ResponseEntity<List<Orders>> addParkOrders(@RequestBody Orders orders) {
    if (StringUtils.isNotBlank(orders.getCarId()) && !ordersService.isExistInParkingLotCarId(orders.getCarId())) {
      return ResponseEntity.ok(ordersService.addOrders(orders));
    }
    log.error("【创建停车订单】 carId错误或者car已经存在停车场 , orders={}", orders);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

  /**
   * 停车：管理员分配某个订单给指定的停车员
   * @param orderId
   * @param boyId
   * @return
   */
  @PutMapping("/{orderId}/parkingBoy/{boyId}")
  public ResponseEntity<OrdersDTO> setUsersToOrders(@PathVariable("orderId") Integer orderId,
                                                    @PathVariable("boyId") Long boyId) {
    Optional<User> userOptional = userRepository.findById(boyId);
    if (userOptional.isPresent()){
      Optional<Orders> orderOptional = ordersService.findOrderByOrderId(orderId);
      if (orderOptional.isPresent()){
        User parkingBoy = userOptional.get();
        List<ParkingLot> parkingLots = parkingBoy.getParkingLotList();
        if (parkingLots.stream().filter(x -> x.getSize() != 0).collect(Collectors.toList()).size() != 0) {
          Orders orders = orderOptional.get();
          orders.setUser(parkingBoy);
          ordersService.updateUserIdById(orderId, boyId);
          ordersService.updateStatusById(orderId, OrderStatusEnum.YES.getMessage());
          return ResponseEntity.ok(Order2OrderDTOConverter.convert(orders));
        }
        log.error("【管理员分配某个订单给指定的停车员】 该停车员管理的停车场全部停满，分配失败, orderId={}, boyId={}",orderId,boyId);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
      }
    }

    log.error("【管理员分配某个订单给指定的停车员】 参数错误, orderId={}, boyId={}",orderId,boyId);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

  //停车：指定停车场给订单
  @PutMapping("/{OrderId}/parkingLot/{ParkingLotId}")
  public OrdersDTO setParkingLotToOrders(@PathVariable int orderId, @PathVariable Long parkingLotId) {
    ordersService.updateParkingLotIdById(orderId, parkingLotId);
    int size = parkingLotRepository.findById(parkingLotId).get().getSize() - 1;
    parkingLotRepository.updateSizeById(parkingLotId, size);
    ordersService.updateStatusById(orderId, OrderStatusEnum.PARK_DONE.getMessage());
    return Order2OrderDTOConverter.convert(ordersService.findOrderByOrderId(orderId).get());
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
//    ParkingLot parkingLot = parkingLotRepository.findOrderByOrderId(ordersService.getParkingLotId(orders.getOrderId())).get();
//    Long parkingLotId = parkingLot.getId();
//    int size = parkingLotRepository.findOrderByOrderId(parkingLotId).get().getSize() + 1;
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
