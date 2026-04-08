package com.example.appcodetest.utils;

import com.example.appcodetest.model.LessonStep;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FakeDataProvider {

    public static List<LessonStep> getSteps(String lessonId) {

        List<LessonStep> steps = new ArrayList<>();

        try {

            // =========================
            // LESSON 1 - BIẾN
            // =========================
            if ("lesson_1".equals(lessonId)) {

                // STEP 1 - INFO
                LessonStep s1 = new LessonStep();
                s1.type = "INFO";
                s1.data = new JSONObject();
                s1.data.put("content", "👋 Chào bạn!\nBiến giúp lưu trữ dữ liệu trong chương trình.");
                steps.add(s1);

                // STEP 2 - INFO
                LessonStep s2 = new LessonStep();
                s2.type = "INFO";
                s2.data = new JSONObject();
                s2.data.put("content", "Ví dụ:\nname = \"An\"\nname là tên biến.");
                steps.add(s2);

                // STEP 3 - QUESTION
                LessonStep s3 = new LessonStep();
                s3.type = "QUESTION";
                s3.data = new JSONObject();
                s3.data.put("question", "Biến dùng để làm gì?");
                s3.data.put("options", new JSONArray()
                        .put("Trang trí giao diện")
                        .put("Lưu trữ dữ liệu")
                        .put("Tăng tốc máy"));
                s3.data.put("correctIndex", 1);
                s3.data.put("explanation", "Biến dùng để lưu trữ dữ liệu.");
                steps.add(s3);

                // STEP 4 - INFO CLICK
                LessonStep s4 = new LessonStep();
                s4.type = "INFO";
                s4.data = new JSONObject();
                s4.data.put("content", "Nhấn vào để tiếp tục (city)");
                steps.add(s4);

                // STEP 5 - FILL CODE
                LessonStep s5 = new LessonStep();
                s5.type = "FILL_CODE";
                s5.data = new JSONObject();
                s5.data.put("codeTemplate", "_____ = _____");
                s5.data.put("words", new JSONArray()
                        .put("city")
                        .put("=")
                        .put("\"Hanoi\""));
                s5.data.put("correctAnswer", "city = \"Hanoi\"");
                steps.add(s5);

                // STEP 6 - INFO DONE
                LessonStep s6 = new LessonStep();
                s6.type = "INFO";
                s6.data = new JSONObject();
                s6.data.put("content", "🎉 Tốt lắm! Bạn đã hiểu cách tạo biến.");
                steps.add(s6);
            }

            // =========================
            // LESSON 2 - IF
            // =========================
            else if ("lesson_2".equals(lessonId)) {

                // STEP 1 - INFO
                LessonStep s1 = new LessonStep();
                s1.type = "INFO";
                s1.data = new JSONObject();
                s1.data.put("content", "🔀 Câu lệnh if dùng để kiểm tra điều kiện.");
                steps.add(s1);

                // STEP 2 - INFO
                LessonStep s2 = new LessonStep();
                s2.type = "INFO";
                s2.data = new JSONObject();
                s2.data.put("content", "Cú pháp:\nif (điều kiện) {\n   // code\n}");
                steps.add(s2);

                // STEP 3 - QUESTION
                LessonStep s3 = new LessonStep();
                s3.type = "QUESTION";
                s3.data = new JSONObject();
                s3.data.put("question", "Nếu điều kiện sai thì chuyện gì xảy ra?");
                s3.data.put("options", new JSONArray()
                        .put("Code bên trong vẫn chạy")
                        .put("Code bên trong không chạy")
                        .put("App bị crash"));
                s3.data.put("correctIndex", 1);
                s3.data.put("explanation", "Nếu điều kiện sai thì code bên trong không chạy.");
                steps.add(s3);

                // STEP 4 - QUESTION (CODE STYLE)
                LessonStep s4 = new LessonStep();
                s4.type = "QUESTION";
                s4.data = new JSONObject();
                s4.data.put("question", "if (5 > 6) sẽ như thế nào?");
                s4.data.put("options", new JSONArray()
                        .put("Luôn đúng")
                        .put("Sai vì 5 < 6")
                        .put("Không xác định"));
                s4.data.put("correctIndex", 1);
                s4.data.put("explanation", "5 không lớn hơn 6 nên điều kiện sai.");
                steps.add(s4);

                // STEP 5 - FILL CODE
                LessonStep s5 = new LessonStep();
                s5.type = "FILL_CODE";
                s5.data = new JSONObject();
                s5.data.put("codeTemplate", "if (_____ > _____)");
                s5.data.put("words", new JSONArray()
                        .put("5")
                        .put(">")
                        .put("3"));
                s5.data.put("correctAnswer", "5 > 3");
                steps.add(s5);

                // STEP 6 - INFO DONE
                LessonStep s6 = new LessonStep();
                s6.type = "INFO";
                s6.data = new JSONObject();
                s6.data.put("content", "🔥 Bạn đã hiểu if rồi!");
                steps.add(s6);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return steps;
    }
}