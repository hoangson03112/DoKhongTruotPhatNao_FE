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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagementFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private EditText searchBox;
    private Spinner roleFilter;

    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_management, container, false);

        recyclerView = view.findViewById(R.id.user_recycler_view);
        searchBox = view.findViewById(R.id.search_box);
        roleFilter = view.findViewById(R.id.role_filter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter(getContext(), new UserAdapter.OnUserActionListener() {
            @Override
            public void onDelete(User user) {
                deleteUser(user);
            }

            @Override
            public void onUpdate(User user) {
                AdminEditUserDialogFragment dialog = new AdminEditUserDialogFragment(user, updatedUser -> updateUser(updatedUser));
                dialog.show(getChildFragmentManager(), "EditUser");
            }
        });
        recyclerView.setAdapter(adapter); // ✅ gán adapter ngay sau khi tạo

        // setup role filter
        String[] roles = {"Tất cả", "admin", "staff", "user", "parking_owner"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, roles);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleFilter.setAdapter(spinnerAdapter);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString(), roleFilter.getSelectedItem().toString());
            }
        });

        roleFilter.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                adapter.filter(searchBox.getText().toString(), roleFilter.getSelectedItem().toString());
            }

            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        fetchUsers();

        return view;
    }

    private void fetchUsers() {
        apiService.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setUserList(response.body());
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteUser(User user) {
        apiService.deleteUser(user.getId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                Toast.makeText(getContext(), "Đã xoá người dùng", Toast.LENGTH_SHORT).show();
                fetchUsers();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi xoá người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser(User user) {
        apiService.updateUser(user.getId(), user).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                Toast.makeText(getContext(), "Đã cập nhật người dùng", Toast.LENGTH_SHORT).show();
                fetchUsers();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi cập nhật người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
