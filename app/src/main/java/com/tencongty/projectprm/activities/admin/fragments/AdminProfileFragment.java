package com.tencongty.projectprm.activities.admin.fragments;

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
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.activities.common.LoginActivity;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;
import com.tencongty.projectprm.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProfileFragment extends Fragment {

    private TextView tvName, tvEmail;
    private ImageView imgAvatar;

    private LinearLayout btnMyProfile, btnSettings, btnNotifications,
            btnTransactionHistory, btnFAQ, btnAbout, btnLogout;

    public AdminProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_profile, container, false); // sử dụng layout đẹp của bạn
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

        fetchAdminInfo();

        // Đăng xuất
        btnLogout.setOnClickListener(v -> logout());

        // Các nút khác có thể xử lý sau tuỳ ý
        btnMyProfile.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chức năng Hồ sơ cá nhân", Toast.LENGTH_SHORT).show();
        });

        btnSettings.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Cài đặt", Toast.LENGTH_SHORT).show();
        });

        btnNotifications.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Thông báo", Toast.LENGTH_SHORT).show();
        });

        btnTransactionHistory.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Lịch sử giao dịch", Toast.LENGTH_SHORT).show();
        });

        btnFAQ.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Câu hỏi thường gặp", Toast.LENGTH_SHORT).show();
        });

        btnAbout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Về ứng dụng", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchAdminInfo() {
        TokenManager tokenManager = new TokenManager(getContext());
        String token = tokenManager.getToken();

        if (token == null) {
            Toast.makeText(getContext(), "Không có token, vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        Call<JsonObject> call = apiService.me();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null &&
                        response.body().has("data")) {
                    JsonObject data = response.body().getAsJsonObject("data");

                    String name = data.has("name") ? data.get("name").getAsString() : "Admin";
                    String email = data.has("email") ? data.get("email").getAsString() : "admin@example.com";

                    tvName.setText(name);
                    tvEmail.setText(email);
                } else {
                    Toast.makeText(getContext(), "Lỗi khi lấy thông tin admin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
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
    }
}
