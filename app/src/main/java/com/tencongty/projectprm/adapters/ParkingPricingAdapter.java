package com.tencongty.projectprm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.ParkingPricing;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ParkingPricingAdapter extends RecyclerView.Adapter<ParkingPricingAdapter.PricingViewHolder> {

    private List<ParkingPricing> pricingList;

    public ParkingPricingAdapter(List<ParkingPricing> pricingList) {
        this.pricingList = pricingList;
    }

    @NonNull
    @Override
    public PricingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parking_pricing, parent, false);
        return new PricingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PricingViewHolder holder, int position) {
        ParkingPricing pricing = pricingList.get(position);

        // Format giá sang định dạng tiền tệ Việt Nam
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(pricing.getPrice());

        // Kết hợp với đơn vị (ví dụ: "/giờ", "/lượt")
        String priceText = formattedPrice + pricing.getUnit();

        holder.tvType.setText(pricing.getType());     // Loại xe: "Xe máy", "Ô tô", ...
        holder.tvPrice.setText(priceText);            // Ví dụ: "5.000 ₫/giờ"
    }

    @Override
    public int getItemCount() {
        return pricingList != null ? pricingList.size() : 0;
    }

    public static class PricingViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvPrice;

        public PricingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvPricingType);
            tvPrice = itemView.findViewById(R.id.tvPricingPrice);
        }
    }
}
