package com.tencongty.projectprm.activities.parkingowner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.ParkingLotOwner;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;
import com.tencongty.projectprm.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

public class ParkingListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ParkingLotOwnerAdapter adapter;
    private final List<ParkingLotOwner> parkingLotList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_owner_parking_list, container, false);
        recyclerView = view.findViewById(R.id.rv_parking_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo adapter với listener xử lý click
        adapter = new ParkingLotOwnerAdapter(parkingLotList, parkingLot -> {
            Bundle bundle = new Bundle();
            bundle.putString("PARKING_LOT_ID", parkingLot.get_id());
            bundle.putInt("PARKING_LOT_CAPACITY", parkingLot.getCapacity());
            bundle.putString("PARKING_LOT_NAME", parkingLot.getName());

            // Gửi link ảnh đầu tiên (nếu có)
            List<String> images = parkingLot.getImages();
            if (images != null && !images.isEmpty()) {
                bundle.putString("PARKING_LOT_IMAGE", images.get(0));
            }

            androidx.navigation.NavController navController =
                    androidx.navigation.fragment.NavHostFragment.findNavController(ParkingListFragment.this);

            navController.navigate(R.id.nav_parking_slot_status, bundle);
        });


        recyclerView.setAdapter(adapter);

        loadParkingLots();
        return view;
    }

    private void loadParkingLots() {
        String token = new TokenManager(requireContext()).getToken();
        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        apiService.getParkingLots().enqueue(new Callback<List<ParkingLotOwner>>() {
            @Override
            public void onResponse(Call<List<ParkingLotOwner>> call, Response<List<ParkingLotOwner>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    parkingLotList.clear();
                    parkingLotList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không tải được danh sách bãi đỗ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ParkingLotOwner>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
