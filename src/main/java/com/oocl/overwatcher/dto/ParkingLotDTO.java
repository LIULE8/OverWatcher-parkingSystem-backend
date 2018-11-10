package com.oocl.overwatcher.dto;

import com.oocl.overwatcher.entities.ParkingLot;

public class ParkingLotDTO {
    private Long parkingLotId;
    private String parkingLotName;
    private int size;
    private int initSize;
    private String status;
    private Long userId;

    public String getParkingLotName() {
        return parkingLotName;
    }

    public int getSize() {
        return size;
    }

    public int getInitSize() {
        return initSize;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public String getStatus() {
        return status;
    }

    public ParkingLotDTO(ParkingLot parkingLot) {
        this.parkingLotId = parkingLot.getId();
        this.parkingLotName = parkingLot.getName();
        this.size = parkingLot.getSize();
        this.initSize = parkingLot.getInitSize();
        this.status = parkingLot.getStatus();
        if (parkingLot.getUser()!=null){
            this.userId = parkingLot.getUser().getId();
        }else {
            this.userId=0L;
        }
    }
}
