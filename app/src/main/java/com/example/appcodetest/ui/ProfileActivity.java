package com.example.appcodetest.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;
import com.example.appcodetest.api.ApiService;
import com.example.appcodetest.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    ImageView imgAvatar;
    EditText edtName;
    Button btnSave, btnLogout;

    Uri imageUri;
    ApiService api;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgAvatar = findViewById(R.id.imgAvatar);
        edtName = findViewById(R.id.edtName);
        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);

        api = RetrofitClient.getClient().create(ApiService.class);

        SharedPreferences prefs = getSharedPreferences("APP", MODE_PRIVATE);

        // =========================
        // 🔥 FIX TOKEN
        // =========================
        String rawToken = prefs.getString("token", "");

        if (rawToken.isEmpty()) {
            Toast.makeText(this, "Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        token = "Bearer " + rawToken;

        // =========================
        // LOAD LOCAL
        // =========================
        String name = prefs.getString("NAME", "");
        String avatar = prefs.getString("AVATAR", null);

        edtName.setText(name);

        if (avatar != null) {
            imgAvatar.setImageURI(Uri.parse(avatar));
        }

        // =========================
        // CHỌN ẢNH
        // =========================
        imgAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });

        // =========================
        // SAVE PROFILE
        // =========================
        btnSave.setOnClickListener(v -> {

            String newName = edtName.getText().toString().trim();

            if (newName.isEmpty()) {
                edtName.setError("Nhập tên đi bro");
                return;
            }

            // 👉 lưu local
            prefs.edit().putString("NAME", newName).apply();

            // 🔥 FIX API CALL (có token)
            api.updateProfile(token, newName)
                    .enqueue(new Callback<Void>() {

                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                            if (response.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this,
                                        "Cập nhật tên OK", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this,
                                        "Update fail: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(ProfileActivity.this,
                                    "Lỗi update name", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // =========================
        // LOGOUT
        // =========================
        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });
    }

    // =========================
    // NHẬN ẢNH
    // =========================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();

            if (imageUri != null) {
                imgAvatar.setImageURI(imageUri);

                getSharedPreferences("APP", MODE_PRIVATE)
                        .edit()
                        .putString("AVATAR", imageUri.toString())
                        .apply();
            }
        }
    }
}