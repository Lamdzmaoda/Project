/* (C)2026 */
package com.example.identity_servive.dto.request.AuthRequest;

import com.example.identity_servive.validator.DobConstraint;
import java.time.LocalDate;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Class đại diện cho yêu cầu cập nhật thông tin người dùng. Chứa các trường dữ liệu mà người dùng
 * được phép thay đổi.
 */
@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc cho Jackson/Spring)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@Builder // Hỗ trợ khởi tạo đối tượng theo pattern Builder
@FieldDefaults(level = AccessLevel.PRIVATE) // Tự động đặt tất cả các field là 'private'
public class UserUpdateRequest {
  // Tên mới
  String displayName;

  String avatarUrl; // Ảnh đại diện

  String bio;

  // Ngày sinh cần cập nhật
  @DobConstraint(min = 10, message = "INVALID_DOB")
  LocalDate birthDate;

  List<String> roles;
}
