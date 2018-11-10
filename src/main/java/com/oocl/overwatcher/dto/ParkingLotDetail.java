package com.oocl.overwatcher.dto;

/**
 * @author LIULE9
 */
public class ParkingLotDetail {

    private String parkingLotName;
    private String parkingBoyName;
    private int size;
    private int initSize;
    private Long userId;

    public ParkingLotDetail(String parkingLotName, String parkingBoyName, int size, int initSize) {
        this.parkingLotName = parkingLotName;
        this.parkingBoyName = parkingBoyName;
        this.size = size;
        this.initSize = initSize;

    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getInitSize() {
        return initSize;
    }

    public void setInitSize(int initSize) {
        this.initSize = initSize;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public String getParkingBoyName() {
        return parkingBoyName;
    }

    public void setParkingBoyName(String parkingBoyName) {
        this.parkingBoyName = parkingBoyName;
    }
}
