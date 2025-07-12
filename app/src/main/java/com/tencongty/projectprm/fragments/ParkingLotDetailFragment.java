package com.tencongty.projectprm.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.adapters.ParkingImageAdapter;
import com.tencongty.projectprm.models.ParkingLot;
import com.tencongty.projectprm.models.ParkingPricing;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;
import com.tencongty.projectprm.utils.LocationHelper; // Sử dụng LocationHelper để tính khoảng cách

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingLotDetailFragment extends Fragment implements OnMapReadyCallback {

    private TextView tvName, tvAddress, tvDistance, tvCapacity, tvAvailableSlots;
    private TextView tvOwnerName, tvOwnerEmail, tvOwnerPhone, tvCreatedAt;
    private RecyclerView rvImages, rvPricing;
    private ChipGroup chipGroupFeatures;
    private MaterialButton btnGetDirections, btnBookNow;
    private ImageView ivBack;
    private View loadingLayout;

    private ParkingLot parkingLotDetail;
    private String parkingLotId;
    private double userLat, userLng;
    private ApiService apiService;

    // Biến cho Google Maps
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;

    public ParkingLotDetailFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_lot_detail, container, false);

        initViews(view);
        setupRecyclerViews();
        getArgumentsData();

        // Khởi tạo SupportMapFragment
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fetchParkingLotDetail();

        return view;
    }

    private void initViews(View view) {
        tvName = view.findViewById(R.id.tvParkingName);
        tvAddress = view.findViewById(R.id.tvParkingAddress);
        tvDistance = view.findViewById(R.id.tvDistance);
        tvCapacity = view.findViewById(R.id.tvCapacity);
        tvAvailableSlots = view.findViewById(R.id.tvAvailableSlots);
        tvOwnerName = view.findViewById(R.id.tvOwnerName);
        tvOwnerEmail = view.findViewById(R.id.tvOwnerEmail);
        tvOwnerPhone = view.findViewById(R.id.tvOwnerPhone);

        rvImages = view.findViewById(R.id.rvParkingImages);
        rvPricing = view.findViewById(R.id.rvPricing);
        chipGroupFeatures = view.findViewById(R.id.chipGroupFeatures);

        btnGetDirections = view.findViewById(R.id.btnGetDirections);
        btnBookNow = view.findViewById(R.id.btnBookNow);
        ivBack = view.findViewById(R.id.ivBack);
        loadingLayout = view.findViewById(R.id.loadingLayout);

        apiService = ApiClient.getClient(getContext()).create(ApiService.class);

        // Set click listeners
        ivBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // Cập nhật click listener cho nút chỉ đường để hiển thị trên bản đồ nội bộ
        btnGetDirections.setOnClickListener(v -> showDirectionsOnMap());

        btnBookNow.setOnClickListener(v -> bookParkingLot());
    }

    private void setupRecyclerViews() {
        // Cấu hình RecyclerView cho hình ảnh (horizontal scroll)
        rvImages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        // Cấu hình RecyclerView cho giá (vertical scroll)
        rvPricing.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getArgumentsData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            parkingLotId = bundle.getString("parkingLotId");
            userLat = bundle.getDouble("userLat");
            userLng = bundle.getDouble("userLng");
        }
    }

    private void fetchParkingLotDetail() {
        showLoading(true);
        if (parkingLotId == null) {
            Toast.makeText(getContext(), "Không tìm thấy ID bãi đỗ xe", Toast.LENGTH_SHORT).show();
            showLoading(false);
            return;
        }

        apiService.getParkingLotDetail(parkingLotId).enqueue(new Callback<ParkingLot>() {
            @Override
            public void onResponse(Call<ParkingLot> call, Response<ParkingLot> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    parkingLotDetail = response.body();
                    displayParkingLotInfo();
                } else {
                    Toast.makeText(getContext(), "Không thể tải chi tiết bãi đỗ xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ParkingLot> call, Throwable t) {
                showLoading(false);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayParkingLotInfo() {
        if (parkingLotDetail == null) return;

        tvName.setText(parkingLotDetail.getName());
        tvAddress.setText(parkingLotDetail.getAddress());
        tvCapacity.setText("Tổng số chỗ: " + parkingLotDetail.getCapacity());
        tvAvailableSlots.setText("Còn trống: " + parkingLotDetail.getAvailableSlots());

        // Tính khoảng cách (Sử dụng LocationHelper hoặc DistanceCalculator)
        double distance = LocationHelper.calculateDistance(
                userLat, userLng,
                parkingLotDetail.getLatitude(),
                parkingLotDetail.getLongitude()
        );
        DecimalFormat df = new DecimalFormat("#.##");
        tvDistance.setText("Cách bạn: " + df.format(distance) + " km");

        // Thông tin chủ sở hữu
        tvOwnerName.setText("Tên: " + parkingLotDetail.getOwner().getName());
        tvOwnerEmail.setText("Email: " + parkingLotDetail.getOwner().getEmail());
        tvOwnerPhone.setText("SĐT: " + parkingLotDetail.getOwner().getPhoneNumber());

        // Hiển thị hình ảnh
        if (parkingLotDetail.getImages() != null && !parkingLotDetail.getImages().isEmpty()) {
            rvImages.setAdapter(new ParkingImageAdapter(parkingLotDetail.getImages()));
        }

        // Hiển thị giá (Nếu bạn có PricingAdapter)
        // if (parkingLotDetail.getPricing() != null) {
        //     rvPricing.setAdapter(new PricingAdapter(parkingLotDetail.getPricing()));
        // }

        // Hiển thị tiện ích (features)
        chipGroupFeatures.removeAllViews();
        if (parkingLotDetail.getFeatures() != null) {
            for (String feature : parkingLotDetail.getFeatures()) {
                Chip chip = new Chip(getContext());
                chip.setText(feature);
                chip.setChipBackgroundColorResource(R.color.bg_green);
                chip.setTextColor(getResources().getColor(android.R.color.white));
                chip.setClickable(false);
                chipGroupFeatures.addView(chip);
            }
        }

        // Sau khi hiển thị thông tin, nếu bản đồ đã sẵn sàng, hiển thị vị trí
        if (googleMap != null) {
            displayParkingLotOnMap();
        }
    }

    // Phương thức xử lý khi người dùng nhấn nút "Xem bản đồ"
    private void showDirectionsOnMap() {
        if (googleMap != null && parkingLotDetail != null) {
            double destLat = parkingLotDetail.getCoordinates().getLat();
            double destLng = parkingLotDetail.getCoordinates().getLng();

            // Di chuyển camera đến vị trí bãi đỗ xe
            LatLng parkingLocation = new LatLng(destLat, destLng);
            // Sử dụng animateCamera để có hiệu ứng mượt mà
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(parkingLocation, 16));
            Toast.makeText(getContext(), "Di chuyển đến vị trí bãi đỗ xe trên bản đồ", Toast.LENGTH_SHORT).show();
        } else {
            // Trường hợp bản đồ chưa sẵn sàng hoặc dữ liệu chưa tải xong
            Toast.makeText(getContext(), "Bản đồ đang tải...", Toast.LENGTH_SHORT).show();
        }
    }

    // Triển khai OnMapReadyCallback
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Cấu hình cơ bản cho bản đồ
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false); // Ẩn nút chỉ đường mặc định

        // Sau khi bản đồ sẵn sàng, nếu dữ liệu bãi đỗ xe đã có, hiển thị vị trí
        if (parkingLotDetail != null) {
            displayParkingLotOnMap();
        }
    }

    // Phương thức hiển thị vị trí bãi đỗ xe trên bản đồ
    private void displayParkingLotOnMap() {
        if (googleMap == null || parkingLotDetail == null) {
            return;
        }

        double destLat = parkingLotDetail.getLatitude();
        double destLng = parkingLotDetail.getLongitude();

        LatLng parkingLocation = new LatLng(destLat, destLng);

        // Thêm marker cho bãi đỗ xe
        googleMap.addMarker(new MarkerOptions()
                .position(parkingLocation)
                .title(parkingLotDetail.getName())
                .snippet(parkingLotDetail.getAddress()));

        // Di chuyển camera đến vị trí bãi đỗ xe
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkingLocation, 15));
    }

    private void bookParkingLot() {
        if (parkingLotDetail == null) return;

        if (parkingLotDetail.getAvailableSlots() <= 0) {
            Toast.makeText(getContext(), "Bãi đỗ xe đã hết chỗ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Navigate to booking fragment or activity
        // You can implement booking logic here
        Bundle bundle = new Bundle();
        bundle.putString("parkingLotJson", new Gson().toJson(parkingLotDetail));
        bundle.putDouble("userLat", userLat);
        bundle.putDouble("userLng", userLng);

        // Navigate to booking fragment
        // NavHostFragment.findNavController(this).navigate(R.id.bookingFragment, bundle);

        Toast.makeText(getContext(), "Chức năng đặt chỗ sẽ được triển khai", Toast.LENGTH_SHORT).show();
    }

    private void showLoading(boolean show) {
        if (loadingLayout != null) {
            loadingLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}