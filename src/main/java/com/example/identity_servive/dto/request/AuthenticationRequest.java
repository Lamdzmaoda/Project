package com.example.identity_servive.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Class đại diện cho yêu cầu xác thực (đăng nhập) từ Client gửi lên.
 * Chứa các thông tin cần thiết để hệ thống nhận diện người dùng.
 */
@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE) // Tự động đặt tất cả các field là 'private' (theo chuẩn đóng gói)
public class AuthenticationRequest {

    // Tên đăng nhập của người dùng (có thể là username hoặc email tùy logic hệ thống)
    String username;

    // Mật khẩu thuần (plain text) người dùng nhập từ giao diện
    String password;

}