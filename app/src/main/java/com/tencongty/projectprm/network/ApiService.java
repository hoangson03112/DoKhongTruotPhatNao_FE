package com.tencongty.projectprm.network;

import com.google.gson.JsonObject;
import com.tencongty.projectprm.models.LoginRequest;
import com.tencongty.projectprm.models.RegisterRequest;
import com.tencongty.projectprm.models.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("/api/auth/login")
    Call<JsonObject> login(@Body LoginRequest loginRequest);

    @POST("/api/auth/register")
    Call<JsonObject> register(@Body RegisterRequest registerRequest);

    @GET("api/auth/logout")
    Call<JsonObject> logout();

    @GET("api/auth/me")
    Call<JsonObject>  me();

    //admin
    @GET("/api/admin/users")
    Call<List<User>> getAllUsers();

    @DELETE("/api/admin/users/{id}")
    Call<ResponseBody> deleteUser(@Path("id") String userId);

    @PUT("/api/admin/users/{id}")
    Call<ResponseBody> updateUser(@Path("id") String userId, @Body User user);
}
