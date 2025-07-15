package com.tencongty.projectprm.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.Owner;

public class AdminEditOwnerDialogFragment extends DialogFragment {

    private Owner owner;
    private OnOwnerUpdateListener listener;
    private Spinner verificationSpinner;

    public interface OnOwnerUpdateListener {
        void onOwnerUpdate(Owner updated);
    }

    public AdminEditOwnerDialogFragment(Owner owner, OnOwnerUpdateListener listener) {
        this.owner = owner;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.admin_dialog_edit_owner_status, null);

        verificationSpinner = view.findViewById(R.id.spinner_verification_status);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                new String[]{"pending", "verified", "rejected"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        verificationSpinner.setAdapter(adapter);

        int selectedIndex = 0;
        if (owner.getVerificationStatus() != null) {
            switch (owner.getVerificationStatus()) {
                case "verified": selectedIndex = 1; break;
                case "rejected": selectedIndex = 2; break;
            }
        }
        verificationSpinner.setSelection(selectedIndex);

        builder.setView(view)
                .setTitle("Cập nhật xác thực chủ bãi xe")
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String selectedStatus = verificationSpinner.getSelectedItem().toString();
                    owner.setVerificationStatus(selectedStatus);
                    if (listener != null) listener.onOwnerUpdate(owner);
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        return builder.create();
    }
}
