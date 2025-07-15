package com.tencongty.projectprm.activities.parkingowner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.tencongty.projectprm.MainActivity;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.activities.common.LoginActivity;
import com.tencongty.projectprm.fragments.HomeFragment;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;
import com.tencongty.projectprm.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerProfileFragment extends Fragment {

    private TextView tvName, tvEmail;
    private ImageView imgAvatar;
    private LinearLayout btnMyProfile, btnSettings, btnNotifications, btnTransactionHistory, btnFAQ, btnAbout, btnLogout, btnHome;

    public OwnerProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ view
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
        btnHome = view.findViewById(R.id.btnParkingHome);
        btnHome.setVisibility(View.GONE);
        // Gọi API để lấy thông tin người dùng
        fetchUserInfo();

        // Sự kiện logout
        btnLogout.setOnClickListener(v -> {
            TokenManager tokenManager = new TokenManager(getContext());
            String token = tokenManager.getToken();

            if (token == null) {
                Toast.makeText(getContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
            Call<JsonObject> call = apiService.logout();

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful() && response.body() != null &&
                            response.body().has("success") && response.body().get("success").getAsBoolean()) {
                        tokenManager.clearToken();
                        Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        requireActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "Đăng xuất thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void fetchUserInfo() {
        TokenManager tokenManager = new TokenManager(getContext());
        String token = tokenManager.getToken();

        if (token == null) {
            Toast.makeText(getContext(), "Không có token, vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        Call<JsonObject> call = apiService.me();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();

                    if (responseBody.has("success") && responseBody.get("success").getAsBoolean()) {
                        JsonObject data = responseBody.getAsJsonObject("data");

                        if (data != null) {
                            String name = data.has("name") ? data.get("name").getAsString() : "Chưa có tên";
                            String email = data.has("email") ? data.get("email").getAsString() : "Chưa có email";
                            String role = data.has("role") ? data.get("role").getAsString() : "";

                            tvName.setText(name);
                            tvEmail.setText(email);

                            btnHome.setVisibility(View.VISIBLE);
                            btnHome.setOnClickListener(v -> {
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                            });

                        }
                    } else {
                        Toast.makeText(getContext(), "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi server khi lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
