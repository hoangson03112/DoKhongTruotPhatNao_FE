package com.tencongty.projectprm.activities.parkingowner;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.models.ParkingOwnerRegisterRequest;
import com.tencongty.projectprm.network.ApiClient;
import com.tencongty.projectprm.network.ApiService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingOwner_RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etFullName, etPhone;
    private TextView tvSelectedImages, tvBackToLogin;
    private MaterialButton btnRegister;
    private Button btnSelectImages;
    private List<Uri> selectedImageUris = new ArrayList<>();

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUris.clear();
                    Intent data = result.getData();
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            selectedImageUris.add(data.getClipData().getItemAt(i).getUri());
                        }
                    } else if (data.getData() != null) {
                        selectedImageUris.add(data.getData());
                    }
                    tvSelectedImages.setText("Đã chọn " + selectedImageUris.size() + " ảnh");
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.packing_owner_activity_register);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegisterOwner);
        btnSelectImages = findViewById(R.id.btnSelectImages);
        tvSelectedImages = findViewById(R.id.tvSelectedImageCount);
        tvBackToLogin = findViewById(R.id.tvLoginLink);

        btnSelectImages.setOnClickListener(v -> openImagePicker());
        btnRegister.setOnClickListener(v -> handleRegisterOwner());
        tvBackToLogin.setOnClickListener(v -> finish());


    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE); // giúp lọc ảnh mở được
        imagePickerLauncher.launch(Intent.createChooser(intent, "Chọn ảnh xác minh"));
    }

    private void handleRegisterOwner() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || selectedImageUris.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin và chọn ít nhất 1 ảnh xác minh", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> imageStrings = new ArrayList<>();
        for (Uri uri : selectedImageUris) {
            String fileName = getFileNameFromUri(uri);
            if (fileName != null) {
                saveImageToAppDirectory(uri, fileName); // sao chép ảnh
                imageStrings.add(fileName); // chỉ lưu tên
            }
        }

        ParkingOwnerRegisterRequest request = new ParkingOwnerRegisterRequest(
                username, email, password, name, phone, imageStrings
        );

        ApiService apiService = ApiClient.getClient(getApplicationContext()).create(ApiService.class);
        apiService.registerParkingOwner(request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject res = response.body();
                    if (res.has("success") && res.get("success").getAsBoolean()) {
                        Toast.makeText(ParkingOwner_RegisterActivity.this, res.get("message").getAsString(), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(ParkingOwner_RegisterActivity.this, res.get("message").getAsString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ParkingOwner_RegisterActivity.this, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ParkingOwner_RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }

        if (result == null) {
            // fallback if scheme is file://
            result = uri.getLastPathSegment();
            if (result != null && result.contains("/")) {
                result = result.substring(result.lastIndexOf('/') + 1);
            }
        }

        return result;
    }

    private void saveImageToAppDirectory(Uri uri, String fileName) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(new File(getFilesDir(), fileName))) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu ảnh " + fileName, Toast.LENGTH_SHORT).show();
        }
    }


}