package com.oocl.overwatcher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LIULE9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLotDetail {

    /**
     * 停车场名字
     */
    private String parkingLotName;

    /**
     * 停车员名字
     */
    private String parkingBoyName;


    /**
     * 停车场现容量
     */
    private Integer size;

    /**
     * 停车场初始容量
     */
    private Integer initSize;
}
