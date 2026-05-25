/* (C)2026 */
package com.example.identity_servive.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Ngoại lệ tùy chỉnh cho ứng dụng (Custom Application Exception). Dùng để ném ra (throw) khi có lỗi
 * nghiệp vụ xảy ra (ví dụ: User không tồn tại, Sai mật khẩu). Kế thừa từ RuntimeException để không
 * bắt buộc phải try-catch mọi nơi.
 */
@Getter
@Setter
public class AppException extends RuntimeException {

  // Chứa thông tin chi tiết về lỗi (Mã lỗi, Thông điệp, HTTP Status)
  private ErrorCode errorCode;
  private String detailMessage;

  /**
   * Khởi tạo ngoại lệ dựa trên một mã lỗi cụ thể.
   *
   * @param errorCode Đối tượng Enum chứa thông tin lỗi.
   */
  public AppException(ErrorCode errorCode) {
    // Truyền thông điệp lỗi (message) lên class cha (RuntimeException)
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.detailMessage = null;
  }

  public AppException(ErrorCode errorCode, String detailMessage) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.detailMessage = detailMessage;
  }
}
