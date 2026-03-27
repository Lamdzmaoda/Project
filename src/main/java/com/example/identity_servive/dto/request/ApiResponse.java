/* (C)2026 */
package com.example.identity_servive.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Class dùng chung để bao bọc mọi phản hồi từ API (Standard API Response). Giúp Frontend luôn nhận
 * được một cấu trúc dữ liệu nhất quán.
 *
 * @param <T> Kiểu dữ liệu của nội dung phản hồi (User, List, AuthenticationResponse, v.v.)
 */
@Data // Tự động tạo Getter, Setter, toString, equals và hashCode
@Builder // Hỗ trợ khởi tạo đối tượng theo pattern Builder (ví dụ:
// ApiResponse.builder().code(1000).build())
@NoArgsConstructor // Tạo constructor không tham số (cần thiết cho Jackson để chuyển JSON sang
// Object)
@AllArgsConstructor // Tạo constructor đầy đủ tham số
@FieldDefaults(level = AccessLevel.PRIVATE) // Tự động đặt tất cả các field là 'private'
@JsonInclude(
    JsonInclude.Include
        .NON_NULL) // Chỉ render ra JSON những trường nào có giá trị (trường null sẽ bị ẩn đi)
public class ApiResponse<T> {

  // Mã code tùy chỉnh (ví dụ: 1000 là thành công, 1001 là lỗi validate, v.v.)
  int code;

  // Thông điệp giải thích về kết quả trả về
  String message;

  // Dữ liệu thực tế trả về cho Client (có thể là một Object hoặc một List)
  T result;
}
