package com.example.identity_servive.dto.request.learningRequest;

import com.example.identity_servive.enums.Difficulty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang
// Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProblemRequest {
    String lessonId;
    String title;
    String description;
    String content;
    String slug;
    int orderIndex;
    String expectedOutput;
    Difficulty difficulty;
    String methodName;
    String solutionCode;
    String hint;
    List<ProblemConditionRequest> conditions;
}
