/* (C)2026 */
package com.example.identity_servive.dto.response.progress;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang
// Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LearingProgressResponse {
  long currentXp; // XP hiện tại trong language này
  long totalXp; // Tổng XP tối đa của language
  long nextChapterRequiredXp; // XP cần để mở chapter tiếp theo
  int completedLessons; // Số lesson đã hoàn thành
  int totalLessons; // Tổng số lesson trong language
  int completedChapter;
}
