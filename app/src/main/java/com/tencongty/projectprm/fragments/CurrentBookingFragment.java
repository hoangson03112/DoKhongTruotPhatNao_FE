package com.tencongty.projectprm.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.adapters.BookingAdapter;
import com.tencongty.projectprm.models.Booking;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentBookingFragment extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadBookings();
        return view;
    }

    public void loadBookings() {
        ApiService service = ApiClient.getClient(getContext()).create(ApiService.class);
        service.getBookings().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    List<Booking> current = new ArrayList<>();
                    JsonArray data = response.body().getAsJsonArray("data");
                    Gson gson = new Gson();
                    for (JsonElement e : data) {
                        Booking b = gson.fromJson(e, Booking.class);
                        if (b.status.equals("pending") || b.status.equals("active")) {
                            current.add(b);
                        }
                    }
                    recyclerView.setAdapter(new BookingAdapter(getContext(), current, true, () -> loadBookings()));
                } else {
                    Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}