package com.oocl.overwatcher.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * @author LIULE9
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    private String orderType;

    private String orderStatus;

    private String carId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parkinglot_id")
    @JsonIgnore
    private ParkingLot parkingLot;

    @CreatedDate
    private ZonedDateTime createdDate = ZonedDateTime.now().plusHours(8);

}
