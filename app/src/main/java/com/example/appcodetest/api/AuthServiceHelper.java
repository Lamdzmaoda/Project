package com.example.appcodetest.api;

import com.example.appcodetest.model.*;

import retrofit2.Call;

public class AuthServiceHelper {

    // =========================
    // 🔐 LOGIN
    // =========================

    // Hàm hỗ trợ đăng nhập
    public static Call<AuthResponse> login(
            ApiService api,
            LoginRequest request
    ) {
        return api.login(request);
    }

    // =========================
    // 📝 REGISTER
    // =========================

    // FIX:
    // ApiService.register() hiện trả về:
    // Call<Object>
    //
    // nên helper cũng phải trả đúng kiểu này,
    // không được để Call<ApiResponse<Object>>
    // nếu không sẽ lỗi:
    //
    // Incompatible types:
    // Found: Call<Object>
    // Required: Call<ApiResponse<Object>>

    public static Call<Object> register(
            ApiService api,
            String email,
            String password,
            String userName
    ) {
        RegisterRequest request =
                new RegisterRequest(
                        userName,
                        password,
                        email
                );

        return api.register(request);
    }
}