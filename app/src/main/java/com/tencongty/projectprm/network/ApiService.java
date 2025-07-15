package com.tencongty.projectprm.network;

import com.google.gson.JsonObject;
import com.tencongty.projectprm.models.AddParkingLotRequest;
import com.tencongty.projectprm.models.BookingRequest;
import com.tencongty.projectprm.models.LoginRequest;
import com.tencongty.projectprm.models.ParkingLot;
import com.tencongty.projectprm.models.ParkingLotOwner;
import com.tencongty.projectprm.models.ParkingOwnerActionRequest;
import com.tencongty.projectprm.models.ParkingOwnerRegisterRequest;
import com.tencongty.projectprm.models.RegisterRequest;
import com.tencongty.projectprm.models.Reservation;
import com.tencongty.projectprm.models.ReservationResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/auth/login")
    Call<JsonObject> login(@Body LoginRequest loginRequest);

    @POST("/api/auth/register")
    Call<JsonObject> register(@Body RegisterRequest registerRequest);

    @POST("/api/auth/owner/register")
    Call<JsonObject> registerParkingOwner(@Body ParkingOwnerRegisterRequest request);
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

    @GET("/api/owner/parking-lots")
    Call<List<ParkingLotOwner>> getParkingLots();

    // Thêm bên trong interface ApiService:
    @GET("/api/owner/{id}/reservations")
    Call<ReservationResponse> getReservationsByOwner(
            @Header("Authorization") String authHeader,
            @Path("id") String ownerId
    );

    @POST("/api/owner/parking-lots")
    Call<JsonObject> addParkingLot(@Body AddParkingLotRequest request);

    @retrofit2.http.PATCH("/api/owner/parking-lots/{id}")
    Call<JsonObject> updateParkingBookingStatus(
            @Path("id") String parkingLotId,
            @Body ParkingOwnerActionRequest request
    );

}
