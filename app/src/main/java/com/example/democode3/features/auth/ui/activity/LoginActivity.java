package com.example.democode3.features.auth.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.democode3.MainActivity;
import com.example.democode3.R;
import com.example.democode3.core.session.TokenManager;
import com.example.democode3.features.admin.ui.activity.AdminActivity;
import com.example.democode3.features.auth.api.response.LoginResponse;
import com.example.democode3.features.auth.repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity
        extends AppCompatActivity {

    // =====================================
    // VIEW
    // =====================================

    private EditText edtEmail;

    private EditText edtPassword;

    private Button btnLogin;

    private TextView txtRegister;

    // =====================================
    // REPOSITORY
    // =====================================

    private AuthRepository repository;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_login
        );

        // =================================
        // AUTO LOGIN
        // =================================

        String token =
                TokenManager.getToken(this);

        if (token != null) {

            // ADMIN

            if (
                    TokenManager.isAdmin(this)
            ) {

                startActivity(

                        new Intent(

                                this,

                                AdminActivity.class
                        )
                );
            }

            // USER

            else {

                startActivity(

                        new Intent(

                                this,

                                MainActivity.class
                        )
                );
            }

            finish();

            return;
        }

        initViews();

        initRepository();

        setupClick();
    }

    // =====================================
    // INIT VIEW
    // =====================================

    private void initViews() {

        edtEmail =
                findViewById(R.id.edtEmail);

        edtPassword =
                findViewById(R.id.edtPassword);

        btnLogin =
                findViewById(R.id.btnLogin);

        txtRegister =
                findViewById(R.id.txtRegister);
    }

    // =====================================
    // REPOSITORY
    // =====================================

    private void initRepository() {

        repository =
                new AuthRepository(this);
    }

    // =====================================
    // CLICK
    // =====================================

    private void setupClick() {

        // LOGIN

        btnLogin.setOnClickListener(v -> {

            String email =
                    edtEmail.getText()
                            .toString()
                            .trim();

            String password =
                    edtPassword.getText()
                            .toString()
                            .trim();

            // VALIDATE

            if (email.isEmpty()) {

                edtEmail.setError(
                        "Nhập tài khoản"
                );

                return;
            }

            if (password.isEmpty()) {

                edtPassword.setError(
                        "Nhập mật khẩu"
                );

                return;
            }

            // LOGIN API

            repository.login(

                    email,

                    password,

                    new Callback<LoginResponse>() {

                        @Override
                        public void onResponse(

                                Call<LoginResponse> call,

                                Response<LoginResponse> response
                        ) {

                            Log.d(
                                    "LOGIN_CODE",
                                    String.valueOf(
                                            response.code()
                                    )
                            );

                            // SUCCESS

                            if (
                                    response.isSuccessful()
                                            &&
                                            response.body() != null
                                            &&
                                            response.body().result != null
                                            &&
                                            response.body().result.authenticated
                            ) {

                                // SAVE TOKEN

                                TokenManager.saveToken(

                                        LoginActivity.this,

                                        response.body()
                                                .result
                                                .token
                                );

                                Toast.makeText(

                                        LoginActivity.this,

                                        "Đăng nhập thành công",

                                        Toast.LENGTH_SHORT

                                ).show();

                                // ADMIN

                                if (
                                        TokenManager.isAdmin(
                                                LoginActivity.this
                                        )
                                ) {

                                    startActivity(

                                            new Intent(

                                                    LoginActivity.this,

                                                    AdminActivity.class
                                            )
                                    );
                                }

                                // USER

                                else {

                                    startActivity(

                                            new Intent(

                                                    LoginActivity.this,

                                                    MainActivity.class
                                            )
                                    );
                                }

                                finish();
                            }

                            // ERROR

                            else {

                                String message =
                                        "Đăng nhập thất bại";

                                try {

                                    if (
                                            response.errorBody() != null
                                    ) {

                                        String errorBody =

                                                response.errorBody()
                                                        .string();

                                        Log.e(
                                                "LOGIN_ERROR",
                                                errorBody
                                        );

                                        if (
                                                errorBody.contains(
                                                        "Unauthenticated"
                                                )
                                        ) {

                                            message =
                                                    "Sai tài khoản hoặc mật khẩu";
                                        }

                                        else if (
                                                errorBody.contains(
                                                        "user not exists"
                                                )
                                        ) {

                                            message =
                                                    "Tài khoản không tồn tại";
                                        }

                                        else {

                                            message = errorBody;
                                        }
                                    }

                                }

                                catch (Exception e) {

                                    e.printStackTrace();
                                }

                                Toast.makeText(

                                        LoginActivity.this,

                                        message,

                                        Toast.LENGTH_LONG

                                ).show();
                            }
                        }

                        @Override
                        public void onFailure(

                                Call<LoginResponse> call,

                                Throwable t
                        ) {

                            Log.e(
                                    "LOGIN_FAILURE",
                                    t.getMessage()
                            );

                            Toast.makeText(

                                    LoginActivity.this,

                                    "Không thể kết nối server",

                                    Toast.LENGTH_LONG

                            ).show();
                        }
                    }
            );
        });

        // REGISTER

        txtRegister.setOnClickListener(v -> {

            startActivity(

                    new Intent(
                            this,
                            RegisterActivity.class
                    )
            );
        });
    }
}