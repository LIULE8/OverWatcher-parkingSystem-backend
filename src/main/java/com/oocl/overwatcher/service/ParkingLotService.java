package com.oocl.overwatcher.service;

import com.oocl.overwatcher.entities.ParkingLot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 描述: 停车场服务接口
 *
 * @author LIULE9
 * @create 2018-11-13 3:08 PM
 */
public interface ParkingLotService {

   /**
    * 查询一个停车场
    * @param id
    * @return
    */
   Optional<ParkingLot> findOne(Long id);

   /**
    * 查找所有停车场
    * @return
    */
   List<ParkingLot> findAll();

   /**
    * 查找所有没有owner的停车场
    * @return
    */
   List<ParkingLot> findAllParkingLotNoOwner();

   /**
    * 分页查找所有的停车场
    * @param pageable
    * @return
    */
   Page<ParkingLot> findAllParkingLotByPage(Pageable pageable);

   /**
    * 分页，条件查询停车场
    * @param condition
    * @param value
    * @param pageable
    * @return
    */
   Page<ParkingLot> findByCondition(String condition, String value, Pageable pageable);

   /**
    * 添加一个停车场
    * @param parkingLot
    */
   void save(ParkingLot parkingLot);

   /**
    * 开启或者关闭某个停车场
    * @param parkingLot
    */
   void openOrCloseParkingLot(ParkingLot parkingLot);

   /**
    * 修改停车场信息
    * @param parkingLot
    */
   void updateParkingLog(ParkingLot parkingLot);
}