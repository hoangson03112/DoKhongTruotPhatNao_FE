package com.tencongty.projectprm.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.fragments.AdminOwnerDetailActivity;
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

        TextView ownerName, ownerEmail, ownerVerificationStatus, ownerPhone, ownerUsername;
        Button btnEdit, btnDelete;

        public OwnerViewHolder(@NonNull View itemView) {
            super(itemView);
            ownerName = itemView.findViewById(R.id.owner_name);
            ownerUsername = itemView.findViewById(R.id.owner_username);
            ownerPhone = itemView.findViewById(R.id.owner_phone);
            ownerEmail = itemView.findViewById(R.id.owner_email);
            ownerVerificationStatus = itemView.findViewById(R.id.owner_status);
            btnEdit = itemView.findViewById(R.id.btn_edit_owner);
            btnDelete = itemView.findViewById(R.id.btn_delete_owner);
        }

        public void bind(Owner owner) {
            // Hiển thị thông tin có tiêu đề in đậm
            ownerName.setText(formatLabelText("Tên Chủ Bãi: ", owner.getName() != null ? owner.getName() : "Không tên"));
            ownerUsername.setText(formatLabelText("UserName: ", owner.getUsername() != null ? owner.getUsername() : "Không username"));
            ownerPhone.setText(formatLabelText("Số Điện Thoại: ", owner.getPhone() != null ? owner.getPhone() : "Không phone"));
            ownerEmail.setText(formatLabelText("Email: ", owner.getEmail() != null ? owner.getEmail() : "Không email"));

            // Trạng thái xác thực
            String statusText = getStatusText(owner.getVerificationStatus());
            ownerVerificationStatus.setText(formatLabelText("Xác thực: ", statusText));
            ownerVerificationStatus.setTextColor(getStatusColor(owner.getVerificationStatus()));

            // Nút sửa và xoá
            btnEdit.setOnClickListener(v -> listener.onUpdate(owner));
            btnDelete.setOnClickListener(v -> listener.onDelete(owner));

            // Mở chi tiết khi click
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, AdminOwnerDetailActivity.class);
                intent.putExtra("owner", owner); // Owner cần implement Serializable hoặc Parcelable
                context.startActivity(intent);
            });
        }

        // ======== Hàm hỗ trợ ========

        private SpannableStringBuilder formatLabelText(String label, String value) {
            SpannableStringBuilder builder = new SpannableStringBuilder(label + value);
            builder.setSpan(new StyleSpan(Typeface.BOLD), 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return builder;
        }

        private String getStatusText(String status) {
            if (status == null) return "Không xác định";
            switch (status) {
                case "pending":
                    return "Chờ duyệt";
                case "verified":
                    return "Đã duyệt";
                case "rejected":
                    return "Từ chối";
                default:
                    return status;
            }
        }

        private int getStatusColor(String status) {
            switch (status) {
                case "pending":
                    return Color.parseColor("#FF9800"); // Cam
                case "verified":
                    return Color.parseColor("#4CAF50"); // Xanh lá
                case "rejected":
                    return Color.parseColor("#F44336"); // Đỏ
                default:
                    return Color.GRAY;
            }
        }
    }
}
