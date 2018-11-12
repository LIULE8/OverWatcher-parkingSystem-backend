package com.oocl.overwatcher.controller;

import com.oocl.overwatcher.converter.ParkingLot2ParkingLotDTOConverter;
import com.oocl.overwatcher.converter.ParkingLot2ParkingLotDetail;
import com.oocl.overwatcher.dto.ParkingLotDTO;
import com.oocl.overwatcher.dto.ParkingLotDetail;
import com.oocl.overwatcher.entities.ParkingLot;
import com.oocl.overwatcher.service.ParkingLotService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author LIULE9
 */
@RestController
@RequestMapping("/parkingLots")
@Slf4j
public class ParkingLotController {

  private final ParkingLotService parkingLotService;

  @Autowired
  public ParkingLotController(ParkingLotService parkingLotService) {
    this.parkingLotService = parkingLotService;
  }

  /**
   * 分页查询所有停车场
   *
   * @param pageSize
   * @param curPage
   * @return
   */
  @GetMapping
  public ResponseEntity<List<ParkingLotDTO>> getAllParkingLotByPage(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(value = "curPage", defaultValue = "1") Integer curPage) {
    PageRequest pageRequest = PageRequest.of(curPage, pageSize);
    List<ParkingLot> parkingLotList = parkingLotService.getAllParkingLotByPage(pageRequest).getContent();
    List<ParkingLotDTO> parkingLotDTOList = ParkingLot2ParkingLotDTOConverter.convert(parkingLotList);
    return ResponseEntity.ok(parkingLotDTOList);
  }

  /**
   * 根据 parkingLotId 查询停车场信息
   *
   * @param parkingLotId
   * @return
   */
  @GetMapping("/{id}")
  public ResponseEntity<ParkingLot> findOne(@PathVariable("id") Long parkingLotId) {
    try {
      ParkingLot parkingLot = parkingLotService.findOne(parkingLotId).orElseThrow(() -> new Exception("没有该停车场"));
      return ResponseEntity.ok(parkingLot);
    } catch (Exception e) {
      log.error("【根据 parkingLotId 查询停车场信息】, 没有找到该停车场, parkingLotId={}", parkingLotId);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  /**
   * 统计所有停车场的具体信息
   *
   * @param pageSize
   * @param curPage
   * @return
   */
  @GetMapping("/statistical")
  public ResponseEntity<List<ParkingLotDetail>> countAllParkingLotDetail(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                         @RequestParam(value = "curPage", defaultValue = "1") Integer curPage) {
    PageRequest pageRequest = PageRequest.of(curPage, pageSize);

    List<ParkingLot> parkingLots = parkingLotService.getAllParkingLotByPage(pageRequest).getContent();

    List<ParkingLotDetail> collect = ParkingLot2ParkingLotDetail.convert(parkingLots);

    return ResponseEntity.ok(collect);
  }

  /**
   * 找到所有没有 owner 的停车场
   *
   * @return
   */
  @GetMapping("/nonOwner")
  public ResponseEntity<List<ParkingLot>> findAllParkingLotNoOwner() {
    return ResponseEntity.ok(parkingLotService.findAllParkingLotNoOwner());
  }

  /**
   * 条件查询
   *
   * @param condition
   * @param value
   * @return
   */
  @GetMapping("/criteria")
  public ResponseEntity<List<ParkingLotDTO>> findParkingByCondition(@RequestParam("condition") String condition,
                                                                    @RequestParam("value") String value,
                                                                    @RequestParam("pageSize") Integer pageSize,
                                                                    @RequestParam("curPage") Integer curPage) {
    PageRequest pageRequest = PageRequest.of(curPage, pageSize);

    List<ParkingLot> parkingLots = parkingLotService.findByCondition(condition, value, pageRequest).getContent();

    return ResponseEntity.ok(ParkingLot2ParkingLotDTOConverter.convert(parkingLots));
  }

  /**
   * 创建停车场
   *
   * @param parkingLot
   * @return
   */
  @PostMapping
  public ResponseEntity<Void> createParkingLot(@NotNull @RequestBody ParkingLot parkingLot) {
    try {
      parkingLotService.save(parkingLot);
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } catch (Exception e) {
      log.error("【创建停车场】 创建失败, parkingLot={}", parkingLot);
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  /**
   * 注销停车场
   *
   * @param parkingLot
   * @return
   */
  @PutMapping("/status")
  public ResponseEntity<Void> updateParkingLotStatus(@RequestBody ParkingLot parkingLot) {
    if (StringUtils.isNotBlank(parkingLot.getStatus()) && parkingLot.getParkingLotId() != null) {
      parkingLotService.updateStatus(parkingLot);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

//  指定某个停车场给另一个停车员assignParkingLotToAnotherParkingBoy

  /**
   * 修改停车场的信息
   *
   * @param parkingLot
   * @return
   */
  @PutMapping
  public ResponseEntity<Void> updateParkingLog(@NotNull @RequestBody ParkingLot parkingLot) {
    try {
      parkingLotService.updateParkingLog(parkingLot);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (Exception e) {
      log.error("【修改停车场的信息】"
          .concat(e.getMessage())
          .concat(", parkingLot={}"), parkingLot);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }
}
