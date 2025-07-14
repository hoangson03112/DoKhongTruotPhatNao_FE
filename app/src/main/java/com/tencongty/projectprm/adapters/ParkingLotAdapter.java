package com.tencongty.projectprm.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.ParkingLot;
import com.tencongty.projectprm.utils.LocationHelper;

import java.util.List;

// Adapter để hiển thị danh sách các bãi đỗ xe trong RecyclerView
public class ParkingLotAdapter extends RecyclerView.Adapter<ParkingLotAdapter.ParkingLotViewHolder> {

    public interface OnParkingLotClickListener {
        void onParkingLotClick(ParkingLot parkingLot);
    }

    private List<ParkingLot> parkingLots;
    private double userLat;
    private double userLng;
    private OnParkingLotClickListener listener;

    // Constructor cập nhật để nhận listener
    public ParkingLotAdapter(List<ParkingLot> parkingLots, double userLat, double userLng, OnParkingLotClickListener listener) {
        this.parkingLots = parkingLots;
        this.userLat = userLat;
        this.userLng = userLng;
        this.listener = listener;
    }

    // Constructor không có listener (backward compatibility)
    public ParkingLotAdapter(List<ParkingLot> parkingLots, double userLat, double userLng) {
        this.parkingLots = parkingLots;
        this.userLat = userLat;
        this.userLng = userLng;
        this.listener = null;
    }

    // Phương thức để set listener sau khi khởi tạo
    public void setOnParkingLotClickListener(OnParkingLotClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ParkingLotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parking_lot_modern, parent, false);
        return new ParkingLotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingLotViewHolder holder, int position) {
        ParkingLot lot = parkingLots.get(position);

        holder.tvName.setText(lot.getName());
        holder.tvAddress.setText(lot.getAddress());

        double distance = LocationHelper.calculateDistance(userLat, userLng, lot.getLatitude(), lot.getLongitude());
        holder.tvDistance.setText(String.format("Cách bạn: %.1f km", distance));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onParkingLotClick(lot); // Gọi callback
            }
        });
    }

    @Override
    public int getItemCount() {
        return parkingLots.size();
    }

    public static class ParkingLotViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvDistance;

        public ParkingLotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDistance = itemView.findViewById(R.id.tvDistance);
        }
    }
}