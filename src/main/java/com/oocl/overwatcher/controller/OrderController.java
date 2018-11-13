package com.oocl.overwatcher.controller;

import com.oocl.overwatcher.converter.Order2OrderDTOConverter;
import com.oocl.overwatcher.dto.OrderDTO;
import com.oocl.overwatcher.entities.Order;
import com.oocl.overwatcher.enums.OrderStatusEnum;
import com.oocl.overwatcher.service.impl.OrderServiceImpl;
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
@RequestMapping("orders")
@Slf4j
public class OrderController {

  private final OrderServiceImpl orderServiceImpl;


  @Autowired
  public OrderController(OrderServiceImpl orderServiceImpl) {
    this.orderServiceImpl = orderServiceImpl;
  }

  /**
   * 查询所有订单,分页查询
   *
   * @param pageSize
   * @param curPage
   * @return
   */
  @GetMapping
  public ResponseEntity<List<Order>> getOrdersByPage(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                     @RequestParam(value = "curPage", defaultValue = "1") Integer curPage) {
    PageRequest pageRequest = PageRequest.of(curPage, pageSize);
    return ResponseEntity.ok(orderServiceImpl.findAllOrdersByPage(pageRequest).getContent());
  }

  /**
   * 根据ID查询订单
   *
   * @param orderId
   * @return
   */
  @GetMapping("{orderId}")
  public ResponseEntity<Order> getOrdersByOrderId(@PathVariable("orderId") Integer orderId) {
    Optional<Order> orderOptional = orderServiceImpl.findOrderByOrderId(orderId);
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
  public ResponseEntity<List<Order>> findAllOrderWhichCarIdIs(@RequestParam("carId") String carId) {
    if (StringUtils.isNotBlank(carId)) {
      return ResponseEntity.ok(orderServiceImpl.findAllOrderWhichCarIdIs(carId));
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
  public ResponseEntity<Order> findOrderWhichCarInParkingLotByCarId(@PathVariable("carId") String carId) {
    if (StringUtils.isNotBlank(carId)) {
      Optional<Order> orderOptional = orderServiceImpl.findOrderWhichCarInParkingLotByCarId(carId);
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
  public ResponseEntity<List<Order>> findAllOrdersByConditionAndPage(@RequestParam("condition") String condition,
                                                                     @RequestParam("value") String value,
                                                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                     @RequestParam(value = "curPage", defaultValue = "1") Integer curPage) {
    if (StringUtils.isNotBlank(condition) && StringUtils.isNotBlank(value)) {
      return ResponseEntity.ok(orderServiceImpl.findByCondition(condition, value, PageRequest.of(curPage, pageSize)).getContent());
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
  public ResponseEntity<List<OrderDTO>> findAfterOrder(@PathVariable("boyId") Long boyId) {
    return ResponseEntity.ok(Order2OrderDTOConverter.convert(orderServiceImpl.findAfterOrder(boyId)));
  }


  /**
   * 创建停车订单
   *
   * @param order
   * @return
   */
  @PostMapping
  public ResponseEntity<List<Order>> createParkOrder(@RequestBody Order order) {
    if (StringUtils.isNotBlank(order.getCarId()) && !orderServiceImpl.isExistInParkingLotCarId(order.getCarId())) {
      return ResponseEntity.ok(orderServiceImpl.createParkOrders(order));
    }
    log.error("【创建停车订单】 carId错误或者car已经存在停车场 , order={}", order);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

  /**
   * 管理员分配某个订单给指定的停车员
   *
   * @param orderId
   * @param boyId
   * @return
   */
  @PutMapping("/{orderId}/parkingBoy/{boyId}")
  public ResponseEntity<OrderDTO> assignOrderToParkingBoy(@PathVariable("orderId") Integer orderId,
                                                          @PathVariable("boyId") Long boyId) {
    try {
      return ResponseEntity.ok(orderServiceImpl.assignOrderToParkingBoy(orderId, boyId));
    } catch (Exception e) {
      log.error("【管理员分配某个订单给指定的停车员】 "
          .concat(e.getMessage())
          .concat(", orderId={}, boyId={}"), orderId, boyId);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

  /**
   * 停车员结束订单（抢单，停车完成）
   * 手机端
   *
   * @param orderId
   * @param parkingLotId
   * @return
   */
  @PutMapping("/{orderId}/parkingLot/{parkingLotId}")
  public ResponseEntity<OrderDTO> finishParkOrder(@PathVariable("orderId") Integer orderId,
                                                  @PathVariable("parkingLotId") Long parkingLotId) {
    try {
      return ResponseEntity.ok(orderServiceImpl.finishParkOrder(orderId, parkingLotId));
    } catch (Exception e) {
      log.error("【停车员结束订单（抢单，停车完成）】 "
          .concat(e.getMessage())
          .concat(", orderId={}, parkingLotId={}"), orderId, parkingLotId);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

  /**
   * 用户取车，创建取车订单,停车员尚未取车
   *
   * @param carId
   * @return
   */
  @PostMapping("/createUnParkOrder/{carId}")
  public ResponseEntity<OrderDTO> createUnParkOrder(@PathVariable("carId") String carId) {
    try {
      return ResponseEntity.status(HttpStatus.CREATED).body(orderServiceImpl.createUnParkOrders(carId));
    } catch (Exception e) {
      log.error("【用户取车，创建取车订单,停车员尚未取车】 "
          .concat(e.getMessage())
          .concat(", carId={}"), carId);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

  /**
   * 停车员取车, 结束取车订单
   * 手机端
   *
   * @param carId
   * @return
   */
  @PutMapping("/finishUnParkOrder/{carId}")
  public ResponseEntity<OrderDTO> finishUnParkOrder(@PathVariable("carId") String carId) {
    try {
      return ResponseEntity.ok(orderServiceImpl.finishUnParkOrder(carId));
    } catch (Exception e) {
      log.error("【停车员取车, 结束取车订单】 "
          .concat(e.getMessage())
          .concat(", carId={}"), carId);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

  /**
   * 根据停车员ID查看历史订单
   *
   * @param userId
   * @return
   */
  @GetMapping("/showHistoryOrders/{userId}")
  public ResponseEntity<List<Order>> showHistoryOrders(@PathVariable("userId") Long userId) {
    return ResponseEntity.ok(orderServiceImpl.showHistoryOrdersByUserId(userId, OrderStatusEnum.UNPARK_DONE.getMessage()));
  }

}
