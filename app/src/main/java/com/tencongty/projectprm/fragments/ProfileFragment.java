package com.tencongty.projectprm.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.activities.common.LoginActivity;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;
import com.tencongty.projectprm.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvEmail;
    private ImageView imgAvatar;
    private LinearLayout btnMyProfile, btnSettings, btnNotifications, btnTransactionHistory, btnFAQ, btnAbout, btnLogout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ các view
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        imgAvatar = view.findViewById(R.id.imgAvatar);

        btnMyProfile = view.findViewById(R.id.btnMyProfile);
        btnSettings = view.findViewById(R.id.btnSettings);
        btnNotifications = view.findViewById(R.id.btnNotifications);
        btnTransactionHistory = view.findViewById(R.id.btnTransactionHistory);
        btnFAQ = view.findViewById(R.id.btnFAQ);
        btnAbout = view.findViewById(R.id.btnAbout);
        btnLogout = view.findViewById(R.id.btnLogout);


        btnLogout.setOnClickListener(v -> {
            TokenManager tokenManager = new TokenManager(getContext());
            String token = tokenManager.getToken();

            if (token == null) {
                Toast.makeText(getContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
            Call<JsonObject> call = apiService.logout(); // Gửi token theo header

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        JsonObject res = response.body();
                        if (res.has("success") && res.get("success").getAsBoolean()) {
                            tokenManager.clearToken();
                            Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                            requireActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "Đăng xuất thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Lỗi server khi đăng xuất", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
