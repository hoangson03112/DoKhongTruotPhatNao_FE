package com.tencongty.projectprm.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

// Lớp trợ giúp để lấy vị trí hiện tại của người dùng thông qua FusedLocationProvider
public class LocationHelper {

    // Tên interface đổi lại để tránh trùng với Google LocationCallback
    public interface MyLocationCallback {
        void onLocationReceived(Location location);
    }

    // Phương thức tính khoảng cách sử dụng đối tượng Location (khuyến khích)
    // Trả về đơn vị km
    public static double calculateDistance(double startLat, double startLng, double endLat, double endLng) {
        Location start = new Location("start");
        start.setLatitude(startLat);
        start.setLongitude(startLng);

        Location end = new Location("end");
        end.setLatitude(endLat);
        end.setLongitude(endLng);

        float distanceInMeters = start.distanceTo(end);
        return distanceInMeters / 1000.0; // Trả về đơn vị km
    }

    // Phương thức lấy vị trí hiện tại của người dùng
    public static void getCurrentLocation(Activity activity, MyLocationCallback callback) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền nếu chưa có
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(activity);

        // Tạo LocationRequest để yêu cầu vị trí chính xác cao
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(3000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(10000);

        // Yêu cầu cập nhật vị trí
        fusedLocationClient.requestLocationUpdates(locationRequest,
                new com.google.android.gms.location.LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult result) {
                        // Gỡ bỏ cập nhật sau khi nhận được kết quả
                        fusedLocationClient.removeLocationUpdates(this);
                        if (result != null && !result.getLocations().isEmpty()) {
                            // Trả về vị trí cuối cùng
                            callback.onLocationReceived(result.getLastLocation());
                        } else {
                            // Không thể lấy vị trí
                            callback.onLocationReceived(null);
                        }
                    }
                }, Looper.getMainLooper());
    }
}