package com.example.identity_servive.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Class đại diện cho kết quả trả về sau khi thực hiện yêu cầu xác thực.
 * Giúp Client biết được việc đăng nhập có thành công hay không.
 */
@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh (Ví dụ: AuthenticationResponse.builder().authenticated(true).build())
@NoArgsConstructor // Tạo constructor không tham số (Cần thiết cho Jackson để chuyển đổi Object sang JSON)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE) // Tự động đặt các field là 'private'
public class AuthenticationResponse {

    /**
     * Trạng thái xác thực:
     * - true: Thông tin đăng nhập chính xác.
     * - false: Sai username hoặc mật khẩu.
     */
    boolean authenticated;
    String token;
}