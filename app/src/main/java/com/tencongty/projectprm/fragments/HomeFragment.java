package com.tencongty.projectprm.fragments;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.adapters.ParkingLotAdapter;
import com.tencongty.projectprm.models.ParkingLot;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;
import com.tencongty.projectprm.utils.LocationHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private RecyclerView rvParkingLots;
    private ApiService apiService;
    private List<ParkingLot> allParkingLots = new ArrayList<>();
    private double currentUserLat, currentUserLon;


    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvParkingLots = view.findViewById(R.id.nearbyParkingRecyclerView);
        rvParkingLots.setLayoutManager(new LinearLayoutManager(getContext()));

        apiService = ApiClient.getClient(getContext()).create(ApiService.class);

        if (getContext() != null && getActivity() != null) {
            checkLocationPermissionAndGetLocation();
        }
        TextView viewAll = view.findViewById(R.id.viewAllParkingLotsText);
        viewAll.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("parkingLotsJson", new Gson().toJson(allParkingLots));
            bundle.putDouble("userLat", currentUserLat);
            bundle.putDouble("userLng", currentUserLon);

            // Sử dụng Navigation để chuyển Fragment
            NavHostFragment.findNavController(this)
                    .navigate(R.id.parkingFragment, bundle);
        });




        return view;
    }

    private void checkLocationPermissionAndGetLocation() {
        if (getContext() == null || getActivity() == null) return;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Dùng requestPermissions của Fragment để tránh crash
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        } else {
            getUserLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                Toast.makeText(getContext(), "Bạn cần cấp quyền vị trí để tiếp tục", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getUserLocation() {
        if (getActivity() == null || getContext() == null) return;

        LocationHelper.getCurrentLocation(getActivity(), new LocationHelper.MyLocationCallback() {
            @Override
            public void onLocationReceived(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    Toast.makeText(getContext(), "Lat: " + lat + ", Lon: " + lon, Toast.LENGTH_SHORT).show();
                    fetchNearbyParkingLots(lat, lon);
                } else {
                    Toast.makeText(getContext(), "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchNearbyParkingLots(double lat, double lon) {
        Call<List<ParkingLot>> call = apiService.getNearbyParkingLots(lat, lon);
        call.enqueue(new Callback<List<ParkingLot>>() {
            @Override
            public void onResponse(Call<List<ParkingLot>> call, Response<List<ParkingLot>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ParkingLot> fullList = response.body();

                    // Lấy 3 bãi gần nhất
                    List<ParkingLot> top3 = fullList.size() > 3 ? fullList.subList(0, 3) : fullList;

                    rvParkingLots.setAdapter(new ParkingLotAdapter(top3, lat, lon));

                    // Lưu lại danh sách đầy đủ nếu cần truyền sang màn “Xem tất cả”
                    allParkingLots = fullList; // bạn khai báo biến này ở ngoài class
                    currentUserLat = lat;
                    currentUserLon = lon;
                } else {
                    Toast.makeText(getContext(), "Không có dữ liệu bãi đỗ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ParkingLot>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
