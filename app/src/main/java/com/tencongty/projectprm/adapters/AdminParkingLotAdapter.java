package com.tencongty.projectprm.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.AdminParkingLot;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminParkingLotAdapter extends RecyclerView.Adapter<AdminParkingLotAdapter.ViewHolder> {

    private Context context;
    private List<AdminParkingLot> parkingLots;
    private ApiService apiService;
    private Runnable reloadCallback;

    public AdminParkingLotAdapter(Context context, List<AdminParkingLot> parkingLots, Runnable reloadCallback) {
        this.context = context;
        this.parkingLots = parkingLots;
        this.reloadCallback = reloadCallback;
        this.apiService = ApiClient.getClient(context).create(ApiService.class);
    }

    @NonNull
    @Override
    public AdminParkingLotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_parking_lot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminParkingLotAdapter.ViewHolder holder, int position) {
        AdminParkingLot lot = parkingLots.get(position);

        holder.tvName.setText(lot.getName());
        holder.tvAddress.setText(lot.getAddress());
        holder.tvCapacity.setText("Sức chứa: " + lot.getCapacity());
        holder.tvAvailable.setText("Còn trống: " + lot.getAvailableSlots());
        holder.tvStatus.setText("Trạng thái: " + lot.getStatus());

        if (lot.getImages() != null && !lot.getImages().isEmpty()) {
            Glide.with(context)
                    .load(lot.getImages().get(0))
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder_parking);
        }

        holder.statusSwitch.setChecked("active".equals(lot.getStatus()));

        holder.statusSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateStatus(lot.getId(), isChecked ? "active" : "inactive");
        });

        holder.btnDelete.setOnClickListener(v -> confirmDelete(lot.getId()));

        holder.itemView.setOnClickListener(v -> {
            if (lot.getCoordinates() != null) {
                double lat = lot.getCoordinates().getLat();
                double lng = lot.getCoordinates().getLng();
                String uri = "geo:" + lat + "," + lng + "?q=" + lat + "," + lng + "(" + lot.getName() + ")";
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            } else {
                Toast.makeText(context, "Không có tọa độ cho bãi đỗ này", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateStatus(String id, String newStatus) {
        Map<String, String> body = new HashMap<>();
        body.put("status", newStatus);

        apiService.updateParkingLotStatus(id, body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "✅ Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                    if (reloadCallback != null) reloadCallback.run();
                } else {
                    Toast.makeText(context, "❌ Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(context, "❌ Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDelete(String id) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa bãi đỗ này?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteParkingLot(id))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteParkingLot(String id) {
        apiService.deleteParkingLot(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "🗑️ Đã xóa bãi đỗ", Toast.LENGTH_SHORT).show();
                    if (reloadCallback != null) reloadCallback.run();
                } else {
                    Toast.makeText(context, "❌ Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "❌ Lỗi khi xóa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return parkingLots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvCapacity, tvAvailable, tvStatus;
        ImageView imageView;
        Switch statusSwitch;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvParkingName);
            tvAddress = itemView.findViewById(R.id.tvParkingAddress);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            tvAvailable = itemView.findViewById(R.id.tvAvailableSlots);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            imageView = itemView.findViewById(R.id.ivParkingImage);
            statusSwitch = itemView.findViewById(R.id.switchStatus);
            btnDelete = itemView.findViewById(R.id.btnDeleteParking);

        }
    }
}