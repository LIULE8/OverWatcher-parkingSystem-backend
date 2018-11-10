package com.oocl.overwatcher.controller;

import com.oocl.overwatcher.converter.ParkingLot2ParkingLotDTOConverter;
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
import java.util.stream.Collectors;

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

  @GetMapping("/statistical")
  public ResponseEntity<List<ParkingLotDetail>> statisticalAllParkingLotDetail(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                               @RequestParam(value = "curPage", defaultValue = "1") Integer curPage) {
    PageRequest pageRequest = PageRequest.of(curPage, pageSize);
    List<ParkingLot> parkingLots = parkingLotService.getAllParkingLotByPage(pageRequest).getContent();
    List<ParkingLotDetail> collect = parkingLots.stream().map(parkingLot ->
        new ParkingLotDetail(parkingLot.getParkingLotName(),
            parkingLot.getUser() == null ? "暂无" : parkingLot.getUser().getName(),
            parkingLot.getSize(),
            parkingLot.getInitSize()))
        .collect(Collectors.toList());
    return ResponseEntity.ok(collect);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ParkingLot> findOne(@PathVariable("id") Long id) {
    try {
      ParkingLot parkingLot = parkingLotService.findOne(id).orElseThrow(() -> new Exception("没有该停车场"));
      return ResponseEntity.ok(parkingLot);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @GetMapping("/nonOwner")
  public List<ParkingLot> finAllParkingLotNoOwner() {
    return parkingLotService.finAllParkingLotNoOwner();
  }

  @GetMapping("/condition")
  public List<ParkingLotDTO> findParkingByCondition(String condition, String value) {
    List<ParkingLot> parkingLots = parkingLotService.findByCondition(condition, value);
    return ParkingLot2ParkingLotDTOConverter.convert(parkingLots);
  }

}
