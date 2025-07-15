package com.tencongty.projectprm.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.Owner;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

public class AdminOwnerDetailActivity extends AppCompatActivity {

    private TextView txtName, txtUsername, txtPhone, txtEmail, txtStatus;
    private ImageView img1, img2, img3, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_admin_owner_detail_activity);

        // Ánh xạ view
        txtName = findViewById(R.id.txt_owner_name);
        txtUsername = findViewById(R.id.txt_owner_username);
        txtPhone = findViewById(R.id.txt_owner_phone);
        txtEmail = findViewById(R.id.txt_owner_email);
        txtStatus = findViewById(R.id.txt_owner_status);
        img1 = findViewById(R.id.img_owner_1);
        img2 = findViewById(R.id.img_owner_2);
        img3 = findViewById(R.id.img_owner_3);
        btnBack = findViewById(R.id.btn_back); // Nút quay lại

        // Xử lý nút back
        btnBack.setOnClickListener(v -> finish());

        // Nhận dữ liệu owner
        Serializable extra = getIntent().getSerializableExtra("owner");
        if (extra instanceof Owner) {
            Owner owner = (Owner) extra;

            txtName.setText("Tên: " + owner.getName());
            txtUsername.setText("Username: " + owner.getUsername());
            txtPhone.setText("SĐT: " + owner.getPhone());
            txtEmail.setText("Email: " + owner.getEmail());
            txtStatus.setText("Trạng thái: " + getStatusText(owner.getVerificationStatus()));

            List<String> images = owner.getOwnerVerificationImages();
            ImageView[] imageViews = {img1, img2, img3};

            if (images != null) {
                for (int i = 0; i < images.size() && i < imageViews.length; i++) {
                    String imageUrl = images.get(i);
                    int index = i;

                    new Thread(() -> {
                        try {
                            InputStream input = new URL(imageUrl).openStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(input);
                            runOnUiThread(() -> imageViews[index].setImageBitmap(bitmap));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        }
    }

    private String getStatusText(String status) {
        if (status == null) return "Không xác định";
        switch (status) {
            case "pending":
                return "Chờ duyệt";
            case "verified":
                return "Đã duyệt";
            case "rejected":
                return "Từ chối";
            default:
                return status;
        }
    }
}
