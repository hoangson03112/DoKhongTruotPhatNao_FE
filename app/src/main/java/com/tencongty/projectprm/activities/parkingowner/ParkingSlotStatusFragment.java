package com.tencongty.projectprm.activities.parkingowner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.Reservation;
import com.tencongty.projectprm.models.ReservationResponse;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;
import com.tencongty.projectprm.utils.TokenManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import android.graphics.Color;


public class ParkingSlotStatusFragment extends Fragment {

    private RecyclerView rvSlots;
    private TextView tvAvailableCount, tvReservedCount;
    private ParkingSlotAdapter adapter;
    private List<Reservation> reservationList = new ArrayList<>();
    private int capacity = 100;
    private String parkingLotId = "";

    public ParkingSlotStatusFragment() {
        // Required empty public constructor
    }

    public static ParkingSlotStatusFragment newInstance(String parkingLotId, int capacity) {
        ParkingSlotStatusFragment fragment = new ParkingSlotStatusFragment();
        Bundle args = new Bundle();
        args.putString("PARKING_LOT_ID", parkingLotId);
        args.putInt("PARKING_LOT_CAPACITY", capacity);
        args.putString("PARKING_LOT_NAME", "Tên bãi đỗ nào đó"); // thêm dòng này
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parkingLotId = getArguments().getString("PARKING_LOT_ID", "");
            capacity = getArguments().getInt("PARKING_LOT_CAPACITY", 100);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_slot_status, container, false);

        // Ánh xạ các view chính
        rvSlots = view.findViewById(R.id.rvSlots);
        tvAvailableCount = view.findViewById(R.id.tvAvailableCount);
        tvReservedCount = view.findViewById(R.id.tvReservedCount);
        TextView tvParkingLotName = view.findViewById(R.id.tvParkingLotName);
        TextView titleText = view.findViewById(R.id.titleText);
        ImageView btnBack = view.findViewById(R.id.btnBack);

        // Xử lý dữ liệu truyền qua arguments
        if (getArguments() != null) {
            String parkingLotName = getArguments().getString("PARKING_LOT_NAME", "Bãi đỗ xe");
            capacity = getArguments().getInt("PARKING_LOT_CAPACITY", 100);
            parkingLotId = getArguments().getString("PARKING_LOT_ID", "");

            tvParkingLotName.setText(parkingLotName);
            titleText.setText("Bãi đỗ của bạn");
        }

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.popBackStack(); // Trở về Fragment trước
        });

        // Setup RecyclerView
        rvSlots.setLayoutManager(new GridLayoutManager(requireContext(), 5));
        reservationList = new ArrayList<>();
        adapter = new ParkingSlotAdapter(requireContext(), reservationList, capacity);
        rvSlots.setAdapter(adapter);

        // Load dữ liệu
        loadReservations();

        ImageView imgParkingImage = view.findViewById(R.id.imgParkingImage);

        if (getArguments() != null) {
            String parkingImageUrl = getArguments().getString("PARKING_LOT_IMAGE", null);
            if (parkingImageUrl != null && !parkingImageUrl.isEmpty()) {
                Glide.with(requireContext())
                        .load(parkingImageUrl)
                        .placeholder(R.drawable.baseline_image_24)
                        .error(R.drawable.baseline_image_not_supported_24)
                        .into(imgParkingImage);
            } else {
                imgParkingImage.setVisibility(View.GONE);
            }
        }
        return view;
    }

    private void loadReservations() {
        String token = new TokenManager(requireContext()).getToken();
        if (parkingLotId == null || parkingLotId.isEmpty()) {
            Toast.makeText(requireContext(), "Không có ID bãi đỗ!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        apiService.getReservationsByOwner("Bearer " + token, parkingLotId)
                .enqueue(new Callback<ReservationResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ReservationResponse> call, @NonNull Response<ReservationResponse> response) {
                        if (!response.isSuccessful()) {
                            Log.e("API", "HTTP error: " + response.code());
                            try {
                                if (response.errorBody() != null) {
                                    Log.e("API", "errorBody: " + response.errorBody().string());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            reservationList.clear();
                            reservationList.addAll(response.body().getData());

                            int reservedCount = reservationList.size();
                            int availableCount = capacity - reservedCount;

                            tvReservedCount.setText("Slot đã đặt: " + reservedCount);
                            tvAvailableCount.setText("Slot còn trống: " + availableCount);

                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(requireContext(), "Lỗi khi tải dữ liệu đặt chỗ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ReservationResponse> call, @NonNull Throwable t) {
                        Toast.makeText(requireContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
