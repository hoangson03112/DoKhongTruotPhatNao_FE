package com.tencongty.projectprm.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.User;

public class AdminEditUserDialogFragment extends DialogFragment {

    private User user;
    private OnUserUpdateListener listener;

    private EditText editUsername, editName, editEmail, editPhone;
    private Spinner editRole, editVerificationStatus;

    public interface OnUserUpdateListener {
        void onUserUpdate(User user);
    }

    public AdminEditUserDialogFragment(User user, OnUserUpdateListener listener) {
        this.user = user;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.admin_dialog_edit_user, null);

        initViews(view);
        setupSpinners();
        populateUserData();

        builder.setView(view)
                .setTitle("Cập nhật trạng thái người dùng")
                .setPositiveButton("Lưu", (dialog, id) -> {
                    updateUserData(); // Chỉ cập nhật trạng thái
                    if (listener != null) {
                        listener.onUserUpdate(user);
                    }
                })
                .setNegativeButton("Hủy", (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    private void initViews(View view) {
        editUsername = view.findViewById(R.id.edit_username);
        editName = view.findViewById(R.id.edit_name);
        editEmail = view.findViewById(R.id.edit_email);
        editPhone = view.findViewById(R.id.edit_phone);
        editRole = view.findViewById(R.id.edit_role);
        editVerificationStatus = view.findViewById(R.id.edit_verification_status);

        // Disable các trường chỉ xem
        editUsername.setEnabled(false);
        editName.setEnabled(false);
        editEmail.setEnabled(false);
        editPhone.setEnabled(false);
        editRole.setEnabled(false);
    }

    private void setupSpinners() {
        // Role spinner
        String[] roles = {"admin", "staff", "user", "parking_owner"};
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, roles);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editRole.setAdapter(roleAdapter);

        // Verification status spinner
        String[] verificationStatuses = {"pending", "verified", "rejected"};
        ArrayAdapter<String> verificationAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, verificationStatuses);
        verificationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editVerificationStatus.setAdapter(verificationAdapter);
    }

    private void populateUserData() {
        if (user == null) return;

        editUsername.setText(user.getUsername());
        editName.setText(user.getName());
        editEmail.setText(user.getEmail());
        editPhone.setText(user.getPhone());

        // Set role selection
        String[] roles = {"admin", "staff", "user", "parking_owner"};
        for (int i = 0; i < roles.length; i++) {
            if (roles[i].equals(user.getRole())) {
                editRole.setSelection(i);
                break;
            }
        }

        // Set verification status selection
        String[] verificationStatuses = {"pending", "verified", "rejected"};
        for (int i = 0; i < verificationStatuses.length; i++) {
            if (verificationStatuses[i].equals(user.getVerificationStatus())) {
                editVerificationStatus.setSelection(i);
                break;
            }
        }
    }

    private void updateUserData() {
        // Chỉ cập nhật verificationStatus
        user.setVerificationStatus(editVerificationStatus.getSelectedItem().toString());
    }
}
