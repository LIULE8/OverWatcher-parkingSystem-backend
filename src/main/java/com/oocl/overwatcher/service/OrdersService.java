package com.oocl.overwatcher.service;

import com.oocl.overwatcher.entities.Orders;
import com.oocl.overwatcher.repositories.OrdersRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

  public List<Orders> getOrders() {
    return ordersRepository.findAll();
  }

  @Transactional
  public List<Orders> addOrders(Orders orders) {
    ordersRepository.save(orders);
    return ordersRepository.findAll();
  }

  public Optional<Orders> findById(int id) {
    return ordersRepository.findById(id);
  }

  public Optional<Orders> findOrderWhichCarInParkingLotByCarId(String carId) {
    return ordersRepository.findByCarIdAndOrderStatusNot(carId,"取车成功");
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

  public boolean isExistInParkingLotCarId(String carId) {
    Optional<Orders> orderOptional = findOrderWhichCarInParkingLotByCarId(carId);
    return orderOptional.isPresent();
  }

  public Long getParkingLotId(int id) {
    return ordersRepository.findParkinglotIdById(id);
  }

  public List<Orders> findByStatus(String status) {
    return ordersRepository.findByStaus(status);
  }

  public List<Orders> findByType(String type) {
    return ordersRepository.findByType(type);
  }

  public List<Orders> findByCarIds(String carId) {
    return ordersRepository.findByCarIds(carId);
  }

  public List<Orders> findAfterOrder(int boyId) {
    return ordersRepository.findAfterOrder(boyId);
  }

  public List<Orders> findByCondition(String condition, String value) {
    return ordersRepository.findAll((root, query, criteriaBuilder) -> {
      Predicate predicate = null;
      if (StringUtils.isNotBlank(condition) && "type".equals(condition)) {
        predicate = criteriaBuilder.equal(root.get("type").as(String.class), value);
      } else if (StringUtils.isNotBlank(condition) && "status".equals(condition)) {
        predicate = criteriaBuilder.equal(root.get("status").as(String.class), value);
      }
      return predicate;
    });
  }

  public List<Orders> getHistoryByUserId(Long userId) {
    return ordersRepository.getHistoryByUserId(userId);
  }

  public Page<Orders> getOrders(PageRequest pageRequest) {
    return ordersRepository.findAll(pageRequest);
  }
}
