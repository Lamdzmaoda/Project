/* (C)2026 */
package com.example.identity_servive.dto.response.learningResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang
// Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyResponse {
  boolean isCorrect;
  long earnedXp;
  String message;
  Object correctAnswer; // Đáp án đúng từ DB
  Object userOutput; // Output thực tế (nếu là Code thì là kết quả chạy code)
  com.example.identity_servive.enums.Type type;
}
