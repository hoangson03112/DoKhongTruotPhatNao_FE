package com.tencongty.projectprm.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class BookingRequest {
    private String parkingLotId;
    private String licensePlate;
    @SerializedName("startTime")
    private Date checkInDateTime;
    @SerializedName("timeCheckOut")
    private Date checkOutDateTime;

    // Constructors
    public BookingRequest() {}

    public BookingRequest(String parkingLotId, String licensePlate,
                          Date checkInDateTime, Date checkOutDateTime) {
        this.parkingLotId = parkingLotId;
        this.licensePlate = licensePlate;
        this.checkInDateTime = checkInDateTime;
        this.checkOutDateTime = checkOutDateTime;
    }

    // Getters and Setters
    public String getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Date getCheckInDateTime() {
        return checkInDateTime;
    }

    public void setCheckInDateTime(Date checkInDateTime) {
        this.checkInDateTime = checkInDateTime;
    }

    public Date getCheckOutDateTime() {
        return checkOutDateTime;
    }

    public void setCheckOutDateTime(Date checkOutDateTime) {
        this.checkOutDateTime = checkOutDateTime;
    }
}