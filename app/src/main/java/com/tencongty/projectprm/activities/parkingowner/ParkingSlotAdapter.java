package com.tencongty.projectprm.activities.parkingowner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tencongty.projectprm.R;

import java.util.List;

public class ParkingSlotAdapter extends RecyclerView.Adapter<ParkingSlotAdapter.ViewHolder> {

    private final List<String> slotStatusList;

    public ParkingSlotAdapter(List<String> slotStatusList) {
        this.slotStatusList = slotStatusList;
    }

    @NonNull
    @Override
    public ParkingSlotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingSlotAdapter.ViewHolder holder, int position) {
        holder.textView.setText(slotStatusList.get(position));
    }

    @Override
    public int getItemCount() {
        return slotStatusList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1); // dùng layout mặc định
        }
    }
}
