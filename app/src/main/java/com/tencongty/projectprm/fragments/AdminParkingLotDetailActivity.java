package com.tencongty.projectprm.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.AdminParkingLot;

public class AdminParkingLotDetailActivity extends AppCompatActivity {

    private TextView tvName, tvAddress, tvPhone, tvStatus;
    private Button btnMap;
    private ImageView mainImageView;
    private LinearLayout imagesContainer;
    private AdminParkingLot lot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_admin_parking_lot_detail_activity);

        // Nhận dữ liệu
        lot = (AdminParkingLot) getIntent().getSerializableExtra("parkingLot");
        if (lot == null) {
            Toast.makeText(this, "Không có dữ liệu bãi đỗ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup Toolbar with Back button
        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Chi tiết bãi đỗ");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Ánh xạ View
        tvName = findViewById(R.id.tvDetailName);
        tvAddress = findViewById(R.id.tvDetailAddress);
        tvPhone = findViewById(R.id.tvDetailPhone);
        tvStatus = findViewById(R.id.tvDetailStatus);
        mainImageView = findViewById(R.id.ivDetailImage);
        imagesContainer = findViewById(R.id.layoutAllImages);
        btnMap = findViewById(R.id.btnOpenMap);

        // Gán dữ liệu
        tvName.setText(lot.getName());
        tvAddress.setText(lot.getAddress());
        tvPhone.setText("📞 " + (lot.getOwner() != null && lot.getOwner().getPhone() != null ? lot.getOwner().getPhone() : "Không có"));
        tvStatus.setText("Trạng thái: " + lot.getStatus());

        if (lot.getImages() != null && !lot.getImages().isEmpty()) {
            // Ảnh chính
            Glide.with(this)
                    .load(lot.getImages().get(0))
                    .placeholder(R.drawable.placeholder_parking)
                    .into(mainImageView);

            // Hiển thị các ảnh còn lại bên dưới
            for (int i = 0; i < lot.getImages().size(); i++) {
                String imageUrl = lot.getImages().get(i);
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        500
                );
                params.setMargins(0, 16, 0, 0);
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_parking)
                        .into(imageView);

                imagesContainer.addView(imageView);
            }
        } else {
            mainImageView.setImageResource(R.drawable.placeholder_parking);
        }

        // Nút mở bản đồ
        btnMap.setOnClickListener(v -> {
            if (lot.getCoordinates() != null) {
                double lat = lot.getCoordinates().getLat();
                double lng = lot.getCoordinates().getLng();
                String uri = "geo:" + lat + "," + lng + "?q=" + lat + "," + lng + "(" + lot.getName() + ")";
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapIntent.setPackage("com.google.android.apps.maps");
                try {
                    startActivity(mapIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "Không tìm thấy ứng dụng Google Maps", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Không có tọa độ để mở bản đồ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Bắt sự kiện nút Back trên Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Quay lại
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
