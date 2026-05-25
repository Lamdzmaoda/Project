package com.example.democode3.features.auth.repository;

import android.content.Context;

import com.example.democode3.core.network.RetrofitClient;

import com.example.democode3.features.auth.api.AuthApiService;

import com.example.democode3.features.auth.api.request.LoginRequest;
import com.example.democode3.features.auth.api.request.RefreshRequest;
import com.example.democode3.features.auth.api.request.RegisterRequest;

import com.example.democode3.features.auth.api.response.LoginResponse;
import com.example.democode3.features.auth.api.response.RegisterResponse;

import retrofit2.Callback;

public class AuthRepository {

    // =====================================
    // API
    // =====================================

    private final AuthApiService api;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public AuthRepository(
            Context context
    ) {

        api =

                RetrofitClient

                        .getInstance(context)

                        .create(
                                AuthApiService.class
                        );
    }

    // =====================================
    // LOGIN
    // =====================================

    public void login(

            String username,

            String password,

            Callback<LoginResponse> callback
    ) {

        LoginRequest request =
                new LoginRequest();

        request.username =
                username;

        request.password =
                password;

        api.login(request)
                .enqueue(callback);
    }

    // =====================================
    // REFRESH TOKEN
    // =====================================

    public void refreshToken(

            String token,

            Callback<LoginResponse> callback
    ) {

        RefreshRequest request =
                new RefreshRequest();

        request.token =
                token;

        api.refresh(request)
                .enqueue(callback);
    }

    // =====================================
    // REGISTER
    // =====================================

    public void register(

            String displayName,

            String username,

            String email,

            String password,

            Callback<RegisterResponse> callback
    ) {

        RegisterRequest request =
                new RegisterRequest();

        request.displayName =
                displayName;

        request.username =
                username;

        request.email =
                email;

        request.password =
                password;

        api.register(request)
                .enqueue(callback);
    }
}