package com.oocl.overwatcher.converter;

import com.oocl.overwatcher.dto.OrdersDTO;
import com.oocl.overwatcher.entities.Orders;
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

  public static OrdersDTO convert(Orders order) {
    OrdersDTO ordersDTO = new OrdersDTO();

    BeanUtils.copyProperties(order, ordersDTO);

    ordersDTO.setTime(order.getCreatedDate());
    ordersDTO.setUsersId(order.getUser().getId());
    String name = order.getParkingLot() != null ? order.getParkingLot().getName() : null;
    ordersDTO.setName(name);

    return ordersDTO;
  }


  public static List<OrdersDTO> convert(List<Orders> ordersList){
    return  ordersList.stream().map(Order2OrderDTOConverter::convert).collect(Collectors.toList());
  }

}