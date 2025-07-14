package com.tencongty.projectprm.models;

import java.util.List;

public class AddParkingLotRequest {
    private String name;
    private String address;
    private Coordinates coordinates;
    private int capacity;
    private List<String> images;
    private List<String> pricing;

    public AddParkingLotRequest(String name, String address, Coordinates coordinates, int capacity, List<String> images, List<String> pricing) {
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.capacity = capacity;
        this.images = images;
        this.pricing = pricing;
    }

    public static class Coordinates {
        private double lat;
        private double lng;

        public Coordinates(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }
}
