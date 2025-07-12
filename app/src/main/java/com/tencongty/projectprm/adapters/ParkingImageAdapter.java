package com.tencongty.projectprm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tencongty.projectprm.R;
// import com.squareup.picasso.Picasso; // Giả sử sử dụng Picasso hoặc Glide

import java.util.List;

public class ParkingImageAdapter extends RecyclerView.Adapter<ParkingImageAdapter.ImageViewHolder> {

    private List<String> imageUrls;

    // Constructor nhận danh sách URL hình ảnh
    public ParkingImageAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Bạn cần đảm bảo có layout item_parking_image.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parking_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String url = imageUrls.get(position);

        // Sử dụng thư viện tải ảnh (ví dụ: Picasso) để load ảnh từ URL
        // Picasso.get().load(url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            // Bạn cần ID của ImageView trong layout item_parking_image.xml (Ví dụ: R.id.imageViewParking)
            // imageView = itemView.findViewById(R.id.imageViewParking);
        }
    }
}