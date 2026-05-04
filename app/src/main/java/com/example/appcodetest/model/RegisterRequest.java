package com.example.appcodetest.model;

public class RegisterRequest {

    // 🔥 Tên tài khoản đăng ký
    private String username;

    // Mật khẩu đăng ký
    private String password;

    // Email đăng ký
    private String email;


    // Constructor dùng khi gọi API register
    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}