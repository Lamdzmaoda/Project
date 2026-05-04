package com.example.appcodetest.model;

public class UserUpdateRequest {

    // 🔥 Tên của user
    public String firstName;

    // Họ của user
    public String lastName;

    // Email cập nhật
    public String email;


    // Constructor hiện tại đang dùng để update tên
    public UserUpdateRequest(String firstName) {
        this.firstName = firstName;
    }
}