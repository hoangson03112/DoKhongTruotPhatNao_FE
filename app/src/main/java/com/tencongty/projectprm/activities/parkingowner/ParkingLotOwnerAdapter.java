package com.tencongty.projectprm.activities.parkingowner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.ParkingLotOwner;

import java.util.List;

public class ParkingLotOwnerAdapter extends RecyclerView.Adapter<ParkingLotOwnerAdapter.ViewHolder> {

    private final List<ParkingLotOwner> parkingLots;
    private final OnParkingLotClickListener listener;

    // Interface để bắt sự kiện click nút
    public interface OnParkingLotClickListener {
        void onClick(ParkingLotOwner parkingLot);
    }

    public ParkingLotOwnerAdapter(List<ParkingLotOwner> parkingLots, OnParkingLotClickListener listener) {
        this.parkingLots = parkingLots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parking_lot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParkingLotOwner lot = parkingLots.get(position);

        holder.tvName.setText(lot.getName());
        holder.tvAddress.setText(lot.getAddress());
        holder.tvCapacity.setText("Sức chứa: " + lot.getCapacity());
        holder.btnViewSlots.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(lot);
            }
        });
    }

    @Override
    public int getItemCount() {
        return parkingLots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvCapacity;
        private ImageButton btnViewSlots;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            btnViewSlots = itemView.findViewById(R.id.btnViewSlots);
        }
    }
}
