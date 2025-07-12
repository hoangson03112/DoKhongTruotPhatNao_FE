package com.tencongty.projectprm.models;

import java.io.Serializable;

public class ParkingPricing implements Serializable {
    private String type;
    private double price;
    private String unit;

    public ParkingPricing(String type, double price, String unit) {
        this.type = type;
        this.price = price;
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }
}