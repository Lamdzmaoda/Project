/* (C)2026 */
package com.example.identity_servive.dto.response.progress;

import com.example.identity_servive.dto.response.learningResponse.LanguageResponse;
import com.example.identity_servive.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang
// Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollmentResponse {
  String id;
  double currentXp;
  Status status;
  LocalDateTime enrolledAt;
  LocalDateTime completedAt;
  String userName;
  LanguageResponse language;
}
