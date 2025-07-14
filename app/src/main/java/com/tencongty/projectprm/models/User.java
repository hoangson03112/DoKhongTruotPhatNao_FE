package com.tencongty.projectprm.models;


import com.google.gson.annotations.SerializedName;

public class User {
    private String _id;
    private String username;
    private String email;
    private String name;
    private String phone;
    private String role;
    @SerializedName("isDeleted")
    private boolean isDeleted;

    private String verificationStatus;
    private String lastLogin;


    public User(String _id, String username, String email, String name, String phone, String role, boolean isDeleted, String verificationStatus, String lastLogin) {
        this._id = _id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.isDeleted = isDeleted;
        this.verificationStatus = verificationStatus;
        this.lastLogin = lastLogin;
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


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }


}
