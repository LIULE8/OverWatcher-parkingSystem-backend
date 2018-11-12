package com.oocl.overwatcher.repositories;

import com.oocl.overwatcher.entities.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author LIULE9
 */
@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long>, JpaSpecificationExecutor<ParkingLot> {

}
