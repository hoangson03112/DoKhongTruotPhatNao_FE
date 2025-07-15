package com.tencongty.projectprm.activities.admin.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.adapters.OwnerAdapter;
import com.tencongty.projectprm.fragments.AdminEditOwnerDialogFragment;
import com.tencongty.projectprm.models.Owner;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerManagementFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText searchBox;
    private Spinner statusFilter;

    private OwnerAdapter adapter;
    private ApiService apiService;
    private List<Owner> allOwners = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_management, container, false);

        recyclerView = view.findViewById(R.id.owner_recycler_view);
        searchBox = view.findViewById(R.id.owner_search_box);
        statusFilter = view.findViewById(R.id.owner_status_filter);

        apiService = ApiClient.getClient(getContext()).create(ApiService.class);

        setupRecyclerView();
        setupFilters();
        fetchOwners();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OwnerAdapter(getContext(), new OwnerAdapter.OnOwnerActionListener() {
            @Override
            public void onDelete(Owner owner) {
                deleteOwner(owner);
            }

            @Override
            public void onUpdate(Owner owner) {
                AdminEditOwnerDialogFragment dialog = new AdminEditOwnerDialogFragment(owner, updated -> updateOwner(updated));
                dialog.show(getChildFragmentManager(), "EditOwner");
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupFilters() {
        String[] statuses = {"Tất cả", "pending", "verified", "rejected"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusFilter.setAdapter(statusAdapter);

        // Tìm kiếm
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterOwners();
            }
        });

        // Lọc trạng thái
        statusFilter.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                filterOwners();
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void fetchOwners() {
        apiService.getAllOwners().enqueue(new Callback<List<Owner>>() {
            @Override
            public void onResponse(@NonNull Call<List<Owner>> call, @NonNull Response<List<Owner>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allOwners = response.body();
                    filterOwners();
                } else {
                    Toast.makeText(getContext(), "❌ Lỗi khi tải danh sách chủ bãi xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Owner>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "❌ Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterOwners() {
        String keyword = searchBox.getText().toString().toLowerCase().trim();
        String selectedStatus = statusFilter.getSelectedItem().toString();

        List<Owner> filtered = new ArrayList<>();
        for (Owner owner : allOwners) {
            if (owner.isDeleted()) continue;

            // Lọc theo trạng thái
            if (!"Tất cả".equals(selectedStatus) && !selectedStatus.equals(owner.getVerificationStatus())) {
                continue;
            }

            // Lọc theo từ khóa
            boolean match = owner.getUsername() != null && owner.getUsername().toLowerCase().contains(keyword)
                    || owner.getName() != null && owner.getName().toLowerCase().contains(keyword)
                    || owner.getEmail() != null && owner.getEmail().toLowerCase().contains(keyword);

            if (keyword.isEmpty() || match) {
                filtered.add(owner);
            }
        }

        adapter.setOwnerList(filtered);
    }

    private void deleteOwner(Owner owner) {
        apiService.deleteOwner(owner.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "✅ Đã xóa chủ bãi xe khỏi hệ thống", Toast.LENGTH_SHORT).show();
                    fetchOwners(); // Làm mới danh sách chủ bãi xe
                } else {
                    Toast.makeText(getContext(), "❌ Lỗi xoá chủ bãi xe: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "❌ Kết nối thất bại khi xoá chủ bãi xe", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateOwner(Owner owner) {
        Map<String, String> statusBody = new HashMap<>();
        statusBody.put("status", owner.getVerificationStatus()); // verified, pending, rejected

        apiService.updateOwner(owner.getId(), statusBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "✅ Đã cập nhật trạng thái xác thực của chủ bãi xe", Toast.LENGTH_SHORT).show();
                    Log.d("UPDATE_OWNER", "Cập nhật thành công owner ID: " + owner.getId());
                    fetchOwners(); // Làm mới danh sách
                } else {
                    Log.e("UPDATE_OWNER", "❌ Lỗi cập nhật: " + response.code() + " - " + response.message());
                    Toast.makeText(getContext(), "❌ Lỗi khi cập nhật chủ bãi xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("UPDATE_OWNER", "❌ Cập nhật thất bại: " + t.getMessage());
                Toast.makeText(getContext(), "❌ Lỗi kết nối khi cập nhật chủ bãi xe", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
