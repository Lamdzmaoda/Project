package com.example.appcodetest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;
import com.example.appcodetest.api.ApiService;
import com.example.appcodetest.api.AuthServiceHelper;
import com.example.appcodetest.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText edtFullName, edtEmail, edtPassword, edtConfirmPassword;
    Button btnRegister;
    TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // =========================
        // ÁNH XẠ VIEW
        // =========================

        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);

        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        // Khởi tạo Retrofit API
        ApiService apiService =
                RetrofitClient.getClient().create(ApiService.class);

        // =========================
        // 🔥 REGISTER
        // =========================

        btnRegister.setOnClickListener(v -> {

            String username =
                    edtFullName.getText()
                            .toString()
                            .trim();

            String email =
                    edtEmail.getText()
                            .toString()
                            .trim();

            String password =
                    edtPassword.getText()
                            .toString()
                            .trim();

            String confirmPassword =
                    edtConfirmPassword.getText()
                            .toString()
                            .trim();

            // check rỗng
            if (username.isEmpty()
                    || email.isEmpty()
                    || password.isEmpty()
                    || confirmPassword.isEmpty()) {

                Toast.makeText(
                        this,
                        "Nhập đầy đủ thông tin!",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // check confirm password
            if (!password.equals(confirmPassword)) {

                Toast.makeText(
                        this,
                        "Mật khẩu không khớp!",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // =========================
            // FIX:
            // AuthServiceHelper.register()
            // giờ trả về Call<Object>
            // không còn Call<ApiResponse<Object>>
            // =========================

            Call<Object> call =
                    AuthServiceHelper.register(
                            apiService,
                            email,
                            password,
                            username
                    );

            call.enqueue(new Callback<Object>() {

                @Override
                public void onResponse(
                        Call<Object> call,
                        Response<Object> response
                ) {

                    Log.d(
                            "REGISTER_DEBUG",
                            "HTTP CODE: " + response.code()
                    );

                    if (response.isSuccessful()) {

                        Toast.makeText(
                                RegisterActivity.this,
                                "Đăng ký thành công 🚀",
                                Toast.LENGTH_SHORT
                        ).show();

                        // quay về login
                        startActivity(
                                new Intent(
                                        RegisterActivity.this,
                                        LoginActivity.class
                                )
                        );

                        finish();

                    } else {

                        try {

                            if (response.errorBody() != null) {

                                String err =
                                        response.errorBody()
                                                .string();

                                Log.e(
                                        "REGISTER_ERROR",
                                        err
                                );
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(
                                RegisterActivity.this,
                                "Đăng ký thất bại!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }

                @Override
                public void onFailure(
                        Call<Object> call,
                        Throwable t
                ) {

                    t.printStackTrace();

                    Log.e(
                            "REGISTER_FAIL",
                            String.valueOf(
                                    t.getMessage()
                            )
                    );

                    Toast.makeText(
                            RegisterActivity.this,
                            "Không kết nối server!",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        });

        // =========================
        // QUAY VỀ LOGIN
        // =========================

        txtLogin.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            RegisterActivity.this,
                            LoginActivity.class
                    )
            );

            finish();
        });
    }
}