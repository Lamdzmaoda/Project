package com.example.appcodetest.model;

import java.util.List;

public class LessonStep {

    // 🔥 ID của step
    public String id;

    // Tên step
    // ví dụ: Kiểm tra kiến thức IF
    public String title;

    // Loại step
    // ví dụ:
    // INFO
    // CODE
    // QUESTION
    // QUIZ
    // FILL_INLINE
    // FILL_CHOICE
    public String type;

    // Chế độ step
    // ví dụ:
    // LEARN
    // PRACTICE
    public String mode;

    // Thứ tự step trong lesson
    public int orderIndex;

    // Trạng thái khóa / mở
    public String lockedStatus;

    // Trạng thái hoàn thành
    public String completedStatus;

    // XP nhận được khi hoàn thành step
    public double xp;

    // Có bắt buộc hoàn thành để mở step tiếp theo không
    public boolean requiredToUnlockNext;

    // Ngày tạo step
    public String createAt;

    // Dữ liệu chính của step
    public StepData data;


    public static class StepData {

        // =========================
        // QUIZ / QUESTION
        // =========================

        // Câu hỏi chính
        public String question;

        // Danh sách đáp án
        public List<String> options;

        // Vị trí đáp án đúng
        public int correctValue;

        // Giải thích sau khi trả lời
        public String explanation;


        // =========================
        // FILL / CODE
        // =========================

        // Danh sách đáp án cho fill code
        // backend đang dùng field này
        public List<Object> answers;
    }


    @Override
    public String toString() {
        // RecyclerView hiển thị title
        return title;
    }
}