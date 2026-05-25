/* (C)2026 */
package com.example.identity_servive.dto.request.learningRequest;

import com.example.identity_servive.enums.LessonType;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang
// Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonRequest {
  String chapterId;
  String title;
  int orderIndex;
  long xp;
  LessonType lessonType;
  String content; // Nội dung giải thích trước khi code
  String slug; // Đường dẫn URL
  String thumbnailUrl; // Ảnh thumbnail
  String description;
  List<String> steps;
  List<String> problems;
}
