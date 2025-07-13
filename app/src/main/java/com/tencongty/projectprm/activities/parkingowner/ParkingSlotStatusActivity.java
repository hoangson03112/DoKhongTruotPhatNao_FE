package com.tencongty.projectprm.activities.parkingowner;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.Reservation;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingSlotStatusActivity extends AppCompatActivity {

    private RecyclerView rvSlots;
    private TextView tvAvailableCount, tvReservedCount;
    private ParkingSlotAdapter adapter;
    private List<String> slotStatusList = new ArrayList<>();
    private int capacity;
    private String parkingLotId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_slot_status);

        rvSlots = findViewById(R.id.rvSlots);
        tvAvailableCount = findViewById(R.id.tvAvailableCount);
        tvReservedCount = findViewById(R.id.tvReservedCount);

        rvSlots.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParkingSlotAdapter(slotStatusList);
        rvSlots.setAdapter(adapter);

        parkingLotId = getIntent().getStringExtra("PARKING_LOT_ID");
        capacity = getIntent().getIntExtra("PARKING_LOT_CAPACITY", 0);

        loadReservations();
    }

    private void loadReservations() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiService.getReservationsByOwner(parkingLotId).enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Set<Integer> bookedSlots = new HashSet<>();
                    for (int i = 0; i < response.body().size(); i++) {
                        Reservation r = response.body().get(i);
                        // giả định bạn có số thứ tự slot trong r.parkingLocation như "ô số 15"
                        String location = r.getParkingLocation();
                        if (location != null && location.matches(".*?\\d+.*")) {
                            String number = location.replaceAll("\\D+", ""); // Lấy số
                            bookedSlots.add(Integer.parseInt(number));
                        }
                    }

                    slotStatusList.clear();
                    for (int i = 1; i <= capacity; i++) {
                        if (bookedSlots.contains(i)) {
                            slotStatusList.add("Slot " + i + ": ĐÃ ĐẶT");
                        } else {
                            slotStatusList.add("Slot " + i + ": CÒN TRỐNG");
                        }
                    }

                    tvAvailableCount.setText("Slot còn trống: " + (capacity - bookedSlots.size()));
                    tvReservedCount.setText("Slot đã đặt: " + bookedSlots.size());
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(ParkingSlotStatusActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Toast.makeText(ParkingSlotStatusActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
