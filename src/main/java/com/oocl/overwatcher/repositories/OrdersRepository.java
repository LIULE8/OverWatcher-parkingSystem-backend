package com.oocl.overwatcher.repositories;

import com.oocl.overwatcher.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author LIULE9
 */

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Integer>, JpaSpecificationExecutor<Orders> {

//    @Query(value = "select * from orders where car_id = ?1 and status<>'取车完成'", nativeQuery = true)
    Optional<Orders> findByCarIdAndOrderStatusNot(String carId, String orderStatus);

    @Query(value = "update orders set `status` = ?2 where id = ?1", nativeQuery = true)
    @Modifying
    void updateStatusById( int id, String status);

    @Query(value = "update orders set `user_id` = ?2 where id = ?1", nativeQuery = true)
    @Modifying
    void updateUserIdById( int id, Long user_id);

    @Query(value = "update orders set `parkinglot_id` = ?2 where id = ?1", nativeQuery = true)
    @Modifying
    void updateParkingLotIdById(int orderId, Long parkinglot_id);

    @Query(value = "select `parkinglot_id` from orders where id = ?1", nativeQuery = true)
    Long findParkinglotIdById(int id);

    @Query(value = "select * from orders where status = ?1 and type = '存车'", nativeQuery = true)
    List<Orders> findByStaus(String status);

    @Query(value = "select * from orders where type = ?1", nativeQuery = true)
    List<Orders> findByType(String type);

    @Query(value = "select * from orders where car_id = ?1", nativeQuery = true)
    List<Orders> findByCarIds(String carId);

    @Query(value = "select * from orders where `user_id` = ?1 and status='存取中'", nativeQuery = true)
    List<Orders> findAfterOrder(int boyId);

    @Query(value = "select * from orders where `user_id` = ?1 and status='取车完成'", nativeQuery = true)
    List<Orders> getHistoryByUserId(Long userId);
}
