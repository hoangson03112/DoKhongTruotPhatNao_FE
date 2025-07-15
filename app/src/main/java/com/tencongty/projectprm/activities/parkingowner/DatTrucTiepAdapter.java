package com.tencongty.projectprm.activities.parkingowner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.ParkingLotOwner;

import java.util.List;

public class DatTrucTiepAdapter extends RecyclerView.Adapter<DatTrucTiepAdapter.ViewHolder> {

    private final List<ParkingLotOwner> parkingLots;
    private final OnParkingLotClickListener listener;

    public interface OnParkingLotClickListener {
        void onAddBooking(ParkingLotOwner parkingLot);
        void onDeleteBooking(ParkingLotOwner parkingLot);
    }

    public DatTrucTiepAdapter(List<ParkingLotOwner> parkingLots, OnParkingLotClickListener listener) {
        this.parkingLots = parkingLots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parking_lot_add_bk, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParkingLotOwner lot = parkingLots.get(position);

        holder.tvName.setText(lot.getName());
        holder.tvAddress.setText(lot.getAddress());
        holder.tvCapacity.setText("Sức chứa: " + lot.getCapacity());

        // Load ảnh nếu có
        List<String> images = lot.getImages();
        if (images != null && !images.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(images.get(0))
                    .placeholder(R.drawable.baseline_image_24)
                    .error(R.drawable.baseline_image_not_supported_24)
                    .into(holder.imgParking);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.baseline_image_not_supported_24)
                    .into(holder.imgParking);
        }

        // Bắt sự kiện nút
        holder.btnAddBooking.setOnClickListener(v -> {
            if (listener != null) listener.onAddBooking(lot);
        });

        holder.btnDeleteBooking.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteBooking(lot);
        });
    }

    @Override
    public int getItemCount() {
        return parkingLots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvCapacity;
        ImageView imgParking;
        Button btnAddBooking, btnDeleteBooking;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            imgParking = itemView.findViewById(R.id.imgParking);
            btnAddBooking = itemView.findViewById(R.id.btnAddBooking);
            btnDeleteBooking = itemView.findViewById(R.id.btnDeleteBooking);
        }
    }
}
