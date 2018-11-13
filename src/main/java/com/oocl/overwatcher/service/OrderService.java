package com.oocl.overwatcher.service;

import com.oocl.overwatcher.dto.OrderDTO;
import com.oocl.overwatcher.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 描述: 订单服务接口
 *
 * @author LIULE9
 * @create 2018-11-13 3:07 PM
 */
public interface OrderService {

  /**
   * 查找order
   *
   * @param orderId
   * @return
   */
  Optional<Order> findOrderByOrderId(Integer orderId);

  /**
   * 查找该car还在停车场的order
   *
   * @param carId
   * @return
   */
  Optional<Order> findOrderWhichCarInParkingLotByCarId(String carId);

  /**
   * 查找跟该car有关的所有order
   *
   * @param carId
   * @return
   */
  List<Order> findAllOrderWhichCarIdIs(String carId);

  /**
   * 查询所有抢单后的订单
   *
   * @param boyId
   * @return
   */
  List<Order> findAfterOrder(Long boyId);

  /**
   * 查询该停车员所有历史订单
   *
   * @param userId
   * @param status
   * @return
   */
  List<Order> showHistoryOrdersByUserId(Long userId, String status);

  /**
   * 分页查询所有订单
   *
   * @param pageRequest
   * @return
   */
  Page<Order> findAllOrdersByPage(PageRequest pageRequest);

  /**
   * 分页，条件查询所有订单
   *
   * @param condition
   * @param value
   * @param pageable
   * @return
   */
  Page<Order> findByCondition(String condition, String value, Pageable pageable);

  /**
   * 判断该car是否还在停车场
   *
   * @param carId
   * @return
   */
  boolean isExistInParkingLotCarId(String carId);

  /**
   * 创建一个停车订单，并且返回全部订单
   *
   * @param order
   * @return
   */
  List<Order> createParkOrders(Order order);

  /**
   * 指定一个无人处理的订单给parkingBoy
   *
   * @param orderId
   * @param boyId
   * @return
   */
  OrderDTO assignOrderToParkingBoy(Integer orderId, Long boyId);


  /**
   * 结束停车订单
   *
   * @param orderId
   * @param parkingLotId
   * @return
   */
  OrderDTO finishParkOrder(Integer orderId, Long parkingLotId);

  /**
   * 创建取车订单
   *
   * @param carId
   * @return
   */
  OrderDTO createUnParkOrders(String carId);

  /**
   * 结束取车订单
   *
   * @param carId
   * @return
   */
  OrderDTO finishUnParkOrder(String carId);


}