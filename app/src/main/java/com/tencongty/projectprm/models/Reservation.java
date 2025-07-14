package com.tencongty.projectprm.models;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Reservation {
    @SerializedName("cancellationPolicy")
    private CancellationPolicy cancellationPolicy;

    @SerializedName("_id")
    private String id;

    private User user;
    @SerializedName("parkingLot")
    private ParkingLotOwner parkingLotOwner;

    private String startTime;
    private String timeCheckOut;
    private String licensePlate;
    private String status;
    private int totalPrice;
    private String bookingCode;
    private String createdAt;

    public String getId() {
        return id;
    }

    public CancellationPolicy getCancellationPolicy() {
        return cancellationPolicy;
    }

    public User getUser() {
        return user;
    }

    public ParkingLotOwner getParkingLotOwner() {
        return parkingLotOwner;
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

    public String getStatus() {
        return status;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
