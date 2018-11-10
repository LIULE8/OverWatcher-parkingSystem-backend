package com.oocl.overwatcher.service;

import com.oocl.overwatcher.entities.ParkingLot;
import com.oocl.overwatcher.repositories.ParkingLotRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author LIULE9
 */
@Service
@Transactional
public class ParkingLotService {

    @Autowired
    private ParkingLotRepository parkingLotRepository;


    public List<ParkingLot> findAll() {
        return parkingLotRepository.findAll();
    }


    public void save(ParkingLot parkingLot) {
        parkingLotRepository.save(parkingLot);
    }


    public void updateStatus(ParkingLot parkingLot) {
        parkingLotRepository.updateStatusById(parkingLot.getId(), parkingLot.getStatus());
    }

    public Optional<ParkingLot> findOne(Long id) {
        return parkingLotRepository.findById(id);
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
}
