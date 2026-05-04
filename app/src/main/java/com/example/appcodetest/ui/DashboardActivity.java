package com.example.appcodetest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;
import com.example.appcodetest.api.ApiService;
import com.example.appcodetest.api.RetrofitClient;
import com.example.appcodetest.model.ApiResponse;
import com.example.appcodetest.model.UserResponse;
import com.example.appcodetest.utils.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    TextView txtWelcome, txtId, txtUsername, txtEmail, txtBirth;
    Button btnContinue;

    // 🔥 kiểm tra user có phải admin không
    boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Ánh xạ view từ XML
        txtWelcome = findViewById(R.id.txtWelcome);
        txtId = findViewById(R.id.txtId);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtBirth = findViewById(R.id.txtBirth);
        btnContinue = findViewById(R.id.btnContinue);

        // Ban đầu chưa cho bấm tiếp tục
        btnContinue.setEnabled(false);

        // 🔥 Lấy token đã lưu
        String rawToken = Prefs.getToken(this);

        // 🔥 Kiểm tra token
        // nếu chưa đăng nhập thì quay về Login
        if (rawToken == null || rawToken.isEmpty()) {
            Toast.makeText(this, "Chưa đăng nhập!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Gọi API lấy thông tin user
        callApi("Bearer " + rawToken);

        btnContinue.setOnClickListener(v -> {

            // Nếu là ADMIN → vào Admin Panel
            if (isAdmin) {
                startActivity(new Intent(this, AdminActivity.class));
            }

            // Nếu là USER thường → vào MainActivity
            else {
                startActivity(new Intent(this, MainActivity.class));
            }

            finish();
        });
    }

    private void callApi(String token) {

        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        Log.d("TOKEN_DEBUG", token);

        // 🔥 API lấy thông tin user hiện tại
        api.getMyInfo(token).enqueue(new Callback<ApiResponse<UserResponse>>() {

            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call,
                                   Response<ApiResponse<UserResponse>> response) {

                // Thành công + có dữ liệu
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().result != null) {

                    UserResponse user = response.body().result;

                    // Hiển thị thông tin user
                    txtWelcome.setText("Welcome back, " + user.username + "!");
                    txtId.setText(user.id != null ? user.id : "N/A");
                    txtUsername.setText(user.username != null ? user.username : "N/A");
                    txtEmail.setText(user.email != null ? user.email : "N/A");
                    txtBirth.setText(user.birthDate != null ? user.birthDate : "N/A");

                    // Mặc định là user thường
                    isAdmin = false;

                    // 🔥 Kiểm tra role ADMIN
                    if (user.roles != null) {
                        for (UserResponse.Role role : user.roles) {
                            if ("ADMIN".equals(role.name)) {
                                isAdmin = true;
                                break;
                            }
                        }
                    }

                    // Cho phép bấm tiếp tục
                    btnContinue.setEnabled(true);

                } else {

                    // Token lỗi / hết hạn
                    Toast.makeText(DashboardActivity.this,
                            "Token lỗi → đăng nhập lại",
                            Toast.LENGTH_LONG).show();

                    // Xóa token cũ
                    Prefs.clearToken(DashboardActivity.this);

                    // Quay về Login
                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {

                // Lỗi mạng / không kết nối server
                Toast.makeText(DashboardActivity.this,
                        "Lỗi mạng!",
                        Toast.LENGTH_SHORT).show();

                // vẫn cho bấm để tránh bị kẹt app
                btnContinue.setEnabled(true);
            }
        });
    }
}