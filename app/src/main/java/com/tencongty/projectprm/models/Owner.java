package com.tencongty.projectprm.models;

import java.io.Serializable;
import java.util.List;

public class Owner implements Serializable {
    private String _id;
    private String username;
    private String email;
    private String name;
    private String phone;
    private String role;
    private String verificationStatus;
    private boolean isDeleted;
    private String lastLogin;
    private List<String> ownerVerificationImages;


    public Owner(String _id, String username, String email, String name, String phone, String role, String verificationStatus, boolean isDeleted, String lastLogin, List<String> ownerVerificationImages) {
        this._id = _id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.verificationStatus = verificationStatus;
        this.isDeleted = isDeleted;
        this.lastLogin = lastLogin;
        this.ownerVerificationImages = ownerVerificationImages;
    }

    public List<String> getOwnerVerificationImages() {
        return ownerVerificationImages;
    }

    public void setOwnerVerificationImages(List<String> ownerVerificationImages) {
        this.ownerVerificationImages = ownerVerificationImages;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
}
