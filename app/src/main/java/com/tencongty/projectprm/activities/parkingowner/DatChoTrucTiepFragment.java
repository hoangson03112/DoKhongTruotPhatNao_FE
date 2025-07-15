package com.tencongty.projectprm.activities.parkingowner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.ParkingLotOwner;
import com.tencongty.projectprm.models.ParkingOwnerActionRequest;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;
import com.tencongty.projectprm.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatChoTrucTiepFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatTrucTiepAdapter adapter;
    private final List<ParkingLotOwner> parkingLotList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dat_cho_truc_tiep, container, false);
        recyclerView = view.findViewById(R.id.rv_parking_list_add_bk);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 🟢 KHỞI TẠO ADAPTER TRƯỚC
        adapter = new DatTrucTiepAdapter(parkingLotList, new DatTrucTiepAdapter.OnParkingLotClickListener() {
            @Override
            public void onAddBooking(ParkingLotOwner parkingLot) {
                String token = new TokenManager(requireContext()).getToken();
                ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

                ParkingOwnerActionRequest request = new ParkingOwnerActionRequest("in");

                apiService.updateParkingBookingStatus(parkingLot.get_id(), request).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Thêm chỗ đặt thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Thêm chỗ đặt thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDeleteBooking(ParkingLotOwner parkingLot) {
                String token = new TokenManager(requireContext()).getToken();
                ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

                ParkingOwnerActionRequest request = new ParkingOwnerActionRequest("out");

                apiService.updateParkingBookingStatus(parkingLot.get_id(), request).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Hủy chỗ đặt thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Không còn chỗ đặt trực tiếp tại bãi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        recyclerView.setAdapter(adapter);

        // Sau khi đã có adapter -> load data
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

                    // ✅ KHÔNG BỊ NULL VÌ ĐÃ KHỞI TẠO ADAPTER TRƯỚC ĐÓ
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không tải được danh sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ParkingLotOwner>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

