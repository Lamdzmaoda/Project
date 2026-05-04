package com.example.appcodetest.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;
import com.example.appcodetest.api.ApiService;
import com.example.appcodetest.api.RetrofitClient;
import com.example.appcodetest.utils.Prefs;

import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.*;

public class ProfileActivity extends AppCompatActivity {

    // request code chọn ảnh
    private static final int PICK_IMAGE = 1;

    ImageView imgAvatar;
    EditText edtName;
    Button btnSave, btnLogout;

    Uri imageUri;

    ApiService api;

    // token gọi API
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // =========================
        // ÁNH XẠ VIEW
        // =========================

        imgAvatar = findViewById(R.id.imgAvatar);
        edtName = findViewById(R.id.edtName);

        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);

        api = RetrofitClient.getClient().create(ApiService.class);

        // =========================
        // CHECK TOKEN
        // =========================

        // lấy token đã login trước đó
        String rawToken = Prefs.getToken(this);

        // nếu chưa login thì quay về Login
        if (rawToken == null || rawToken.isEmpty()) {

            Toast.makeText(this,
                    "Chưa đăng nhập!",
                    Toast.LENGTH_SHORT).show();

            startActivity(new Intent(
                    this,
                    LoginActivity.class
            ));

            finish();
            return;
        }

        token = "Bearer " + rawToken;

        // =========================
        // LOAD DỮ LIỆU LOCAL
        // =========================

        // load tên đã lưu local
        edtName.setText(
                getSharedPreferences("APP", MODE_PRIVATE)
                        .getString("NAME", "")
        );

        // load avatar local
        String avatar = getSharedPreferences("APP", MODE_PRIVATE)
                .getString("AVATAR", null);

        if (avatar != null) {
            imgAvatar.setImageURI(Uri.parse(avatar));
        }

        // =========================
        // CHỌN ẢNH AVATAR
        // =========================

        imgAvatar.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");

            startActivityForResult(intent, PICK_IMAGE);
        });

        // =========================
        // CẬP NHẬT TÊN
        // =========================

        btnSave.setOnClickListener(v -> {

            String newName =
                    edtName.getText().toString().trim();

            // không cho để trống
            if (newName.isEmpty()) {

                edtName.setError("Nhập tên đi bro");
                return;
            }

            // lưu local trước
            getSharedPreferences("APP", MODE_PRIVATE)
                    .edit()
                    .putString("NAME", newName)
                    .apply();

            // gọi API update profile
            api.updateProfile(token, newName)
                    .enqueue(new Callback<Void>() {

                        @Override
                        public void onResponse(Call<Void> call,
                                               Response<Void> response) {

                            Toast.makeText(
                                    ProfileActivity.this,
                                    "Update OK",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                        @Override
                        public void onFailure(Call<Void> call,
                                              Throwable t) {

                            t.printStackTrace();
                        }
                    });
        });

        // =========================
        // LOGOUT
        // =========================

        btnLogout.setOnClickListener(v -> {

            // xóa token đăng nhập
            Prefs.clearToken(this);

            // quay về màn Login
            startActivity(new Intent(
                    ProfileActivity.this,
                    LoginActivity.class
            ));

            finish();
        });
    }

    // =========================
    // NHẬN ẢNH TỪ GALLERY
    // =========================

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == PICK_IMAGE
                && resultCode == RESULT_OK
                && data != null) {

            imageUri = data.getData();

            if (imageUri != null) {

                // hiển thị avatar mới
                imgAvatar.setImageURI(imageUri);

                // lưu local để lần sau mở lại vẫn còn
                getSharedPreferences("APP", MODE_PRIVATE)
                        .edit()
                        .putString("AVATAR", imageUri.toString())
                        .apply();

                // upload lên server
                uploadAvatar();
            }
        }
    }

    // =========================
    // UPLOAD AVATAR API
    // =========================

    private void uploadAvatar() {

        try {

            InputStream is =
                    getContentResolver()
                            .openInputStream(imageUri);

            byte[] bytes =
                    new byte[is.available()];

            is.read(bytes);

            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse("image/*"),
                            bytes
                    );

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData(
                            "file",
                            "avatar.jpg",
                            requestFile
                    );

            // gọi API upload avatar
            api.uploadAvatar(token, body)
                    .enqueue(new Callback<Void>() {

                        @Override
                        public void onResponse(Call<Void> call,
                                               Response<Void> response) {

                            Toast.makeText(
                                    ProfileActivity.this,
                                    "Upload avatar OK",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                        @Override
                        public void onFailure(Call<Void> call,
                                              Throwable t) {

                            t.printStackTrace();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}