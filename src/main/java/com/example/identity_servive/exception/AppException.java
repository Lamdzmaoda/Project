/* (C)2026 */
package com.example.identity_servive.exception;

/**
 * Ngoại lệ tùy chỉnh cho ứng dụng (Custom Application Exception). Dùng để ném ra (throw) khi có lỗi
 * nghiệp vụ xảy ra (ví dụ: User không tồn tại, Sai mật khẩu). Kế thừa từ RuntimeException để không
 * bắt buộc phải try-catch mọi nơi.
 */
public class AppException extends RuntimeException {

  // Chứa thông tin chi tiết về lỗi (Mã lỗi, Thông điệp, HTTP Status)
  private ErrorCode errorCode;

  /**
   * Khởi tạo ngoại lệ dựa trên một mã lỗi cụ thể.
   *
   * @param errorCode Đối tượng Enum chứa thông tin lỗi.
   */
  public AppException(ErrorCode errorCode) {
    // Truyền thông điệp lỗi (message) lên class cha (RuntimeException)
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  // Getter để lấy thông tin mã lỗi khi xử lý ở GlobalExceptionHandler
  public ErrorCode getErrorCode() {
    return errorCode;
  }

  // Setter để cập nhật mã lỗi nếu cần
  public void setErrorCode(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}
