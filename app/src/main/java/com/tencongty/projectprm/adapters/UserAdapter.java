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
import com.tencongty.projectprm.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

        private Context context;
        private List<User> userList = new ArrayList<>();
        private OnUserActionListener listener;

        public interface OnUserActionListener {
            void onDelete(User user);
            void onUpdate(User user);
        }

        public UserAdapter(Context context, OnUserActionListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public void setUserList(List<User> userList) {
            this.userList = new ArrayList<>();
            if (userList != null) {
                for (User user : userList) {
                    if (!Boolean.TRUE.equals(user.isDeleted())) {
                        this.userList.add(user);
                    }
                }
            }
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_admin_user, parent, false);
            return new UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            User user = userList.get(position);
            holder.bind(user);
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }

        class UserViewHolder extends RecyclerView.ViewHolder {
            private TextView userName, userUsername, userEmail, userRole, userPhone, userLastLogin, userVerificationStatus;
            private Button btnEdit, btnDelete;

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.user_name);
                userUsername = itemView.findViewById(R.id.user_username);
                userEmail = itemView.findViewById(R.id.user_email);
                userRole = itemView.findViewById(R.id.user_role);
                userPhone = itemView.findViewById(R.id.user_phone);
                userLastLogin = itemView.findViewById(R.id.user_last_login);
                userVerificationStatus = itemView.findViewById(R.id.user_verification_status);
                btnEdit = itemView.findViewById(R.id.btn_edit);
                btnDelete = itemView.findViewById(R.id.btn_delete);
            }

            public void bind(User user) {
                // Basic info
                userName.setText(user.getName() != null ? user.getName() : "Không có tên");
                userUsername.setText("@" + (user.getUsername() != null ? user.getUsername() : "unknown"));
                userEmail.setText(user.getEmail() != null ? user.getEmail() : "Không có email");

                // Role with color
                String role = user.getRole() != null ? user.getRole() : "unknown";
                userRole.setText(getRoleDisplayName(role));
                userRole.setBackgroundColor(getRoleColor(role));

                // Phone
                userPhone.setText(user.getPhone() != null ? user.getPhone() : "Chưa cập nhật");

                // Last login
                String lastLogin = formatDate(user.getLastLogin());
                userLastLogin.setText(lastLogin);

                // Verification status (only for parking_owner)
                if ("parking_owner".equals(user.getRole())) {
                    userVerificationStatus.setVisibility(View.VISIBLE);
                    String status = user.getVerificationStatus();
                    userVerificationStatus.setText("Xác thực: " + getVerificationStatusText(status));
                    userVerificationStatus.setTextColor(getVerificationStatusColor(status));
                } else {
                    userVerificationStatus.setVisibility(View.GONE);
                }

                // Button actions
                btnEdit.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onUpdate(user);
                    }
                });

                btnDelete.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onDelete(user);
                    }
                });
            }

            private String getRoleDisplayName(String role) {
                switch (role) {
                    case "admin": return "Quản trị viên";
                    case "staff": return "Nhân viên";
                    case "user": return "Người dùng";
                    case "parking_owner": return "Chủ bãi xe";
                    default: return role;
                }
            }

            private int getRoleColor(String role) {
                switch (role) {
                    case "admin": return Color.parseColor("#f44336"); // Red
                    case "staff": return Color.parseColor("#ff9800"); // Orange
                    case "user": return Color.parseColor("#4caf50"); // Green
                    case "parking_owner": return Color.parseColor("#2196f3"); // Blue
                    default: return Color.parseColor("#757575"); // Gray
                }
            }

            private String getVerificationStatusText(String status) {
                if (status == null) return "Chưa xác định";
                switch (status) {
                    case "pending": return "Chờ duyệt";
                    case "verified": return "Đã duyệt";
                    case "rejected": return "Từ chối";
                    default: return status;
                }
            }

            private int getVerificationStatusColor(String status) {
                if (status == null) return Color.GRAY;
                switch (status) {
                    case "pending": return Color.parseColor("#ff9800"); // Orange
                    case "approved": return Color.parseColor("#4caf50"); // Green
                    case "rejected": return Color.parseColor("#f44336"); // Red
                    case "not_applicable": return Color.parseColor("#757575"); // Gray
                    default: return Color.GRAY;
                }
            }

            private String formatDate(String dateString) {
                if (dateString == null) return "Chưa từng đăng nhập";

                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    Date date = inputFormat.parse(dateString);
                    return outputFormat.format(date);
                } catch (Exception e) {
                    return "Không xác định";
                }
            }
        }
    }