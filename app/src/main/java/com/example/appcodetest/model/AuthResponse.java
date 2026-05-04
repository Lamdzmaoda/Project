package com.example.appcodetest.model;

public class AuthResponse {

    // 🔥 Mã phản hồi từ server
    public int code;

    // 🔥 Thông báo trả về
    public String message;

    // 🔥 Kết quả đăng nhập
    public Result result;


    // Class con chứa dữ liệu login
    public static class Result {

        // true = đăng nhập thành công
        // false = sai tài khoản hoặc mật khẩu
        public boolean authenticated;

        // JWT token dùng để gọi các API cần đăng nhập
        public String token;
    }
}