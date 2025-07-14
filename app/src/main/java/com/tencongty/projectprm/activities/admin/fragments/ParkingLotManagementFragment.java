package com.tencongty.projectprm.activities.admin.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.adapters.AdminParkingLotAdapter;
import com.tencongty.projectprm.models.AdminParkingLot;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingLotManagementFragment extends Fragment {
    private RecyclerView recyclerView;
    private EditText etSearch;
    private Spinner spinnerFilter;
    private AdminParkingLotAdapter adapter;
    private List<AdminParkingLot> fullList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_parking_lot, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        etSearch = view.findViewById(R.id.etSearch);
        spinnerFilter = view.findViewById(R.id.spinnerFilter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(getContext(), R.array.status_filter, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(filterAdapter);

        loadData();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    private void loadData() {
        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        apiService.getAllParkingLots().enqueue(new Callback<List<AdminParkingLot>>() {
            @Override
            public void onResponse(@NonNull Call<List<AdminParkingLot>> call, @NonNull Response<List<AdminParkingLot>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullList = response.body();
                    updateAdapter(fullList);
                } else {
                    Toast.makeText(getContext(), "\u274C Tải dữ liệu thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AdminParkingLot>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "\u274C Lỗi kết nối khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAdapter(List<AdminParkingLot> list) {
        adapter = new AdminParkingLotAdapter(getContext(), list, this::loadData);
        recyclerView.setAdapter(adapter);
    }

    private void filterList() {
        String keyword = etSearch.getText().toString().toLowerCase();
        String filterStatus = spinnerFilter.getSelectedItem().toString();

        List<AdminParkingLot> filtered = new ArrayList<>();
        for (AdminParkingLot lot : fullList) {
            boolean matchesSearch = lot.getName().toLowerCase().contains(keyword) || lot.getAddress().toLowerCase().contains(keyword);
            boolean matchesStatus = filterStatus.equals("Tất cả") || lot.getStatus().equalsIgnoreCase(filterStatus);
            if (matchesSearch && matchesStatus) {
                filtered.add(lot);
            }
        }
        updateAdapter(filtered);
    }
}
