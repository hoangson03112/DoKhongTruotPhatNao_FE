package com.tencongty.projectprm.network;

import com.google.gson.JsonObject;
import com.tencongty.projectprm.models.BookingRequest;
import com.tencongty.projectprm.models.LoginRequest;
import com.tencongty.projectprm.models.ParkingLot;
import com.tencongty.projectprm.models.RegisterRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/auth/login")
    Call<JsonObject> login(@Body LoginRequest loginRequest);

    @POST("/api/auth/register")
    Call<JsonObject> register(@Body RegisterRequest registerRequest);

    @GET("api/auth/logout")
    Call<JsonObject> logout();
  
    @GET("api/auth/me")
    Call<JsonObject>  me();
  
    @GET("/api/parking-lots")
    Call<List<ParkingLot>> getNearbyParkingLots(
            @Query("lat") double lat,
            @Query("lng") double lng
    );

    @GET("/api/parking-lots/{parkingLotId}")
    Call<ParkingLot> getParkingLotDetail(@Path("parkingLotId") String parkingLotId);

    @GET("api/bookings")
    Call<JsonObject> getBookings();

    @POST("api/bookings")
    Call<JsonObject> createBooking(@Body BookingRequest booking);
    @DELETE("api/bookings/{id}")
    Call<JsonObject> cancelBooking(@Path("id") String id);


}
