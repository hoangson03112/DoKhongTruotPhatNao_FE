package com.tencongty.projectprm.models;

import java.util.List;

public class ParkingOwnerRegisterRequest {
    private String username;
    private String email;
    private String password;
    private String name;
    private String phone;
    private List<String> ownerVerificationImages;

    public ParkingOwnerRegisterRequest(String username, String email, String password, String name, String phone, List<String> ownerVerificationImages) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.ownerVerificationImages = ownerVerificationImages;
    }

    // Getter và Setter (nếu bạn cần)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getOwnerVerificationImages() {
        return ownerVerificationImages;
    }

    public void setOwnerVerificationImages(List<String> ownerVerificationImages) {
        this.ownerVerificationImages = ownerVerificationImages;
    }
}
