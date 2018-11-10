package com.oocl.overwatcher.repositories;

import com.oocl.overwatcher.entities.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long>, JpaSpecificationExecutor<ParkingLot> {

    @Query(value = "update Parking_Lot set `status` = ?2 where id = ?1", nativeQuery = true)
    @Modifying
    void updateStatusById( Long id, String status);

    @Query(value = "update Parking_Lot set `size` = ?2 where id = ?1", nativeQuery = true)
    @Modifying
    void updateSizeById( Long id, int size);
}
