package com.tencongty.projectprm.fragments;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.User;

import java.io.Serializable;

public class AdminUserDetailActivity extends AppCompatActivity {

    private TextView txtName, txtUsername, txtEmail, txtPhone, txtRole, txtLastLogin, txtVerification;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_admin_user_detail_activity);

        txtName = findViewById(R.id.txt_user_name);
        txtUsername = findViewById(R.id.txt_user_username);
        txtEmail = findViewById(R.id.txt_user_email);
        txtPhone = findViewById(R.id.txt_user_phone);
        txtRole = findViewById(R.id.txt_user_role);
        txtLastLogin = findViewById(R.id.txt_user_last_login);
        txtVerification = findViewById(R.id.txt_user_verification);
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        Serializable data = getIntent().getSerializableExtra("user");
        if (data instanceof User) {
            User user = (User) data;
            txtName.setText("Tên: " + user.getName());
            txtUsername.setText("Username: @" + user.getUsername());
            txtEmail.setText("Email: " + user.getEmail());
            txtPhone.setText("SĐT: " + user.getPhone());
            txtRole.setText("Vai trò: " + user.getRole());
            txtLastLogin.setText("Lần đăng nhập: " + user.getLastLogin());

            if ("parking_owner".equals(user.getRole())) {
                txtVerification.setText("Xác thực: " + user.getVerificationStatus());
                txtVerification.setVisibility(TextView.VISIBLE);
            } else {
                txtVerification.setVisibility(TextView.GONE);
            }
        }
    }
}
