/* (C)2026 */
package com.example.identity_servive.dto.response.learningResponse;

import com.example.identity_servive.enums.ContentStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang
// Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LanguageResponse {
  String name;
  String description;
  String icon;
  ContentStatus status;
  LocalDateTime durationDays;
  long totalXp;
  String slug; // Đường dẫn URL (VD: java -> java-slug)
  LocalDateTime createAt;
  LocalDateTime updateAt;
  List<ChapterResponse> chapters;
}
