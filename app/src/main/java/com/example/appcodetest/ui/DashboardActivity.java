package com.example.appcodetest.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;
import com.example.appcodetest.api.ApiService;
import com.example.appcodetest.api.RetrofitClient;
import com.example.appcodetest.model.ApiResponse;
import com.example.appcodetest.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    TextView txtWelcome, txtId, txtUsername, txtEmail, txtBirth;
    Button btnContinue;

    boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtWelcome = findViewById(R.id.txtWelcome);
        txtId = findViewById(R.id.txtId);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtBirth = findViewById(R.id.txtBirth);
        btnContinue = findViewById(R.id.btnContinue);

        // 🔒 khóa nút trước
        btnContinue.setEnabled(false);

        // =========================
        // 🔥 FIX TOKEN (KEY + CHECK)
        // =========================
        SharedPreferences prefs = getSharedPreferences("APP", MODE_PRIVATE);
        String token = prefs.getString("token", ""); // ✅ FIX: token (không phải TOKEN)

        if (token.isEmpty()) {
            Toast.makeText(this, "Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        callApi(token);

        btnContinue.setOnClickListener(v -> {

            if (isAdmin) {
                startActivity(new Intent(DashboardActivity.this, AdminActivity.class));
            } else {
                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            }

            finish();
        });
    }

    private void callApi(String token) {

        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        // 🔥 FIX QUAN TRỌNG NHẤT
        String auth = "Bearer " + token;

        Log.d("TOKEN_DEBUG", auth);

        api.getMyInfo(auth).enqueue(new Callback<ApiResponse<UserResponse>>() {

            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call,
                                   Response<ApiResponse<UserResponse>> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().result != null) {

                    UserResponse user = response.body().result;

                    txtWelcome.setText("Welcome back, " + user.username + "!");
                    txtId.setText(user.id != null ? user.id : "N/A");
                    txtUsername.setText(user.username != null ? user.username : "N/A");
                    txtEmail.setText(user.email != null ? user.email : "N/A");
                    txtBirth.setText(user.birthDate != null ? user.birthDate : "N/A");

                    // =========================
                    // 🔥 CHECK ROLE
                    // =========================
                    isAdmin = false;

                    if (user.roles != null) {
                        for (UserResponse.Role role : user.roles) {
                            if ("ADMIN".equals(role.name)) {
                                isAdmin = true;
                                break;
                            }
                        }
                    }

                    btnContinue.setEnabled(true);

                } else {

                    Log.e("API_ERROR", "Response lỗi: " + response.code());

                    Toast.makeText(DashboardActivity.this,
                            "Không lấy được user! (Token có thể sai)",
                            Toast.LENGTH_LONG).show();

                    btnContinue.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {

                t.printStackTrace();

                Toast.makeText(DashboardActivity.this,
                        "Lỗi API: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();

                btnContinue.setEnabled(true);
            }
        });
    }
}