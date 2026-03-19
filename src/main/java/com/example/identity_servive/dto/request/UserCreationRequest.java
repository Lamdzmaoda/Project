package com.example.identity_servive.dto.request;

import com.example.identity_servive.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * Class đại diện cho yêu cầu tạo người dùng mới (Đăng ký tài khoản).
 * Chứa các thông tin cơ bản mà người dùng nhập từ form đăng ký.
 */
@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc cho Jackson/Spring)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@Builder // Hỗ trợ khởi tạo đối tượng theo pattern Builder (tiện cho việc Test)
@FieldDefaults(level = AccessLevel.PRIVATE) // Tự động đặt tất cả các field là 'private'
public class UserCreationRequest {

    // Tên đăng nhập của người dùng
    // @Size: Kiểm tra độ dài tối thiểu là 3 ký tự. Nếu sai, ném ra message "USER_INVALID"
    @Size(min = 8, message = "USER_INVALID")
    String userName;

    // Mật khẩu người dùng chọn
    // @Size: Kiểm tra độ dài tối thiểu là 8 ký tự. Nếu sai, ném ra message "PASSWORD_INVALID"
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;

    // Tên (Ví dụ: "Văn A")
    String firstName;

    // Họ (Ví dụ: "Nguyễn")
    String lastName;

    // Ngày sinh (Sử dụng kiểu LocalDate để quản lý ngày tháng chuẩn Java 8+)
    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate birthDate;
}