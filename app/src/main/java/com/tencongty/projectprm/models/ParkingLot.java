package com.tencongty.projectprm.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class ParkingLot implements Serializable {

    @SerializedName("_id")
    private String id;
    private String name;
    private String address;
    private Coordinates coordinates;
    private int capacity;
    private int availableSlots;
    private String status;
    private String parkingType;
    private String verificationStatus;
    private List<String> features;
    private List<String> images;
    private Owner owner;
    private List<ParkingPricing> pricing;
    private String createdAt;

    // Getters và Setters cho các thuộc tính chính
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public Coordinates getCoordinates() { return coordinates; }
    public int getCapacity() { return capacity; }
    public int getAvailableSlots() { return availableSlots; }
    public String getStatus() { return status; }
    public String getParkingType() { return parkingType; }
    public String getVerificationStatus() { return verificationStatus; }
    public List<String> getFeatures() { return features; }
    public List<String> getImages() { return images; }
    public Owner getOwner() { return owner; }
    public List<ParkingPricing> getPricing() { return pricing; }
    public String getCreatedAt() { return createdAt; }

    // Phương thức trợ giúp để lấy Lat/Lng dễ dàng hơn
    public double getLatitude() {
        return coordinates != null ? coordinates.getLat() : 0;
    }

    public double getLongitude() {
        return coordinates != null ? coordinates.getLng() : 0;
    }

    // Lớp lồng nhau cho Coordinates
    public static class Coordinates implements Serializable {
        private double lat;
        private double lng;

        public double getLat() { return lat; }
        public double getLng() { return lng; }
    }

    // Lớp lồng nhau cho Owner
    public static class Owner implements Serializable {
        private String id;
        private String name;
        private String email;
        private String phoneNumber;

        public String getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhoneNumber() { return phoneNumber; }
    }
}