package com.example.democode3.features.auth.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.democode3.R;
import com.example.democode3.features.auth.api.response.RegisterResponse;
import com.example.democode3.features.auth.repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity
        extends AppCompatActivity {

    // =====================================
    // VIEW
    // =====================================

    private EditText edtFullName;

    private EditText edtUsername;

    private EditText edtEmail;

    private EditText edtPassword;

    private EditText edtConfirmPassword;

    private Button btnRegister;

    private TextView txtLogin;

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
                R.layout.activity_register
        );

        initViews();

        initRepository();

        setupClick();
    }

    // =====================================
    // INIT VIEW
    // =====================================

    private void initViews() {

        edtFullName =
                findViewById(R.id.edtFullName);

        edtUsername =
                findViewById(R.id.edtUsername);

        edtEmail =
                findViewById(R.id.edtEmail);

        edtPassword =
                findViewById(R.id.edtPassword);

        edtConfirmPassword =
                findViewById(
                        R.id.edtConfirmPassword
                );

        btnRegister =
                findViewById(R.id.btnRegister);

        txtLogin =
                findViewById(R.id.txtLogin);
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

        // REGISTER

        btnRegister.setOnClickListener(v -> {

            String displayName =
                    edtFullName.getText()
                            .toString()
                            .trim();

            String username =
                    edtUsername.getText()
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

            // VALIDATE

            if (
                    displayName.isEmpty()
                            ||
                            username.isEmpty()
                            ||
                            email.isEmpty()
                            ||
                            password.isEmpty()
                            ||
                            confirmPassword.isEmpty()
            ) {

                Toast.makeText(
                        this,
                        "Nhập đầy đủ thông tin",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // PASSWORD CHECK

            if (
                    !password.equals(confirmPassword)
            ) {

                Toast.makeText(
                        this,
                        "Mật khẩu không khớp",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // REGISTER API

            repository.register(

                    displayName,

                    username,

                    email,

                    password,

                    new Callback<RegisterResponse>() {

                        @Override
                        public void onResponse(

                                Call<RegisterResponse> call,

                                Response<RegisterResponse> response
                        ) {

                            Log.d(
                                    "REGISTER_CODE",
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
                            ) {

                                Toast.makeText(

                                        RegisterActivity.this,

                                        "Đăng ký thành công",

                                        Toast.LENGTH_SHORT

                                ).show();

                                startActivity(

                                        new Intent(

                                                RegisterActivity.this,

                                                LoginActivity.class
                                        )
                                );

                                finish();
                            }

                            // ERROR

                            else {

                                String message =
                                        "Đăng ký thất bại";

                                try {

                                    if (
                                            response.errorBody() != null
                                    ) {

                                        String errorBody =

                                                response.errorBody()
                                                        .string();

                                        Log.e(
                                                "REGISTER_ERROR",
                                                errorBody
                                        );

                                        if (
                                                errorBody.contains(
                                                        "user exists"
                                                )
                                        ) {

                                            message =
                                                    "Tài khoản đã tồn tại";
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

                                        RegisterActivity.this,

                                        message,

                                        Toast.LENGTH_LONG

                                ).show();
                            }
                        }

                        @Override
                        public void onFailure(

                                Call<RegisterResponse> call,

                                Throwable t
                        ) {

                            Log.e(
                                    "REGISTER_FAILURE",
                                    t.getMessage()
                            );

                            Toast.makeText(

                                    RegisterActivity.this,

                                    "Không thể kết nối server",

                                    Toast.LENGTH_LONG

                            ).show();
                        }
                    }
            );
        });

        // LOGIN

        txtLogin.setOnClickListener(v -> {

            finish();
        });
    }
}