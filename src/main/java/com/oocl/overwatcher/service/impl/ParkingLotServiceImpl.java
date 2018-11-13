package com.oocl.overwatcher.service.impl;

import com.oocl.overwatcher.entities.ParkingLot;
import com.oocl.overwatcher.repositories.ParkingLotRepository;
import com.oocl.overwatcher.service.ParkingLotService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author LIULE9
 */
@Service
public class ParkingLotServiceImpl implements ParkingLotService {

  private static final String CONDITION_NAME = "name";
  private static final String CONDITION_SIZE_LESS = "size_less";
  private static final String CONDITION_SIZE_MORE = "size_more";
  private static final String CONDITION_STATUS = "status";

  private final ParkingLotRepository parkingLotRepository;

  @Autowired
  public ParkingLotServiceImpl(ParkingLotRepository parkingLotRepository) {
    this.parkingLotRepository = parkingLotRepository;
  }

  @Override
  public Page<ParkingLot> findAllParkingLotByPage(Pageable pageable) {
    return parkingLotRepository.findAll(pageable);
  }

  @Override
  public Optional<ParkingLot> findOne(Long id) {
    return parkingLotRepository.findById(id);
  }

  @Override
  public List<ParkingLot> findAll() {
    return parkingLotRepository.findAll();
  }

  @Override
  public List<ParkingLot> findAllParkingLotNoOwner() {
    return parkingLotRepository.findAll().stream().filter(parkingLot -> parkingLot.getUser() == null).collect(Collectors.toList());
  }

  @Override
  public Page<ParkingLot> findByCondition(String condition, String value, Pageable pageable) {
    return parkingLotRepository.findAll((root, query, criteriaBuilder) -> {
      Predicate predicate = null;
      if (StringUtils.isBlank(condition)) {
        return predicate;
      }
      switch (condition) {
        case CONDITION_NAME:
          predicate = criteriaBuilder.like(root.get("name"), "%" + value + "%");
          break;
        case CONDITION_SIZE_LESS:
          predicate = criteriaBuilder.lessThanOrEqualTo(root.get("size"), Integer.parseInt(value));
          break;
        case CONDITION_SIZE_MORE:
          predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("size"), Integer.parseInt(value));
          break;
        case CONDITION_STATUS:
          predicate = criteriaBuilder.equal(root.get("status"), value);
          break;
        default:
          break;
      }
      return predicate;
    }, pageable);
  }

  @Override
  @Transactional
  public void save(ParkingLot parkingLot) {
    parkingLotRepository.save(parkingLot);
  }

  @Override
  @Transactional
  public void openOrCloseParkingLot(ParkingLot parkingLot) {
    ParkingLot dbParkingLot = findOne(parkingLot.getParkingLotId()).orElseThrow(() -> new RuntimeException("停车场id存在或者id错误"));
    dbParkingLot.setStatus(parkingLot.getStatus());
  }

  @Override
  @Transactional
  public void updateParkingLog(ParkingLot parkingLot) {
    ParkingLot dbParkingLot = findOne(parkingLot.getParkingLotId()).orElseThrow(() -> new RuntimeException("参数错误"));
    BeanUtils.copyProperties(parkingLot, dbParkingLot);
  }

}
