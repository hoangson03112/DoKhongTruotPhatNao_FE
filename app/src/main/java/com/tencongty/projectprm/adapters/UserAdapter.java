package com.tencongty.projectprm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList = new ArrayList<>();
    private List<User> fullList = new ArrayList<>();
    private final Context context;
    private final OnUserActionListener listener;

    public interface OnUserActionListener {
        void onDelete(User user);
        void onUpdate(User user);
    }

    public UserAdapter(Context context, OnUserActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setUserList(List<User> list) {
        this.fullList = list;
        this.userList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    public void filter(String query, String role) {
        userList = new ArrayList<>();
        for (User user : fullList) {
            boolean matchesQuery = user.getName().toLowerCase().contains(query.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(query.toLowerCase());
            boolean matchesRole = role.equals("Tất cả") || user.getRole().equals(role);
            if (matchesQuery && matchesRole) {
                userList.add(user);
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
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.role.setText(user.getRole());

        holder.btnDelete.setOnClickListener(v -> listener.onDelete(user));
        holder.btnEdit.setOnClickListener(v -> listener.onUpdate(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, role;
        ImageButton btnDelete, btnEdit;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            email = itemView.findViewById(R.id.user_email);
            role = itemView.findViewById(R.id.user_role);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnEdit = itemView.findViewById(R.id.btn_edit);
        }
    }
}