/* (C)2026 */
package com.example.identity_servive.dto.response.learningResponse;

import com.example.identity_servive.enums.ContentStatus;
import com.example.identity_servive.enums.LessonType;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonResponse {
  String id;
  String chapterId;
  String title;
  String description;
  int orderIndex;
  long xp;
  ContentStatus status;
  LessonType lessonType;
  String slug; // Đường dẫn URL
  String thumbnailUrl; // Ảnh thumbnail
  LocalDateTime createAt;
  LocalDateTime updateAt;
  List<StepResponse> steps;
  List<ProblemResponse> problems;
}
