/* (C)2026 */
package com.example.identity_servive.exception;

import com.example.identity_servive.dto.request.ApiResponse;
import jakarta.validation.ConstraintViolation;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tools.jackson.databind.exc.InvalidFormatException;

/**
 * Bộ xử lý ngoại lệ toàn cục (Global Exception Handler).
 * @ControllerAdvice: Lắng nghe tất cả các ngoại lệ xảy ra ở tầng Controller của ứng dụng.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    /**
     * 1. Bắt tất cả các lỗi thuộc loại RuntimeException (Lỗi hệ thống/Lỗi không xác định).
     */
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handleException(RuntimeException ex) {
        ApiResponse response = new ApiResponse();

        // In lỗi ra log để lập trình viên kiểm tra
        log.error("Runtime Exception: ", ex);

        // Trả về mã lỗi chung để tránh lộ thông tin hệ thống nhạy cảm
        response.setCode(ErrorCode.UNAUTHORIZED_EXISTED.getCode());
        response.setMessage(ErrorCode.UNAUTHORIZED_EXISTED.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 2. Bắt lỗi AppException - Các ngoại lệ nghiệp vụ do mình chủ động ném ra trong code (Service).
     */
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException ex) {
        // Lấy đối tượng ErrorCode từ trong exception được ném ra
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse response = new ApiResponse();

        // Gán mã code và tin nhắn tương ứng từ Enum ErrorCode vào phản hồi
        response.setCode(errorCode.getCode());
        // Logic quan trọng: Nếu có logs chi tiết từ Piston, ưu tiên hiển thị nó
        // Nếu không có, hiển thị thông báo mặc định của ErrorCode
        String finalMessage = (ex.getDetailMessage() != null && !ex.getDetailMessage().isEmpty())
                ? ex.getDetailMessage()
                : errorCode.getMessage();
        response.setMessage(finalMessage);

        // Trả về kèm HTTP Status (VD: 400 Bad Request, 404 Not Found)
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse<Void>> handlingHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        ApiResponse<Void> response = new ApiResponse();
        // Lấy đối tượng ErrorCode từ trong exception được ném ra
        ErrorCode errorCode = ErrorCode.PARSE_DATA_INVALID;

        String message = errorCode.getMessage();
        if (ex.getCause() instanceof InvalidFormatException invalidEx) {
            // Ví dụ: Lâm gửi "SAI_ENUM" vào trường IsLocked
            String targetType = invalidEx.getTargetType().getSimpleName();
            String value = invalidEx.getValue().toString();
            message += String.format("The value '%s' does not match the data type '%s'.", value, targetType);
        }

        // Trả về kèm HTTP Status (VD: 400 Bad Request, 404 Not Found)
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(
                        ApiResponse.<Void>builder()
                                .code(errorCode.getCode())
                                .message(message)
                                .build()
                );
    }

    /**
     * 3. Bắt lỗi AccessDeniedException - Xảy ra khi User truy cập vào API mà không có đủ quyền hạn.
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED; // Lỗi 403 Forbidden

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(
                        ApiResponse.builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build());
    }

    /**
     * 4. Bắt lỗi Validation (Dữ liệu đầu vào sai định dạng, vi phạm @Size, @Email, @Min...).
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleException(MethodArgumentNotValidException ex) {
        // Lấy message key từ Annotation (VD: @Size(message = "PASSWORD_INVALID"))
        String enumkey = Objects.requireNonNull(ex.getFieldError()).getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY; // Mặc định nếu không tìm thấy key trong Enum
        Map<String, Object> attributes = null;

        try {
            // Tìm Enum tương ứng với key lấy được
            errorCode = ErrorCode.valueOf(enumkey);

            // Trích xuất các thuộc tính của Validation (như giá trị 'min' trong @Size)
            var constraintViolations =
                    ex.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attributes = constraintViolations.getConstraintDescriptor().getAttributes();
            log.info("Validation attributes: {}", attributes.toString());
        } catch (IllegalArgumentException e) {
            // Xảy ra khi key trong Annotation không khớp với tên biến nào trong ErrorCode.java
        }

        ApiResponse response = new ApiResponse();
        response.setCode(errorCode.getCode());

        // Nếu có thuộc tính động (VD: mật khẩu phải có {min} ký tự), thực hiện thay thế giá trị vào tin nhắn
        response.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttributes(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    ResponseEntity<ApiResponse<Void>> handlingDataIntegrity(DataIntegrityViolationException ex) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        ErrorCode errorCode = ErrorCode.DATA_INTEGRITY_VIOLATION; // Lỗi 403 Forbidden
        String message = errorCode.getMessage();
        String rootMSG = ex.getRootCause() != null ? ex.getRootCause().getMessage() : "";
        if(rootMSG.contains("Duplicate entry")) {
            message = message + "Data existed";
        } else if (rootMSG.contains("Column") && rootMSG.contains("cannot be null")) {
            message = message + "The data is missing required fields.";
        }
        apiResponse.setMessage(message);
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(
                        ApiResponse.<Void>builder()
                                .code(errorCode.getCode())
                                .message(message)
                                .build());
    }

    /**
     * Hàm hỗ trợ: Thay thế các placeholder trong thông báo lỗi (VD: {min}) bằng giá trị thực tế.
     */
    private String mapAttributes(String message, Map<String, Object> Attributes) {
        String minValue = Attributes.get(MIN_ATTRIBUTE).toString();

        // Thay thế chuỗi "{min}" bằng giá trị số lấy từ Annotation
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}