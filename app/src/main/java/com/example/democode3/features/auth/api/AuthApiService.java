package com.example.democode3.features.auth.api;

import com.example.democode3.features.auth.api.request.LoginRequest;
import com.example.democode3.features.auth.api.request.RefreshRequest;
import com.example.democode3.features.auth.api.request.RegisterRequest;

import com.example.democode3.features.auth.api.response.LoginResponse;
import com.example.democode3.features.auth.api.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {

    // =====================================
    // LOGIN
    // =====================================

    @POST("auth/token")
    Call<LoginResponse> login(

            @Body
            LoginRequest request
    );

    // =====================================
    // REFRESH TOKEN
    // =====================================

    @POST("auth/refresh")
    Call<LoginResponse> refresh(

            @Body
            RefreshRequest request
    );

    // =====================================
    // REGISTER
    // =====================================

    @POST("users")
    Call<RegisterResponse> register(

            @Body
            RegisterRequest request
    );
}