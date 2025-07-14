package com.tencongty.projectprm.models;

import java.util.List;

public class ParkingLotOwner {
    private String _id;
    private String name;
    private String address;
    private Coordinates coordinates;
    private int capacity;
    private int availableSlots;
    private List<Pricing> pricing;
    private String owner;
    private List<String> images;
    private String parkingType;
    private String verificationStatus;
    private String status;
    private boolean isDeleted;
    private String createdAt;
    private int __v;

    public ParkingLotOwner(String _id, String name, String address, Coordinates coordinates, int capacity, int availableSlots, List<Pricing> pricing, String owner, List<String> images, String parkingType, String verificationStatus, String status, boolean isDeleted, String createdAt, int __v) {
        this._id = _id;
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.capacity = capacity;
        this.availableSlots = availableSlots;
        this.pricing = pricing;
        this.owner = owner;
        this.images = images;
        this.parkingType = parkingType;
        this.verificationStatus = verificationStatus;
        this.status = status;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.__v = __v;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public List<Pricing> getPricing() {
        return pricing;
    }

    public void setPricing(List<Pricing> pricing) {
        this.pricing = pricing;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }
}

