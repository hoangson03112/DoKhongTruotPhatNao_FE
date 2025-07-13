package com.tencongty.projectprm.fragments;

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
import com.tencongty.projectprm.activities.parkingowner.ParkingLotAdapter;
import com.tencongty.projectprm.activities.parkingowner.ParkingSlotStatusActivity;
import com.tencongty.projectprm.models.ParkingLot;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;
import com.tencongty.projectprm.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

public class ParkingListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ParkingLotAdapter adapter;
    private List<ParkingLot> parkingLotList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_owner_parking_list, container, false);
        recyclerView = view.findViewById(R.id.rv_parking_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Gán adapter với listener xử lý click
        adapter = new ParkingLotAdapter(parkingLotList, parkingLot -> {
            Intent intent = new Intent(requireContext(), ParkingSlotStatusActivity.class);
            intent.putExtra("PARKING_LOT_ID", parkingLot.get_id());
            intent.putExtra("PARKING_LOT_CAPACITY", parkingLot.getCapacity());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        loadParkingLots();
        return view;
    }

    private void loadParkingLots() {
        String token = new TokenManager(requireContext()).getToken();
        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        apiService.getParkingLots("Bearer " + token).enqueue(new Callback<List<ParkingLot>>() {
            @Override
            public void onResponse(Call<List<ParkingLot>> call, Response<List<ParkingLot>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    parkingLotList.clear();
                    parkingLotList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không tải được danh sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ParkingLot>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
