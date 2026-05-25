/* (C)2026 */
package com.example.identity_servive.dto.request.AuthRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Class đại diện cho yêu cầu đổi mật khẩu của người dùng. Người dùng phải gửi mật khẩu cũ
 * (oldPassword) để xác thực và mật khẩu mới (newPassword) để cập nhật.
 */
@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc cho Jackson/Spring)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@Builder // Hỗ trợ khởi tạo đối tượng theo pattern Builder
@FieldDefaults(level = AccessLevel.PRIVATE) // Tự động đặt tất cả các field là 'private'
public class UserUpdatePasswordRequest {

  // Mật khẩu hiện tại — dùng để xác thực trước khi cho phép đổi
  @NotBlank(message = "PASSWORD_INVALID")
  String oldPassword;

  // Mật khẩu mới — sẽ được mã hóa trước khi lưu vào DB
  @NotBlank(message = "PASSWORD_INVALID")
  @Size(min = 8, message = "PASSWORD_INVALID")
  @Size(max = 20, message = "PASSWORD_TOO_LONG")
  String newPassword;
}
