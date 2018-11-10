package com.oocl.overwatcher.converter;

import com.oocl.overwatcher.dto.ParkingLotDTO;
import com.oocl.overwatcher.entities.ParkingLot;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述: ParkingLot转换成ParkingLotDTO
 *
 * @author LIULE9
 * @create 2018-11-10 4:47 PM
 */
public class ParkingLot2ParkingLotDTOConverter {

  public static ParkingLotDTO convert(ParkingLot parkingLot) {

    ParkingLotDTO parkingLotDTO = new ParkingLotDTO();

    BeanUtils.copyProperties(parkingLot, parkingLotDTO);

    Long userId = parkingLot.getUser() != null? parkingLot.getUser().getId():0L;

    parkingLotDTO.setUserId(userId);

    return parkingLotDTO;
  }

  public static List<ParkingLotDTO> convert(List<ParkingLot> parkingLotList){
    return parkingLotList.stream().map(ParkingLot2ParkingLotDTOConverter::convert).collect(Collectors.toList());
  }
}