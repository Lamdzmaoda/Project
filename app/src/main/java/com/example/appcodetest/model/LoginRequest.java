package com.example.appcodetest.model;

public class LoginRequest {

    // 🔥 Tên đăng nhập
    // backend dùng field username
    private String username;

    // Mật khẩu đăng nhập
    private String password;


    // Constructor dùng khi gọi API login
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}