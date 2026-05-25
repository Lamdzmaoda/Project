/* (C)2026 */
package com.example.identity_servive.dto.response.learningResponse;

import com.example.identity_servive.enums.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang
// Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StepResponse {
  String id;
  String title;
  Type type;
  int orderIndex;
  ContentStatus status;
  String slug; // Đường dẫn URL
  String thumbnailUrl; // Ảnh thumbnail
  LocalDateTime createAt;
  LocalDateTime updateAt;
  Object data;
}
