package com.tencongty.projectprm.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.adapters.ParkingLotAdapter;
import com.tencongty.projectprm.models.ParkingLot;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;
import com.tencongty.projectprm.utils.LocationHelper;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingLotFragment extends Fragment implements ParkingLotAdapter.OnParkingLotClickListener {

    private RecyclerView recyclerView;
    private List<ParkingLot> parkingLots;
    private double userLat, userLng;
    private ParkingLotAdapter adapter;
    private final String TAG = "ParkingLotFragment";
    private TextView resultCountText;


    public ParkingLotFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_lot, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewAllParking);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        try {
            if (getArguments() != null && getArguments().containsKey("parkingLotsJson")) {
                // 👉 Trường hợp mở từ HomeFragment → truyền danh sách
                String json = getArguments().getString("parkingLotsJson");
                userLat = getArguments().getDouble("userLat", 0);
                userLng = getArguments().getDouble("userLng", 0);

                Type listType = new TypeToken<List<ParkingLot>>() {}.getType();
                parkingLots = new Gson().fromJson(json, listType);

                adapter = new ParkingLotAdapter(parkingLots, userLat, userLng, this);
                recyclerView.setAdapter(adapter);
            } else {
                // Mở từ BottomNavigation → lấy vị trí hiện tại
                LocationHelper.getCurrentLocation(requireActivity(), location -> {
                    if (location != null) {
                        userLat = location.getLatitude();
                        userLng = location.getLongitude();
                        fetchParkingLotsFromApi(userLat, userLng);
                    } else {
                        Toast.makeText(getContext(), "Không thể lấy vị trí", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: Lỗi khởi tạo view", e);
            Toast.makeText(getContext(), "Đã xảy ra lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void fetchParkingLotsFromApi(double lat, double lng) {
        try {
            ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
            apiService.getNearbyParkingLots(lat, lng).enqueue(new Callback<List<ParkingLot>>() {
                @Override
                public void onResponse(Call<List<ParkingLot>> call, Response<List<ParkingLot>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        parkingLots = response.body();
                        adapter = new ParkingLotAdapter(parkingLots, lat, lng, ParkingLotFragment.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Log.e(TAG, "onResponse: Không có dữ liệu trả về");
                        Toast.makeText(getContext(), "Không có dữ liệu bãi đỗ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<ParkingLot>> call, Throwable t) {
                    Log.e(TAG, "onFailure: Lỗi kết nối", t);
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "fetchParkingLotsFromApi: Exception", e);
            Toast.makeText(getContext(), "Lỗi khi tải danh sách bãi đỗ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onParkingLotClick(ParkingLot parkingLot) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("parkingLotId", parkingLot.getId());
            bundle.putDouble("userLat", userLat);
            bundle.putDouble("userLng", userLng);

            NavHostFragment.findNavController(ParkingLotFragment.this)
                    .navigate(R.id.action_parkingFragment_to_parkingDetailFragment, bundle);

        } catch (Exception e) {
            Log.e(TAG, "onParkingLotClick: Không thể mở chi tiết bãi đỗ", e);
            Toast.makeText(getContext(), "Không thể mở chi tiết bãi đỗ", Toast.LENGTH_SHORT).show();
        }
    }
}
