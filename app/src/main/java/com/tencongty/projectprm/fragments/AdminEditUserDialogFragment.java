package com.tencongty.projectprm.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.User;

public class AdminEditUserDialogFragment extends DialogFragment {

    private final User user;
    private final OnUserUpdatedListener listener;

    public interface OnUserUpdatedListener {
        void onUserUpdated(User updatedUser);
    }

    public AdminEditUserDialogFragment(User user, OnUserUpdatedListener listener) {
        this.user = user;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.admin_dialog_edit_user, null);

        EditText name = view.findViewById(R.id.edit_name);
        EditText email = view.findViewById(R.id.edit_email);
        EditText phone = view.findViewById(R.id.edit_phone);

        name.setText(user.getName());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());

        return new AlertDialog.Builder(getContext())
                .setTitle("Chỉnh sửa người dùng")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    user.setName(name.getText().toString().trim());
                    user.setEmail(email.getText().toString().trim());
                    user.setPhone(phone.getText().toString().trim());
                    listener.onUserUpdated(user);
                })
                .setNegativeButton("Hủy", null)
                .create();
    }
}
