package com.tencongty.projectprm.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tencongty.projectprm.utils.TokenManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://do-khong-truot-phat-nao.onrender.com/";

    public static Retrofit getClient(Context context) {
        // 1. Khai báo gson có format ISO 8601 (Z là UTC)
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .create();

        // 2. Thêm interceptor để gắn token
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    String token = new TokenManager(context).getToken();
                    Request.Builder builder = original.newBuilder();
                    if (token != null) {
                        builder.header("Authorization", "Bearer " + token);
                    }
                    return chain.proceed(builder.build());
                })
                .build();

        // 3. Gắn gson vào Retrofit
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)) // dùng gson custom
                .client(okHttpClient)
                .build();
    }
}
