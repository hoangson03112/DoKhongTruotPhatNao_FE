package com.tencongty.projectprm.activities.parkingowner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        // Ánh xạ view
        rvSlots = view.findViewById(R.id.rvSlots);
        tvAvailableCount = view.findViewById(R.id.tvAvailableCount);
        tvReservedCount = view.findViewById(R.id.tvReservedCount);

        // Hiển thị tên bãi đỗ
        TextView tvParkingLotName = view.findViewById(R.id.tvParkingLotName);
        if (getArguments() != null) {
            String parkingLotName = getArguments().getString("PARKING_LOT_NAME", "Bãi đỗ xe");
            tvParkingLotName.setText(parkingLotName);
        }


        // Khởi tạo RecyclerView
        rvSlots.setLayoutManager(new GridLayoutManager(requireContext(), 5));
        reservationList = new ArrayList<>(); // đảm bảo danh sách không null
        adapter = new ParkingSlotAdapter(requireContext(), reservationList, capacity);
        rvSlots.setAdapter(adapter);

        // Gọi API để load dữ liệu
        loadReservations();

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
