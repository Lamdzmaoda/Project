package com.example.appcodetest.model;

public class Lesson {

    // 🔥 ID của lesson
    public String id;

    // Tên bài học
    // ví dụ: Vòng lặp For trong Python
    public String title;

    // Thứ tự lesson trong chapter
    public int orderIndex;

    // XP nhận được khi hoàn thành lesson
    public double xp;

    // Tiến độ hoàn thành
    // ví dụ:
    // 0.0 = chưa học
    // 1.0 = hoàn thành
    public double progress;

    // Trạng thái khóa / mở
    // ví dụ:
    // FALSE_LOCKED
    // TRUE_LOCKED
    public String lockedStatus;

    // Trạng thái hoàn thành
    // ví dụ:
    // FALSE
    // TRUE
    public String completedStatus;

    // Ngày tạo lesson
    public String createAt;

    // Nội dung markdown mô tả lesson
    public String contentMarkdown;

    // ❌ Không cần list steps ở đây
    // vì step sẽ load riêng bằng API khác


    @Override
    public String toString() {
        // RecyclerView / Spinner sẽ hiển thị title
        return title;
    }
}