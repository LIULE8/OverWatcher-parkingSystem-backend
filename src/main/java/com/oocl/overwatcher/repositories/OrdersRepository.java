package com.oocl.overwatcher.repositories;

import com.oocl.overwatcher.entities.Order;
import com.oocl.overwatcher.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author LIULE9
 */

@Repository
public interface OrdersRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {

  /**
   * 根据车牌号查找订单，并且该订单状态不等于参数的订单状态
   * @param carId
   * @param orderStatus
   * @return
   */
  Optional<Order> findByCarIdAndOrderStatusNot(String carId, String orderStatus);

  /**
   * 根据车牌号查找订单
   * @param carId
   * @return
   */
  List<Order> findByCarId(String carId);


  /**
   * 根据停车员id和订单状态查找订单
   * @param user
   * @param status
   * @return
   */
  List<Order> findByUserAndOrderStatus(User user, String status);
}
