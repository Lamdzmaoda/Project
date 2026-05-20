/* (C)2026 */
package com.example.identity_servive.configuration;

import com.example.identity_servive.dto.response.ApiResponse;
import com.example.identity_servive.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

// Lớp này thực thi Interface AuthenticationEntryPoint của Spring Security
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        // 1. Xác định loại lỗi mặc định là UNAUTHENTICATED (Chưa xác thực) từ Enum ErrorCode
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        // 2. Thiết lập mã trạng thái HTTP (ví dụ: 401) dựa trên ErrorCode đã định nghĩa
        response.setStatus(errorCode.getStatusCode().value());

        // 3. Thiết lập kiểu nội dung trả về là JSON để Frontend nhận diện được
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 4. Khởi tạo đối tượng ApiResponse để chứa thông tin lỗi (Code và Message)
        ApiResponse<?> apiResponse =
                ApiResponse.builder()
                        .code(errorCode.getCode()) // Mã lỗi nội bộ (ví dụ: 1001)
                        .message(errorCode.getMessage()) // Thông báo lỗi (ví dụ: "Unauthenticated")
                        .build();

        // 5. Sử dụng Jackson (ObjectMapper) để chuyển đổi đối tượng Java sang chuỗi JSON
        ObjectMapper mapper = JsonMapper.builder().build();

        // 6. Ghi chuỗi JSON vào thân phản hồi (Response Body) và đẩy về phía Client
        response.getWriter().write(mapper.writeValueAsString(apiResponse));

        // Đảm bảo dữ liệu được gửi đi ngay lập tức
        response.flushBuffer();
    }
}