package com.oocl.overwatcher.dto;

import lombok.Data;

/**
 * @author LIULE9
 */
@Data
public class ParkingLotDTO {

    private Long parkingLotId;

    private String parkingLotName;

    private Integer size;

    private Integer initSize;

    private String status;

    private Long userId;
}
