package com.tencongty.projectprm.models;

public class User {
    private String _id;
    private String username;
    private String email;
    private String name;
    private String phone;
    private String role;
    private boolean isDeleted;

    public User(boolean isDeleted, String role, String phone, String name, String email, String username, String _id) {
        this.isDeleted = isDeleted;
        this.role = role;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.username = username;
        this._id = _id;
    }

    public String getId() {
        return _id;
    }

    public void set_id(String _id) {
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

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
