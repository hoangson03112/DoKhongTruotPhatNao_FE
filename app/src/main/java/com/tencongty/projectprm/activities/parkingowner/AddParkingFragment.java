package com.tencongty.projectprm.activities.parkingowner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.AddParkingLotRequest;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddParkingFragment extends Fragment {

    private EditText etName, etAddress, etLatitude, etLongitude, etCapacity;
    private EditText etImageUrl1, etImageUrl2, etImageUrl3;
    private Button btnSubmit;
    private RadioButton rb5000, rb10000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_owner_add_parking, container, false);
        initViews(view);
        setupListeners();
        return view;
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.etName);
        etAddress = view.findViewById(R.id.etAddress);
        etLatitude = view.findViewById(R.id.etLatitude);
        etLongitude = view.findViewById(R.id.etLongitude);
        etCapacity = view.findViewById(R.id.etCapacity);
        etImageUrl1 = view.findViewById(R.id.etImageUrl1);
        etImageUrl2 = view.findViewById(R.id.etImageUrl2);
        etImageUrl3 = view.findViewById(R.id.etImageUrl3);
        rb5000 = view.findViewById(R.id.rb5000);
        rb10000 = view.findViewById(R.id.rb10000);
        btnSubmit = view.findViewById(R.id.btnSubmit);
    }

    private void setupListeners() {
        btnSubmit.setOnClickListener(v -> submitParking());
    }

    private void submitParking() {
        String name = etName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String latStr = etLatitude.getText().toString().trim();
        String lngStr = etLongitude.getText().toString().trim();
        String capacityStr = etCapacity.getText().toString().trim();

        // Validate bắt buộc
        if (name.isEmpty() || address.isEmpty() || latStr.isEmpty() || lngStr.isEmpty() || capacityStr.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thu thập các URL ảnh (ít nhất 1)
        List<String> imageUrls = new ArrayList<>();
        if (!etImageUrl1.getText().toString().trim().isEmpty()) {
            imageUrls.add(etImageUrl1.getText().toString().trim());
        }
        if (!etImageUrl2.getText().toString().trim().isEmpty()) {
            imageUrls.add(etImageUrl2.getText().toString().trim());
        }
        if (!etImageUrl3.getText().toString().trim().isEmpty()) {
            imageUrls.add(etImageUrl3.getText().toString().trim());
        }

        if (imageUrls.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập ít nhất 1 URL ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        double lat = Double.parseDouble(latStr);
        double lng = Double.parseDouble(lngStr);
        int capacity = Integer.parseInt(capacityStr);

        List<String> pricingIds = new ArrayList<>();
        if (rb5000.isChecked()) {
            pricingIds.add("68753c014b43bfbf0de7b947");
        }
        if (rb10000.isChecked()) {
            pricingIds.add("68753c674b43bfbf0de7b948");
        }

        AddParkingLotRequest.Coordinates coordinates = new AddParkingLotRequest.Coordinates(lat, lng);
        AddParkingLotRequest request = new AddParkingLotRequest(name, address, coordinates, capacity, imageUrls, pricingIds);

        // ✅ Gọi API mà không cần tự lấy token
        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<JsonObject> call = apiService.addParkingLot(request); // Không truyền token

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Thêm bãi đỗ thành công! Chờ phê duyệt.", Toast.LENGTH_SHORT).show();

                    // Xoá dữ liệu sau khi thêm thành công
                    etName.setText("");
                    etAddress.setText("");
                    etLatitude.setText("");
                    etLongitude.setText("");
                    etCapacity.setText("");
                    etImageUrl1.setText("");
                    etImageUrl2.setText("");
                    etImageUrl3.setText("");

                    // Bỏ chọn RadioButton (nếu cần)
                    rb5000.setChecked(false);
                    rb10000.setChecked(false);

                } else {
                    Toast.makeText(getContext(), "Thêm thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
