package com.oocl.overwatcher.dto;

import java.util.List;

public class ChangeParkingLotDTO {
    private String direction;
    private List<Long> parkingLotId;
    private Long userId;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<Long> getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(List<Long> parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
