package com.oocl.overwatcher.converter;

import com.oocl.overwatcher.dto.OrderDTO;
import com.oocl.overwatcher.entities.Order;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述: 订单对象转换成DTO
 *
 * @author LIULE9
 * @create 2018-11-10 9:54 AM
 */
public class Order2OrderDTOConverter {

  public static OrderDTO convert(Order order) {
    OrderDTO orderDTO = new OrderDTO();
    BeanUtils.copyProperties(order, orderDTO);
    orderDTO.setTime(order.getCreatedDate());
    orderDTO.setUsersId(order.getUser().getId());
    String name = order.getParkingLot() != null ? order.getParkingLot().getParkingLotName() : null;
    orderDTO.setName(name);
    return orderDTO;
  }


  public static List<OrderDTO> convert(List<Order> orderList){
    return  orderList.stream().map(Order2OrderDTOConverter::convert).collect(Collectors.toList());
  }

}