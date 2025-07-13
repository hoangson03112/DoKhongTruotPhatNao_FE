package com.tencongty.projectprm.models;

import com.google.gson.annotations.SerializedName;

public class Reservation {

    @SerializedName("_id")
    private String id;

    @SerializedName("parkingLocation")
    private String parkingLocation;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("timeCheckOut")
    private String timeCheckOut;

    @SerializedName("licensePlate")
    private String licensePlate;

    @SerializedName("bookingCode")
    private String bookingCode;

    @SerializedName("status")
    private String status;

    @SerializedName("totalPrice")
    private int totalPrice;

    // Getter & Setter

    public String getId() {
        return id;
    }

    public String getParkingLocation() {
        return parkingLocation;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getTimeCheckOut() {
        return timeCheckOut;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setParkingLocation(String parkingLocation) {
        this.parkingLocation = parkingLocation;
    }
}
