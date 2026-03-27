/* (C)2026 */
package com.example.identity_servive.dto.response;

import java.time.LocalDate;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Class đại diện cho dữ liệu người dùng trả về cho Client. Giúp kiểm soát những thông tin nào được
 * phép hiển thị ra bên ngoài.
 */
@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc cho Jackson để chuyển Object sang
// JSON)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@Builder // Hỗ trợ khởi tạo đối tượng nhanh (Dùng trong Mapper hoặc Service)
@FieldDefaults(level = AccessLevel.PRIVATE) // Tự động đặt tất cả các field là 'private'
public class UserResponse {

  // ID duy nhất của người dùng trong Database (thường là UUID dạng String)
  String id;

  // Tên đăng nhập
  String userName;

  /**
   * CẢNH BÁO BẢO MẬT: Thông thường, chúng ta KHÔNG BAO GIỜ trả về trường 'password' trong
   * UserResponse. Ngay cả khi mật khẩu đã được mã hóa, việc gửi nó về Client là một kẽ hở bảo mật.
   */

  // Tên của người dùng
  String firstName;

  // Họ của người dùng
  String lastName;

  String email;

  // Ngày sinh
  LocalDate birthDate;

  Set<RoleRespone> roles;
}
