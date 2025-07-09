package com.tencongty.projectprm.models;

public class RegisterRequest {
    private  String username;
    private String email;
    private String name;
    private String phone;
    private String password;

    public RegisterRequest(String username, String email, String name, String phone, String password) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
    }
}
