package com.example.identity_servive.dto.response.learningResponse;

import com.example.identity_servive.enums.Difficulty;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang
// Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProblemResponse {
    String id;
    String title;
    String description;
    Difficulty difficulty;
    String methodName;
    String content;  // Nội dung giải thích trước khi code
    String expectedOutput;
    String hint;
    int orderIndex;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    List<ProblemConditionResponse> conditions;
}
