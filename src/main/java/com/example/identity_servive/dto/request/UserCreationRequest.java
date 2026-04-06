/* (C)2026 */
package com.example.identity_servive.dto.request;

import com.example.identity_servive.enums.Role;
import com.example.identity_servive.validator.DobConstraint;
import jakarta.persistence.Id;
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
public class UserCreationRequest {
    @DobConstraint(min = 4, message = "USERNAME_INVALID")
    String username;

    @DobConstraint(min = 8, message = "INVALID_PASSWORD")
    String password;


    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;

  // Tên mới
  String firstName;

  String email;

    // Họ mới
  String lastName;

  // Ngày sinh cần cập nhật
  @DobConstraint(min = 10, message = "INVALID_DOB")
  LocalDate birthDate;

  List<Role> roles;
}
