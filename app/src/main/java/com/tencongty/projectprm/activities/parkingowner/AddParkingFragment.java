package com.tencongty.projectprm.activities.parkingowner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
    private Button btnAddImage, btnSubmit;
    private RecyclerView rvImages;
    private CheckBox cb5000;
    private AddParkingImageAdapter imageAdapter;

    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int MAX_IMAGE_COUNT = 5;

    private List<Uri> imageUris = new ArrayList<>();

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
        cb5000 = view.findViewById(R.id.cb5000);
        btnAddImage = view.findViewById(R.id.btnAddImage);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        rvImages = view.findViewById(R.id.rvImages);

        imageAdapter = new AddParkingImageAdapter(requireContext(), imageUris);
        rvImages.setAdapter(imageAdapter);
    }

    private void setupListeners() {
        btnAddImage.setOnClickListener(v -> {
            if (imageUris.size() >= MAX_IMAGE_COUNT) {
                Toast.makeText(getContext(), "Tối đa 5 ảnh", Toast.LENGTH_SHORT).show();
                return;
            }
            pickImage();
        });

        btnSubmit.setOnClickListener(v -> {
            submitParking();
        });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUris.add(data.getData());
            imageAdapter.notifyDataSetChanged();
        }
    }

    private void submitParking() {
        String name = etName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String latStr = etLatitude.getText().toString().trim();
        String lngStr = etLongitude.getText().toString().trim();
        String capacityStr = etCapacity.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || latStr.isEmpty() || lngStr.isEmpty() || capacityStr.isEmpty() || imageUris.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin và chọn ít nhất 1 ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        double lat = Double.parseDouble(latStr);
        double lng = Double.parseDouble(lngStr);
        int capacity = Integer.parseInt(capacityStr);

        List<String> pricingIds = new ArrayList<>();
        if (cb5000.isChecked()) {
            pricingIds.add("686bd958481ae15de8c642f2");
        }

        // Giả lập tên ảnh từ URI (nếu cần có tên thật thì cần upload ảnh và lấy link từ server)
        List<String> imageNames = new ArrayList<>();
        for (Uri uri : imageUris) {
            imageNames.add(uri.getLastPathSegment()); // Hoặc có thể hardcode tên
        }

        AddParkingLotRequest.Coordinates coordinates = new AddParkingLotRequest.Coordinates(lat, lng);
        AddParkingLotRequest request = new AddParkingLotRequest(name, address, coordinates, capacity, imageNames, pricingIds);

        // Lấy token từ SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null) {
            Toast.makeText(getContext(), "Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<JsonObject> call = apiService.addParkingLot("Bearer " + token, request);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Thêm bãi đỗ thành công!", Toast.LENGTH_SHORT).show();
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
