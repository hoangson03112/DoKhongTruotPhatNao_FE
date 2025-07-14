package com.tencongty.projectprm.network;

import com.google.gson.JsonObject;
import com.tencongty.projectprm.models.AdminParkingLot;
import com.tencongty.projectprm.models.BookingRequest;
import com.tencongty.projectprm.models.LoginRequest;
import com.tencongty.projectprm.models.Owner;
import com.tencongty.projectprm.models.ParkingLot;
import com.tencongty.projectprm.models.RegisterRequest;
import com.tencongty.projectprm.models.User;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import retrofit2.http.PUT;

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



    //admin
    //admin user
    @GET("/api/admin/users")
    Call<List<User>> getAllUsers();

    @DELETE("/api/admin/users/{id}")
    Call<ResponseBody> deleteUser(@Path("id") String userId);

    @PUT("/api/admin/users/{id}")
    Call<ResponseBody> updateUserStatus(@Path("id") String userId, @Body Map<String, String> statusPayload);

    //admin owner
    @PUT("/api/admin/users/{id}")
    Call<ResponseBody> updateOwner(@Path("id") String ownerId, @Body Map<String, String> statusBody);

    @DELETE("/api/admin/users/{id}")
    Call<ResponseBody> deleteOwner(@Path("id") String ownerId);

    @GET("/api/admin/owners")
    Call<List<Owner>> getAllOwners();

    //adminPakinglot
    @GET("/api/admin/parking-lots")
    Call<List<AdminParkingLot>> getAllParkingLots();

    @PUT("/api/admin/parking-lots/{id}")
    Call<Void> updateParkingLotStatus(@Path("id") String id, @Body Map<String, String> body);

    @DELETE("/api/admin/parking-lots/{id}")
    Call<ResponseBody> deleteParkingLot(@Path("id") String id);


}
