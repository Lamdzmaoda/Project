package com.example.appcodetest.model;

public class Chapter {

    // 🔥 ID của chapter
    public String id;

    // Tên language mà chapter này thuộc về
    // ví dụ: Python, Java, C++
    public String languageName;

    // Tên chapter
    // ví dụ: Chương 1 - Biến trong Python
    public String title;

    // Thứ tự chapter trong course
    public int orderIndex;

    // Trạng thái khóa / mở
    // ví dụ:
    // FALSE_LOCKED
    // TRUE_LOCKED
    public String lockedStatus;

    // Ngày tạo chapter
    public String createAt;

    // ❌ Không cần list lessons ở đây
    // vì lesson sẽ load riêng bằng API khác


    @Override
    public String toString() {
        // Spinner / RecyclerView sẽ hiển thị title
        return title;
    }
}