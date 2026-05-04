package com.example.appcodetest.model;

public class ApiResponse<T> {

    // 🔥 Mã trạng thái trả về từ backend
    // Ví dụ:
    // 1000 = thành công
    // code khác = lỗi
    public int code;

    // 🔥 Thông báo từ server
    // Ví dụ:
    // "Success"
    // "User already exists"
    public String message;

    // 🔥 Dữ liệu chính trả về
    // dùng generic <T> để tái sử dụng cho nhiều loại data
    //
    // Ví dụ:
    // UserResponse
    // List<Language>
    // List<Lesson>
    public T result;
}