package com.tencongty.projectprm.activities.parkingowner;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.Reservation;

import java.util.List;

public class ParkingSlotAdapter extends RecyclerView.Adapter<ParkingSlotAdapter.ViewHolder> {

    private final Context context;
    private final List<Reservation> reservations;
    private final int capacity;

    public ParkingSlotAdapter(Context context, List<Reservation> reservations, int capacity) {
        this.context = context;
        this.reservations = reservations;
        this.capacity = capacity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_parking_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int slotNumber = position + 1;
        holder.tvSlotNumber.setText("Slot " + slotNumber);

        // Suy ra số slot đã đặt
        int reservedCount = reservations.size();

        if (position < reservedCount) {
            Reservation reservation = reservations.get(position);
            holder.itemView.setBackgroundColor(Color.parseColor("#A5D6A7")); // Màu xanh

            holder.itemView.setOnClickListener(v -> showReservationDialog(reservation));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#EEEEEE")); // Màu xám nhạt
            holder.itemView.setOnClickListener(null);
        }
    }

    private void showReservationDialog(Reservation r) {
        new AlertDialog.Builder(context)
                .setTitle("Thông tin đặt chỗ")
                .setMessage(
                        "Biển số xe: " + r.getLicensePlate() + "\n" +
                                "Người đặt: " + r.getUser().getName() + "\n" +
                                "Email: " + r.getUser().getEmail() + "\n" +
                                "SĐT: " + r.getUser().getPhone() + "\n" +
                                "Thời gian: " + r.getStartTime() + " - " + r.getTimeCheckOut() + "\n" +
                                "Trạng thái: " + r.getStatus()
                )
                .setPositiveButton("Đóng", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return capacity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSlotNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSlotNumber = itemView.findViewById(R.id.tvSlotNumber);
        }
    }
}
