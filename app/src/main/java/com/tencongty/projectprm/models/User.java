package com.tencongty.projectprm.models;

import java.util.List;


import com.google.gson.annotations.SerializedName;

public class User {
    private String _id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String role;
    private boolean isDeleted;
    private String lastLogin;
    private String createdAt;
    private String updatedAt;
    private int __v;
    private List<String> ownerVerificationImages;
    private String phone;
    private String verificationStatus;

    public User(String _id, String username, String email, String password, String name, String role, boolean isDeleted, String lastLogin, String updatedAt, String createdAt, int __v, List<String> ownerVerificationImages, String phone, String verificationStatus) {
        this._id = _id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.isDeleted = isDeleted;
        this.lastLogin = lastLogin;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.__v = __v;
        this.ownerVerificationImages = ownerVerificationImages;
        this.phone = phone;
        this.verificationStatus = verificationStatus;
    }

    public String getId() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
}
