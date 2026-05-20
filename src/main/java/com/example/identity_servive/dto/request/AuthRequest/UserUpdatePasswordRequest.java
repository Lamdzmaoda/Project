/* (C)2026 */
package com.example.identity_servive.dto.request.AuthRequest;

import com.example.identity_servive.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

/**
 * Class đại diện cho yêu cầu cập nhật thông tin người dùng. Chứa các trường dữ liệu mà người dùng
 * được phép thay đổi.
 */
@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc cho Jackson/Spring)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@Builder // Hỗ trợ khởi tạo đối tượng theo pattern Builder
@FieldDefaults(level = AccessLevel.PRIVATE) // Tự động đặt tất cả các field là 'private'
public class UserUpdatePasswordRequest {
  // Tên mới
    @Size(min = 8, message = "PASSWORD_INVALID")
  String password;
}
