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
import com.tencongty.projectprm.adapters.UserAdapter;
import com.tencongty.projectprm.fragments.AdminEditUserDialogFragment;
import com.tencongty.projectprm.models.User;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagementFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private EditText searchBox;
    private Spinner roleFilter;
    private ApiService apiService;
    private List<User> allUsers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_management, container, false);

        initViews(view);
        setupRecyclerView();
        setupFilters();

        apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        fetchUsers();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.user_recycler_view);
        searchBox = view.findViewById(R.id.search_box);
        roleFilter = view.findViewById(R.id.role_filter);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter(getContext(), new UserAdapter.OnUserActionListener() {
            @Override
            public void onDelete(User user) {
                showDeleteConfirmation(user);
            }

            @Override
            public void onUpdate(User user) {
                AdminEditUserDialogFragment dialog = new AdminEditUserDialogFragment(user,
                        updatedUser -> updateUser(updatedUser));
                dialog.show(getChildFragmentManager(), "EditUser");
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupFilters() {
        // Setup role filter
        String[] roles = {"Tất cả", "admin", "staff", "user", "parking_owner"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, roles);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleFilter.setAdapter(spinnerAdapter);

        // Search box listener
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers();
            }
        });

        // Role filter listener
        roleFilter.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                filterUsers();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void fetchUsers() {
        apiService.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allUsers = response.body();
                    Log.d("DEBUG", "Số lượng người dùng trả về: " + allUsers.size());
                    adapter.setUserList(allUsers);
                    filterUsers(); // Apply current filter
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterUsers() {
        String searchText = searchBox.getText().toString().toLowerCase().trim();
        String selectedRole = roleFilter.getSelectedItem().toString();

        List<User> filteredUsers = new ArrayList<>();

        for (User user : allUsers) {

            // ✅ Bỏ qua user đã bị đánh dấu isDeleted = true
            if (user.isDeleted()) continue;

            // Filter by role
            if (!selectedRole.equals("Tất cả") && !user.getRole().equals(selectedRole)) {
                continue;
            }

            // Filter by search text
            if (!searchText.isEmpty()) {
                boolean matches = false;
                if (user.getName() != null && user.getName().toLowerCase().contains(searchText)) {
                    matches = true;
                }
                if (user.getEmail() != null && user.getEmail().toLowerCase().contains(searchText)) {
                    matches = true;
                }
                if (user.getUsername() != null && user.getUsername().toLowerCase().contains(searchText)) {
                    matches = true;
                }
                if (!matches) continue;
            }

            filteredUsers.add(user);
        }

        adapter.setUserList(filteredUsers);
    }


    private void showDeleteConfirmation(User user) {
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa người dùng \"" + user.getName() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteUser(user))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteUser(User user) {
        apiService.deleteUser(user.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "✅ Đã xóa người dùng khỏi hệ thống", Toast.LENGTH_SHORT).show();

                    fetchUsers(); // Refresh list
                } else {
                    Toast.makeText(getContext(), "❌ Lỗi xoá người dùng: " + response.code(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "❌ Kết nối thất bại khi xoá người dùng", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void updateUser(User user) {
        Map<String, String> statusBody = new HashMap<>();
        statusBody.put("status", user.getVerificationStatus());

        apiService.updateUserStatus(user.getId(), statusBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "✅ Đã cập nhật trạng thái xác thực", Toast.LENGTH_SHORT).show();
                    fetchUsers();
                } else {
                    Log.e("UPDATE_USER", "❌ Lỗi cập nhật: " + response.code() + " - " + response.message());
                    Toast.makeText(getContext(), "❌ Lỗi khi cập nhật người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "❌ Lỗi kết nối khi cập nhật người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

}