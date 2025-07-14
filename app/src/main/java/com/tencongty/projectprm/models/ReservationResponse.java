package com.tencongty.projectprm.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ReservationResponse {
    private boolean success;
    private int count;
    private List<Reservation> data;

    public boolean isSuccess() {
        return success;
    }

    public int getCount() {
        return count;
    }

    public List<Reservation> getData() {
        return data;
    }
}
