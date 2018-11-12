package com.oocl.overwatcher.converter;

import com.oocl.overwatcher.dto.ParkingLotDetail;
import com.oocl.overwatcher.entities.ParkingLot;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述: parkingLot 转 parkingLotDetail
 *
 * @author LIULE9
 * @create 2018-11-12 9:02 AM
 */
public class ParkingLot2ParkingLotDetail {

  private static ParkingLotDetail convert(ParkingLot parkingLot) {
    ParkingLotDetail parkingLotDetail = new ParkingLotDetail();

    BeanUtils.copyProperties(parkingLot, parkingLotDetail);

    String parkingBoyName = parkingLot.getUser() != null ? parkingLot.getUser().getName() : "暂无";

    parkingLotDetail.setParkingBoyName(parkingBoyName);

    return parkingLotDetail;
  }


  public static List<ParkingLotDetail> convert(List<ParkingLot> parkingLotList){
    return parkingLotList.stream().map(ParkingLot2ParkingLotDetail::convert).collect(Collectors.toList());
  }
}