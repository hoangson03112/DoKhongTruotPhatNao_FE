package com.tencongty.projectprm.models;

public class ParkingOwnerActionRequest {
    private String action;

    public ParkingOwnerActionRequest(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
