package com.example.appcodetest.model;

public class Language {

    // 🔥 Tên language (đồng thời là ID chính)
    // ví dụ: Python, Java, C++
    public String name;

    // Mô tả ngắn của course
    public String description;

    // Link icon đại diện
    public String icon;

    // Level độ khó
    // ví dụ: 1 = cơ bản, 2 = trung cấp...
    public int level;

    // Thời gian học dự kiến
    // backend trả về dạng datetime
    public String durationDays;

    // Ngày tạo course
    public String createAt;

    // ❌ Không cần list chapters ở đây
    // vì chapter sẽ load riêng bằng API khác


    @Override
    public String toString() {
        // Spinner / RecyclerView sẽ hiển thị tên language
        return name;
    }
}