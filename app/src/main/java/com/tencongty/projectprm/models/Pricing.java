package com.tencongty.projectprm.models;

public class Pricing {
    private String _id;
    private String type;
    private int price;

    public Pricing(String _id, String type, int price) {
        this._id = _id;
        this.type = type;
        this.price = price;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
