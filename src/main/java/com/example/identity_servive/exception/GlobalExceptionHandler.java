package com.example.identity_servive.exception;

import com.example.identity_servive.dto.request.ApiResponse;
import com.example.identity_servive.dto.response.UserResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Bộ xử lý ngoại lệ toàn cục (Global Exception Handler).
 * @ControllerAdvice: Lắng nghe tất cả các ngoại lệ xảy ra ở tầng Controller của ứng dụng.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    /**
     * Bắt tất cả các lỗi thuộc loại RuntimeException (các lỗi hệ thống chưa được định nghĩa cụ thể).
     * @param ex Ngoại lệ bị ném ra
     * @return Phản hồi API với mã lỗi mặc định (thường là 9999)
     */

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handleException(RuntimeException ex) {
        ApiResponse response = new ApiResponse();

        // Sử dụng mã lỗi chung cho các lỗi hệ thống không mong muốn
        response.setCode(ErrorCode.UNAUTHORIZED_EXISTED.getCode());
        response.setMessage(ErrorCode.UNAUTHORIZED_EXISTED.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Bắt lỗi AppException - Loại ngoại lệ nghiệp vụ mà chúng ta chủ động ném ra trong Service.
     * @param ex Ngoại lệ chứa mã lỗi (ErrorCode) cụ thể
     */
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse response = new ApiResponse();

        // Lấy mã số và thông điệp tương ứng từ Enum ErrorCode
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode()).body
                (ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
                );
    }
    /**
     * Bắt lỗi Validation (Lỗi do vi phạm các ràng buộc @Size, @NotBlank... trong DTO).
     * @param ex Ngoại lệ do Spring Validator ném ra khi dữ liệu đầu vào không hợp lệ
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleException(MethodArgumentNotValidException ex) {
        // Lấy ra message key mà chúng ta đặt trong @Size(message = "USER_INVALID")
        String enumkey = ex.getFieldError().getDefaultMessage();

        // Mặc định là lỗi INVALID_KEY nếu không tìm thấy key tương ứng trong Enum
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String,Object> attributes = null;

        try {
            // Chuyển chuỗi message key thành đối tượng Enum ErrorCode tương ứng
            errorCode = ErrorCode.valueOf(enumkey);

            var constraintViolations = ex.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attributes = constraintViolations.getConstraintDescriptor().getAttributes();
            log.info(attributes.toString());
        } catch (IllegalArgumentException e) {
            // Nếu key truyền vào không khớp với tên bất kỳ Enum nào, giữ nguyên INVALID_KEY
        }

        ApiResponse response = new ApiResponse();
        response.setCode(errorCode.getCode());
        response.setMessage(Objects.nonNull(attributes) ? mapAttributes(errorCode.getMessage(), attributes) : errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }
    private String mapAttributes(String message, Map<String, Object> Attributes){
        String minValue = Attributes.get(MIN_ATTRIBUTE).toString();

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}