package com.tencongty.projectprm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.ParkingPricing;

import java.util.List;

public class ParkingPricingAdapter extends RecyclerView.Adapter<ParkingPricingAdapter.PricingViewHolder> {

    private List<ParkingPricing> pricingList;

    public ParkingPricingAdapter(List<ParkingPricing> pricingList) {
        this.pricingList = pricingList;
    }

    @NonNull
    @Override
    public PricingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Giả sử bạn có layout item_parking_pricing.xml để hiển thị thông tin giá
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parking_pricing, parent, false);
        return new PricingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PricingViewHolder holder, int position) {
        ParkingPricing pricing = pricingList.get(position);

        // Format giá và đơn vị
        String priceText = String.format("%,.0f %s", pricing.getPrice(), pricing.getUnit());

        holder.tvType.setText(pricing.getType());
        holder.tvPrice.setText(priceText);
    }

    @Override
    public int getItemCount() {
        return pricingList.size();
    }

    public static class PricingViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvPrice;

        public PricingViewHolder(@NonNull View itemView) {
            super(itemView);
            // Bạn cần ID của TextView trong layout item_parking_pricing.xml
            // tvType = itemView.findViewById(R.id.tvPricingType);
            // tvPrice = itemView.findViewById(R.id.tvPricingPrice);
        }
    }
}