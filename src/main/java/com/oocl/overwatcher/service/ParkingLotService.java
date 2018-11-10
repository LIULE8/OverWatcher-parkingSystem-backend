package com.oocl.overwatcher.service;

import com.oocl.overwatcher.entities.ParkingLot;
import com.oocl.overwatcher.repositories.ParkingLotRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
public class ParkingLotService {

  private final ParkingLotRepository parkingLotRepository;

  @Autowired
  public ParkingLotService(ParkingLotRepository parkingLotRepository) {
    this.parkingLotRepository = parkingLotRepository;
  }


  public Page<ParkingLot> getAllParkingLotByPage(Pageable pageable) {
    return parkingLotRepository.findAll(pageable);
  }


  public Optional<ParkingLot> findOne(Long id) {
    return parkingLotRepository.findById(id);
  }


  @Transactional
  public void save(ParkingLot parkingLot) {
    parkingLotRepository.save(parkingLot);
  }

  @Transactional
  public void updateStatus(ParkingLot parkingLot) {
    Optional<ParkingLot> parkingLotOptional = parkingLotRepository.findById(parkingLot.getParkingLotId());
    if (parkingLotOptional.isPresent()){
      ParkingLot dbParkingLot = parkingLotOptional.get();
      dbParkingLot.setStatus(parkingLot.getStatus());
    }
    throw new RuntimeException("停车场id错误");
  }


  public List<ParkingLot> finAllParkingLotNoOwner() {
    return parkingLotRepository.findAll().stream().filter(parkingLot -> parkingLot.getUser() == null).collect(Collectors.toList());
  }

  public List<ParkingLot> findByCondition(String condition, String value) {
    return parkingLotRepository.findAll((Specification<ParkingLot>) (root, query, criteriaBuilder) -> {
      Predicate predicate = null;
      if (StringUtils.isNotBlank(condition) && "name".equals(condition)) {
        predicate = criteriaBuilder.like(root.get("name").as(String.class), "%" + value + "%");
      } else if (StringUtils.isNotBlank(condition) && "size-less".equals(condition)) {
        try {
          predicate = criteriaBuilder.lessThanOrEqualTo(root.get("size").as(Integer.class), Integer.parseInt(value));
        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
      } else if (StringUtils.isNotBlank(condition) && "size-more".equals(condition)) {
        try {
          predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("size").as(Integer.class), Integer.parseInt(value));
        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
      } else if (StringUtils.isNotBlank(condition) && "status".equals(condition)) {
        predicate = criteriaBuilder.equal(root.get("status").as(String.class), value);
      }
      return predicate;
    });
  }

  @Transactional
  public void updateParkingLog(ParkingLot parkingLot) {
    if (parkingLot.getParkingLotId() != null) {
      Optional<ParkingLot> parkingLotOptional = parkingLotRepository.findById(parkingLot.getParkingLotId());
      if (parkingLotOptional.isPresent()) {
        ParkingLot dbParkingLot = parkingLotOptional.get();
        BeanUtils.copyProperties(parkingLot, dbParkingLot);
      }
    }
    throw new RuntimeException("参数错误");
  }
}
