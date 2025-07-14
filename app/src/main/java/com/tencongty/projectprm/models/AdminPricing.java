    package com.tencongty.projectprm.models;

    import java.io.Serializable;

    public class AdminPricing implements Serializable {
        private String _id;
        private String type;
        private int price;

        public AdminPricing(String type, int price, String _id) {
            this.type = type;
            this.price = price;
            this._id = _id;
        }

        public String getId() {
            return _id;
        }

        public String getType() {
            return type;
        }

        public int getPrice() {
            return price;
        }
    }