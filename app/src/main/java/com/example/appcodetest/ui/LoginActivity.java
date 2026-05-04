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
import com.example.appcodetest.model.AuthResponse;
import com.example.appcodetest.model.LoginRequest;
import com.example.appcodetest.utils.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin, btnFacebook, btnGoogle;
    TextView txtForgot, txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // =========================
        // 🔥 AUTO LOGIN
        // =========================

        String savedToken = Prefs.getToken(this);

        // FIX NULL + EMPTY
        if (savedToken != null && !savedToken.isEmpty()) {

            startActivity(new Intent(
                    LoginActivity.this,
                    DashboardActivity.class
            ));

            finish();
            return;
        }

        // =========================
        // ÁNH XẠ VIEW
        // =========================

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnGoogle = findViewById(R.id.btnGoogle);

        txtForgot = findViewById(R.id.txtForgot);
        txtRegister = findViewById(R.id.txtRegister);

        ApiService apiService =
                RetrofitClient.getClient().create(ApiService.class);

        // =========================
        // 🔥 LOGIN
        // =========================

        btnLogin.setOnClickListener(v -> {

            String username =
                    edtUsername.getText().toString().trim();

            String password =
                    edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                        this,
                        "Nhập đầy đủ thông tin!",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            LoginRequest request =
                    new LoginRequest(username, password);

            AuthServiceHelper.login(apiService, request)
                    .enqueue(new Callback<AuthResponse>() {

                        @Override
                        public void onResponse(
                                Call<AuthResponse> call,
                                Response<AuthResponse> response
                        ) {

                            if (response.isSuccessful()
                                    && response.body() != null
                                    && response.body().result != null
                                    && response.body().result.authenticated) {

                                String token =
                                        response.body().result.token;

                                Log.d("TOKEN_RAW", token);

                                // ====================================
                                // 🔥 FIX TOKEN INVALID QUAN TRỌNG NHẤT
                                // ====================================
                                // Backend thường trả:
                                // Bearer eyJhbGciOi...
                                //
                                // Nếu save nguyên như vậy,
                                // các màn khác lại cộng thêm:
                                // "Bearer " + token
                                //
                                // => thành:
                                // Bearer Bearer eyJ...
                                //
                                // => backend báo:
                                // Token invalid
                                // ====================================

                                if (token != null
                                        && token.startsWith("Bearer ")) {

                                    token = token.replace(
                                            "Bearer ",
                                            ""
                                    );
                                }

                                // chỉ save token gốc
                                Prefs.saveToken(
                                        LoginActivity.this,
                                        token
                                );

                                Toast.makeText(
                                        LoginActivity.this,
                                        "Login thành công 🚀",
                                        Toast.LENGTH_SHORT
                                ).show();

                                startActivity(new Intent(
                                        LoginActivity.this,
                                        DashboardActivity.class
                                ));

                                finish();

                            } else {

                                Toast.makeText(
                                        LoginActivity.this,
                                        "Sai tài khoản hoặc mật khẩu!",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }

                        @Override
                        public void onFailure(
                                Call<AuthResponse> call,
                                Throwable t
                        ) {

                            t.printStackTrace();

                            Log.e(
                                    "LOGIN_ERROR",
                                    String.valueOf(t.getMessage())
                            );

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Không kết nối server!",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
        });

        // =========================
        // CHỨC NĂNG PHỤ
        // =========================

        txtForgot.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Chức năng quên mật khẩu",
                        Toast.LENGTH_SHORT
                ).show()
        );

        txtRegister.setOnClickListener(v ->
                startActivity(new Intent(
                        LoginActivity.this,
                        RegisterActivity.class
                ))
        );

        btnFacebook.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Login Facebook",
                        Toast.LENGTH_SHORT
                ).show()
        );

        btnGoogle.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Login Google",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }
}