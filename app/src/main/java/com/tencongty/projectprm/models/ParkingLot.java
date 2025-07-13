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
//=======
//import java.util.List;
//
//public class ParkingLot {
//    private String _id;
//    private String name;
//    private String address;
//    private int capacity;
//    private int availableSlots;
//    private List<Pricing> pricing;
//    private Coordinates coordinates;
//    private String parkingType;
//    private String verificationStatus;
//    private String status;
//    private boolean isDeleted;
//
//    public ParkingLot(String _id, String name, String address, int capacity, int availableSlots, List<Pricing> pricing, Coordinates coordinates, String parkingType, String verificationStatus, String status, boolean isDeleted) {
//        this._id = _id;
//        this.name = name;
//        this.address = address;
//        this.capacity = capacity;
//        this.availableSlots = availableSlots;
//        this.pricing = pricing;
//        this.coordinates = coordinates;
//        this.parkingType = parkingType;
//        this.verificationStatus = verificationStatus;
//        this.status = status;
//        this.isDeleted = isDeleted;
//    }
//
//    public String get_id() {
//        return _id;
//    }
//
//    public void set_id(String _id) {
//        this._id = _id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public int getCapacity() {
//        return capacity;
//    }
//
//    public void setCapacity(int capacity) {
//        this.capacity = capacity;
//    }
//
//    public int getAvailableSlots() {
//        return availableSlots;
//    }
//
//    public void setAvailableSlots(int availableSlots) {
//        this.availableSlots = availableSlots;
//    }
//
//    public List<Pricing> getPricing() {
//        return pricing;
//    }
//
//    public void setPricing(List<Pricing> pricing) {
//        this.pricing = pricing;
//    }
//
//    public Coordinates getCoordinates() {
//        return coordinates;
//    }
//
//    public void setCoordinates(Coordinates coordinates) {
//        this.coordinates = coordinates;
//    }
//
//    public String getParkingType() {
//        return parkingType;
//    }
//
//    public void setParkingType(String parkingType) {
//        this.parkingType = parkingType;
//    }
//
//    public String getVerificationStatus() {
//        return verificationStatus;
//    }
//
//    public void setVerificationStatus(String verificationStatus) {
//        this.verificationStatus = verificationStatus;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public boolean isDeleted() {
//        return isDeleted;
//    }
//
//    public void setDeleted(boolean deleted) {
//        isDeleted = deleted;
//    }
//}
//>>>>>>>
//Stashed changes
