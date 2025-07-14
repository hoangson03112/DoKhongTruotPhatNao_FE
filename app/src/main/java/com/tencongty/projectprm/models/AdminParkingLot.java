package com.tencongty.projectprm.models;

import java.io.Serializable;
import java.util.List;

public class AdminParkingLot implements Serializable {

    private String _id;
    private String name;
    private String address;
    private int capacity;
    private int availableSlots;
    private List<String> images;
    private String parkingType;
    private String verificationStatus;
    private boolean isDeleted;
    private String createdAt;
    private String status;
    private Owner owner;
    private AdminCoordinates coordinates;
    private List<AdminPricing> pricing; // ✅ dùng AdminPricing

    public String getId() { return _id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public int getCapacity() { return capacity; }
    public int getAvailableSlots() { return availableSlots; }
    public List<String> getImages() { return images; }
    public String getParkingType() { return parkingType; }
    public String getVerificationStatus() { return verificationStatus; }
    public boolean isDeleted() { return isDeleted; }
    public String getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
    public Owner getOwner() { return owner; }
    public AdminCoordinates getCoordinates() { return coordinates; }
    public List<AdminPricing> getPricing() { return pricing; }
    public void setStatus(String status) { this.status = status; }

    // Inner class Owner
    public static class Owner implements Serializable {
        private String _id;
        private String username;
        private String email;
        private String name;
        private String role;
        private boolean isDeleted;
        private String verificationStatus;

        public String getId() { return _id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getName() { return name; }
        public String getRole() { return role; }
        public boolean isDeleted() { return isDeleted; }
        public String getVerificationStatus() { return verificationStatus; }
    }
}
