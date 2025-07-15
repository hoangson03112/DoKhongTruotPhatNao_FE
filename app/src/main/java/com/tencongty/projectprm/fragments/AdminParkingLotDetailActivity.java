package com.tencongty.projectprm.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.AdminParkingLot;
import com.tencongty.projectprm.models.Owner;

public class AdminParkingLotDetailActivity extends AppCompatActivity {

    private AdminParkingLot lot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_admin_parking_lot_detail_activity);

        lot = (AdminParkingLot) getIntent().getSerializableExtra("parkingLot");

        // Ánh xạ view
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvAddress = findViewById(R.id.tvDetailAddress);
        TextView tvPhone = findViewById(R.id.tvDetailPhone);
        TextView tvStatus = findViewById(R.id.tvDetailStatus);
        ImageView imageView = findViewById(R.id.ivDetailImage);
        Button btnMap = findViewById(R.id.btnOpenMap);
        ImageButton btnBack = findViewById(R.id.btnBack);

        LinearLayout layoutAllImages = findViewById(R.id.layoutAllImages);

        TextView tvOwnerName = findViewById(R.id.tvOwnerName);
        TextView tvOwnerUsername = findViewById(R.id.tvOwnerUsername);
        TextView tvOwnerEmail = findViewById(R.id.tvOwnerEmail);
        TextView tvOwnerPhone = findViewById(R.id.tvOwnerPhone);
        TextView tvOwnerStatus = findViewById(R.id.tvOwnerStatus);
        ImageView ivOwnerVerification = findViewById(R.id.ivOwnerVerification);

        // Gán dữ liệu
        tvName.setText(lot.getName());
        tvAddress.setText(lot.getAddress());
        tvPhone.setText("📞 " + (lot.getOwner() != null ? lot.getOwner().getPhone() : "Không có"));
        tvStatus.setText("Trạng thái: " + lot.getStatus());

        if (lot.getImages() != null && !lot.getImages().isEmpty()) {
            // Ảnh chính
            Glide.with(this)
                    .load(lot.getImages().get(0))
                    .into(imageView);

            // Các ảnh còn lại
            for (int i = 1; i < lot.getImages().size(); i++) {
                String imageUrl = lot.getImages().get(i);
                ImageView extraImage = new ImageView(this);
                extraImage.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 400));
                extraImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_parking)
                        .into(extraImage);

                layoutAllImages.addView(extraImage);
            }
        }

        btnMap.setOnClickListener(v -> {
            if (lot.getCoordinates() != null) {
                double lat = lot.getCoordinates().getLat();
                double lng = lot.getCoordinates().getLng();
                String uri = "geo:" + lat + "," + lng + "?q=" + lat + "," + lng + "(" + lot.getName() + ")";
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Không có tọa độ để mở bản đồ", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());

        if (lot.getOwner() != null) {
            Owner owner = lot.getOwner();
            tvOwnerName.setText("Họ tên: " + (owner.getName() != null ? owner.getName() : "Không có"));
            tvOwnerUsername.setText("Username: " + (owner.getUsername() != null ? owner.getUsername() : "Không có"));
            tvOwnerEmail.setText("Email: " + (owner.getEmail() != null ? owner.getEmail() : "Không có"));
            tvOwnerPhone.setText("Điện thoại: " + (owner.getPhone() != null ? owner.getPhone() : "Không có"));
            tvOwnerStatus.setText("Xác minh: " + (owner.getVerificationStatus() != null ? owner.getVerificationStatus() : "Không rõ"));

            if (owner.getOwnerVerificationImages() != null && !owner.getOwnerVerificationImages().isEmpty()) {
                ivOwnerVerification.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(owner.getOwnerVerificationImages().get(0))
                        .placeholder(R.drawable.placeholder_user)
                        .into(ivOwnerVerification);
            }
        }
    }
}
