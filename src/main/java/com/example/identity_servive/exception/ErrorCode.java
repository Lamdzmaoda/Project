/* (C)2026 */
package com.example.identity_servive.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * Danh sách các mã lỗi nghiệp vụ (Business Error Codes). Giúp thống nhất các thông điệp lỗi và mã
 * số phản hồi cho Client.
 */

@Getter
public enum ErrorCode {
  // Lỗi hệ thống hoặc lỗi chưa xác định (thường dùng mã 9999)
  UNAUTHORIZED_EXISTED(9999, "Unauthorized", HttpStatus.INTERNAL_SERVER_ERROR),

  // Lỗi khi tạo người dùng nhưng tên đăng nhập đã có trong hệ thống
  USER_EXISTED(1001, "user exists", HttpStatus.BAD_REQUEST),

  // Lỗi khi một khóa (key) truyền vào không hợp lệ hoặc không tìm thấy
  INVALID_KEY(1000, "invalid message key", HttpStatus.BAD_REQUEST),

  // Lỗi Validation: Tên người dùng không đủ độ dài yêu cầu
  USER_INVALID(1002, "username must be at least {min} characters", HttpStatus.BAD_REQUEST),

  // Lỗi Validation: Mật khẩu quá ngắn
  PASSWORD_INVALID(1003, "password must be at least {min} characters", HttpStatus.BAD_REQUEST),

  // Lỗi khi tìm kiếm hoặc cập nhật một người dùng không có trong DB
  USER_NOT_EXISTED(1005, "user not exists", HttpStatus.NOT_FOUND),

  UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
  INVALID_DOB(1008, "your age must be at least {min}", HttpStatus.BAD_REQUEST),
  ID_NOT_EXISTED(1009, "id not exists", HttpStatus.BAD_REQUEST),
    NAME_EXISTED(1010, "name exists", HttpStatus.BAD_REQUEST),
    PARSE_DATA_INVALID(1011, "parse data invalid : ", HttpStatus.BAD_REQUEST),
    DATA_INTEGRITY_VIOLATION(1012, "data integrity violation : ", HttpStatus.BAD_REQUEST),
    STEP_LOCKED(1015, "step locked", HttpStatus.BAD_REQUEST),
    LESSON_INCOMPLETE(1021, "LESSON NOT YET COMPLETED", HttpStatus.BAD_REQUEST),
    INVALID_ANSWER(1013, "INVALID ANSWER", HttpStatus.BAD_REQUEST),
    LESSON_LOCKED(1014, "LESSON LOCKED", HttpStatus.BAD_REQUEST),
    CHAPTER_LOCKED(1017,"chapter locked", HttpStatus.BAD_REQUEST),
    COURSE_UNDER_CONSTRUCTION(1018,"course under construction", HttpStatus.BAD_REQUEST),

    DELETE_FAILED(1019,"delete failed", HttpStatus.BAD_REQUEST),
    NULL_POINTER(1020, "null pointer exception", HttpStatus.BAD_REQUEST),
    CREATE_FAIL(1022, "create fail", HttpStatus.BAD_REQUEST),
    ALREADY_EXISTED(1023, "already existed", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(1024, "invalid request", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1025, "email exists", HttpStatus.BAD_REQUEST),
    UPLOAD_FAILED(1026, "upload failed", HttpStatus.BAD_REQUEST);
  // Mã số lỗi định danh (giúp Frontend dễ dàng bắt lỗi bằng code thay vì so sánh chuỗi)
  private int code = 2000;

  // Thông điệp giải thích lỗi bằng ngôn ngữ con người
  @Setter
  private String message;

  private HttpStatusCode statusCode;

  /** Constructor khởi tạo Enum */
  ErrorCode(int code, String message, HttpStatusCode statusCode) {
    this.code = code;
    this.message = message;
    this.statusCode = statusCode;
  }

}
