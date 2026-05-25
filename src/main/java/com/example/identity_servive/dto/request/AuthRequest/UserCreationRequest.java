/* (C)2026 */
package com.example.identity_servive.dto.request.AuthRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class UserCreationRequest {
  String displayName;

  @NotBlank(message = "USERNAME_REQUIRED")
  @Size.List({
    @Size(min = 4, message = "USER_INVALID"),
    @Size(max = 20, message = "USERNAME_TOO_LONG")
  })
  String username;

  @NotBlank(message = "PASSWORD_REQUIRED")
  @Size.List({
    @Size(min = 8, message = "PASSWORD_INVALID"),
    @Size(max = 20, message = "PASSWORD_TOO_LONG")
  })
  String password;

  @NotBlank(message = "")
  @Email
  String email;
}
