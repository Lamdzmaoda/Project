package com.example.appcodetest.api;

import com.example.appcodetest.model.AuthResponse;
import com.example.appcodetest.model.LoginRequest;
import com.example.appcodetest.model.RegisterRequest;

import retrofit2.Call;

public class AuthServiceHelper {

    // 🔐 LOGIN
    public static Call<AuthResponse> login(ApiService api, LoginRequest request) {
        return api.login(request);
    }

    // 🔥 REGISTER CHUẨN
    public static Call<Object> register(ApiService api, String email, String password, String userName) {

        RegisterRequest request = new RegisterRequest(userName, password, email);

        return api.register(request);
    }
}