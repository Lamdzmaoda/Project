package com.example.appcodetest.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;
import com.example.appcodetest.api.ApiService;
import com.example.appcodetest.api.AuthServiceHelper;
import com.example.appcodetest.api.RetrofitClient;
import com.example.appcodetest.model.AuthResponse;
import com.example.appcodetest.model.LoginRequest;

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

        // 🔥 TẠM TẮT AUTO LOGIN (TRÁNH MÀN HÌNH ĐEN)
        SharedPreferences prefs = getSharedPreferences("APP", MODE_PRIVATE);
        String savedToken = prefs.getString("TOKEN", null);

        // 👉 COMMENT LẠI ĐOẠN NÀY (khi fix xong Main thì bật lại)
        /*
        if (savedToken != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }
        */

        // 🔹 GÁN VIEW
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtForgot = findViewById(R.id.txtForgot);
        txtRegister = findViewById(R.id.txtRegister);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnGoogle = findViewById(R.id.btnGoogle);

        // 🔥 TẠO API
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // 🔥 LOGIN
        btnLogin.setOnClickListener(v -> {

            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest request = new LoginRequest(username, password);

            Call<AuthResponse> call = AuthServiceHelper.login(apiService, request);

            call.enqueue(new Callback<AuthResponse>() {

                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                    if (response.isSuccessful() && response.body() != null
                            && response.body().result != null
                            && response.body().result.authenticated) {

                        String token = response.body().result.token;

                        Toast.makeText(LoginActivity.this, "Login thành công!", Toast.LENGTH_SHORT).show();

                        SharedPreferences prefs = getSharedPreferences("APP", MODE_PRIVATE);
                        prefs.edit().putString("TOKEN", token).apply();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Không kết nối server!", Toast.LENGTH_SHORT).show();
                }
            });

        });

        // 🔹 FORGOT PASSWORD
        txtForgot.setOnClickListener(v ->
                Toast.makeText(this, "Chức năng quên mật khẩu", Toast.LENGTH_SHORT).show()
        );

        // 🔥 REGISTER
        txtRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // 🔹 FACEBOOK
        btnFacebook.setOnClickListener(v ->
                Toast.makeText(this, "Login Facebook", Toast.LENGTH_SHORT).show()
        );

        // 🔹 GOOGLE
        btnGoogle.setOnClickListener(v ->
                Toast.makeText(this, "Login Google", Toast.LENGTH_SHORT).show()
        );
    }
}