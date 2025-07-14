package com.tencongty.projectprm.models;

import java.util.List;

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
}
