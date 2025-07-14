package com.tencongty.projectprm.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.Owner;

import java.util.ArrayList;
import java.util.List;

public class OwnerAdapter extends RecyclerView.Adapter<OwnerAdapter.OwnerViewHolder> {

    private Context context;
    private List<Owner> ownerList = new ArrayList<>();
    private OnOwnerActionListener listener;

    public interface OnOwnerActionListener {
        void onDelete(Owner owner);
        void onUpdate(Owner owner);
    }

    public OwnerAdapter(Context context, OnOwnerActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setOwnerList(List<Owner> owners) {
        this.ownerList = owners != null ? owners : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OwnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_owner, parent, false);
        return new OwnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerViewHolder holder, int position) {
        holder.bind(ownerList.get(position));
    }

    @Override
    public int getItemCount() {
        return ownerList.size();
    }

    class OwnerViewHolder extends RecyclerView.ViewHolder {
        TextView ownerName, ownerEmail, ownerVerificationStatus;
        Button btnEdit, btnDelete;

        public OwnerViewHolder(@NonNull View itemView) {
            super(itemView);
            ownerName = itemView.findViewById(R.id.owner_name);
            ownerEmail = itemView.findViewById(R.id.owner_email);
            ownerVerificationStatus = itemView.findViewById(R.id.owner_status); // Đã sửa lại ID thành owner_status
            btnEdit = itemView.findViewById(R.id.btn_edit_owner);
            btnDelete = itemView.findViewById(R.id.btn_delete_owner);
        }

        public void bind(Owner owner) {
            ownerName.setText(owner.getName() != null ? owner.getName() : "Không tên");
            ownerEmail.setText(owner.getEmail() != null ? owner.getEmail() : "Không email");
            ownerVerificationStatus.setText("Xác thực: " + getStatusText(owner.getVerificationStatus()));
            ownerVerificationStatus.setTextColor(getStatusColor(owner.getVerificationStatus()));

            btnEdit.setOnClickListener(v -> listener.onUpdate(owner));
            btnDelete.setOnClickListener(v -> listener.onDelete(owner));
        }

        private String getStatusText(String status) {
            if (status == null) return "Không xác định";
            switch (status) {
                case "pending": return "Chờ duyệt";
                case "verified": return "Đã duyệt";
                case "rejected": return "Từ chối";
                default: return status;
            }
        }

        private int getStatusColor(String status) {
            switch (status) {
                case "pending": return Color.parseColor("#FF9800");
                case "verified": return Color.parseColor("#4CAF50");
                case "rejected": return Color.parseColor("#F44336");
                default: return Color.GRAY;
            }
        }
    }
}
