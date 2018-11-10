package com.oocl.overwatcher.service;

import com.oocl.overwatcher.entities.Orders;
import com.oocl.overwatcher.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author LIULE9
 */
@Service
public class OrdersService {

  private final OrdersRepository ordersRepository;

  @Autowired
  public OrdersService(OrdersRepository ordersRepository) {
    this.ordersRepository = ordersRepository;
  }

  public Optional<Orders> findOrderByOrderId(Integer orderId) {
    return ordersRepository.findById(orderId);
  }

  public Optional<Orders> findOrderWhichCarInParkingLotByCarId(String carId) {
    return ordersRepository.findByCarIdAndOrderStatusNot(carId, "取车成功");
  }

  public boolean isExistInParkingLotCarId(String carId) {
    Optional<Orders> orderOptional = findOrderWhichCarInParkingLotByCarId(carId);
    return orderOptional.isPresent();
  }

  public List<Orders> findAllOrderWhichCarIdIs(String carId) {
    return ordersRepository.findByCarId(carId);
  }

  public List<Orders> findAfterOrder(Integer boyId) {
    return ordersRepository.findAfterOrder(boyId);
  }

  public List<Orders> findByCondition(String condition, String value, Pageable pageable) {
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

  public List<Orders> getHistoryByUserId(Long userId) {
    return ordersRepository.getHistoryByUserId(userId);
  }

  public Page<Orders> getOrders(PageRequest pageRequest) {
    return ordersRepository.findAll(pageRequest);
  }

  @Transactional
  public List<Orders> addOrders(Orders orders) {
    ordersRepository.save(orders);
    return ordersRepository.findAll();
  }

  @Transactional
  public void updateUserIdById(int orderId, Long boyId) {
    ordersRepository.updateUserIdById(orderId, boyId);
  }

  @Transactional
  public void updateStatusById(int orderId, String status) {
    ordersRepository.updateStatusById(orderId, status);
  }

  @Transactional
  public void updateParkingLotIdById(int orderId, Long parkinglotId) {
    ordersRepository.updateParkingLotIdById(orderId, parkinglotId);
  }
}
