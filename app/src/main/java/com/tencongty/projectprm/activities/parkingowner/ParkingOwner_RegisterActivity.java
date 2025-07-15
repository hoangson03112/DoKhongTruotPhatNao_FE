package com.tencongty.projectprm.activities.parkingowner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.activities.common.LoginActivity;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;
import com.tencongty.projectprm.models.ParkingOwnerRegisterRequest;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingOwner_RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etFullName, etEmail, etPhone, etPassword;
    private EditText etOwnerIdFrontUrl, etOwnerIdBackUrl, etBusinessLicenseUrl;
    private CheckBox cbTerms;
    private MaterialButton btnRegister;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.packing_owner_activity_register);

        // Ánh xạ view
        etUsername = findViewById(R.id.etUsername);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etOwnerIdFrontUrl = findViewById(R.id.etOwnerIdFrontUrl);
        etOwnerIdBackUrl = findViewById(R.id.etOwnerIdBackUrl);
        etBusinessLicenseUrl = findViewById(R.id.etBusinessLicenseUrl);
        cbTerms = findViewById(R.id.cbTerms);
        btnRegister = findViewById(R.id.btnRegisterOwner);

        apiService = ApiClient.getClient(this).create(ApiService.class);

        btnRegister.setOnClickListener(v -> {
            if (!cbTerms.isChecked()) {
                Toast.makeText(this, "Bạn phải đồng ý với điều khoản", Toast.LENGTH_SHORT).show();
                return;
            }

            String username = etUsername.getText().toString().trim();
            String name = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String idFront = etOwnerIdFrontUrl.getText().toString().trim();
            String idBack = etOwnerIdBackUrl.getText().toString().trim();
            String license = etBusinessLicenseUrl.getText().toString().trim();

            if (username.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()
                    || idFront.isEmpty() || idBack.isEmpty() || license.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            ParkingOwnerRegisterRequest request = new ParkingOwnerRegisterRequest(
                    username,
                    email,
                    password,
                    name,
                    phone,
                    Arrays.asList(idFront, idBack, license)
            );

            registerOwner(request);
        });
    }

    private void registerOwner(ParkingOwnerRegisterRequest request) {
        apiService.registerParkingOwner(request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ParkingOwner_RegisterActivity.this, "Đăng ký thành công! Vui lòng chờ duyệt.", Toast.LENGTH_SHORT).show();
                    // Có thể chuyển sang màn hình đăng nhập
                    startActivity(new Intent(ParkingOwner_RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(ParkingOwner_RegisterActivity.this, "Đăng ký thất bại: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ParkingOwner_RegisterActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
