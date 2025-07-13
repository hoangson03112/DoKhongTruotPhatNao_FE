package com.tencongty.projectprm.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.Booking;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private final List<Booking> bookings;
    private final Context context;
    private final boolean allowCancel;
    private final OnBookingCancelledListener cancelListener;

    public BookingAdapter(Context context, List<Booking> bookings, boolean allowCancel, OnBookingCancelledListener cancelListener) {
        this.bookings = bookings;
        this.context = context;
        this.allowCancel = allowCancel;
        this.cancelListener = cancelListener;
    }

    public interface OnBookingCancelledListener {
        void onBookingCancelled();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvParkingLot, tvDate, tvStatus;
        Button btnCancel;
        ImageView btnDirection;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvParkingLot = itemView.findViewById(R.id.tvParkingLot);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnDirection = itemView.findViewById(R.id.btnDirection);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking b = bookings.get(position);
        holder.tvParkingLot.setText(b.parkingLot.getName());

        // Định dạng thời gian
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String formattedStartTime = outputFormat.format(inputFormat.parse(b.startTime));
            String formattedEndTime = outputFormat.format(inputFormat.parse(b.timeCheckOut));
            holder.tvDate.setText("Từ " + formattedStartTime + " đến " + formattedEndTime);
        } catch (Exception e) {
            holder.tvDate.setText("Từ " + b.startTime + " đến " + b.timeCheckOut);
        }

        holder.tvStatus.setText("Trạng thái: " + b.status);

        // Xử lý click btnDirection để mở Google Maps
        if (b.status.equals("pending") || b.status.equals("active")) {
            holder.btnDirection.setVisibility(View.VISIBLE);
            holder.btnDirection.setOnClickListener(v -> {
                try {
                    double lat = b.parkingLot.getLatitude();
                    double lng = b.parkingLot.getLongitude();
                    Log.d("BookingAdapter", "Lat: " + lat + ", Lng: " + lng); // Log tọa độ
                    if (lat == 0 || lng == 0 || lat > 90 || lat < -90 || lng > 180 || lng < -180) {
                        Toast.makeText(context, "Tọa độ không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Đảm bảo định dạng tọa độ với dấu chấm và phân tách bằng dấu phẩy
                    String uri = "google.navigation:q=" + lat + "," + lng;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    } else {
                        String url = "https://www.google.com/maps/dir/?api=1&destination=" + lat + "," + lng;
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(browserIntent);
                    }
                } catch (Exception e) {
                    Log.e("BookingAdapter", "Error opening Google Maps: " + e.getMessage());
                    Toast.makeText(context, "Không thể mở Google Maps", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.btnDirection.setVisibility(View.GONE);
        }

        if (allowCancel && b.status.equals("pending")) {
            holder.btnCancel.setVisibility(View.VISIBLE);
            holder.btnCancel.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Huỷ đặt chỗ")
                        .setMessage("Bạn có chắc chắn muốn huỷ đặt chỗ này?")
                        .setPositiveButton("Huỷ", (dialog, which) -> {
                            cancelBooking(b._id, position);
                        })
                        .setNegativeButton("Không", null)
                        .show();
            });
        } else {
            holder.btnCancel.setVisibility(View.GONE);
        }
    }

    private void cancelBooking(String id, int position) {
        ApiService service = ApiClient.getClient(context).create(ApiService.class);
        service.cancelBooking(id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Huỷ thành công", Toast.LENGTH_SHORT).show();
                    bookings.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, bookings.size());
                    if (cancelListener != null) {
                        cancelListener.onBookingCancelled();
                    }
                } else {
                    Toast.makeText(context, "Huỷ thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "Lỗi khi huỷ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }
}