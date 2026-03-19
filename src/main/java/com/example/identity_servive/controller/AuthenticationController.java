package com.example.identity_servive.controller;

import com.example.identity_servive.dto.request.*;
import com.example.identity_servive.dto.response.AuthenticationResponse;
import com.example.identity_servive.dto.response.IntrospectResponse;
import com.example.identity_servive.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * Controller xử lý các yêu cầu liên quan đến xác thực (đăng nhập, logout, verify token).
 */
@RestController // Đánh dấu đây là một REST Controller, trả về dữ liệu dạng JSON
@RequestMapping("/auth") // Định nghĩa đường dẫn gốc cho các API trong class này là /auth
@RequiredArgsConstructor // Tự động tạo Constructor cho các field được đánh dấu là 'final' (Dependency Injection)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Tự động thêm 'private final' cho các field
public class AuthenticationController {

    // Inject AuthenticationService để xử lý logic nghiệp vụ xác thực
    AuthenticationService authenticationService;

    /**
     * API Đăng nhập
     * @param request Chứa thông tin username và password từ Client gửi lên
     * @return ApiResponse bao bọc kết quả xác thực (true/false)
     */
    @PostMapping("/token") // Tiếp nhận yêu cầu HTTP POST đến đường dẫn /auth/log-in
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {

        // Gọi xuống tầng Service để kiểm tra thông tin đăng nhập
        var result = authenticationService.authenticate(request);

        // Trả về kết quả theo cấu trúc ApiResponse chuẩn của dự án
        return ApiResponse.<AuthenticationResponse>builder()
                .code(2000)
                .result(result)
                .build();
    }

    @PostMapping("/introspect") // Tiếp nhận yêu cầu HTTP POST đến đường dẫn /auth/log-in
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {

        // Gọi xuống tầng Service để kiểm tra thông tin đăng nhập
        var result = authenticationService.introspect(request);

        // Trả về kết quả theo cấu trúc ApiResponse chuẩn của dự án
        return ApiResponse.<IntrospectResponse>builder()
                .code(2001)
                .result(result)
                .build();
    }
    @PostMapping("/refresh") // Tiếp nhận yêu cầu HTTP POST đến đường dẫn /auth/log-in
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request) throws ParseException, JOSEException {

        // Gọi xuống tầng Service để kiểm tra thông tin đăng nhập
        var result = authenticationService.refreshToken(request);

        // Trả về kết quả theo cấu trúc ApiResponse chuẩn của dự án
        return ApiResponse.<AuthenticationResponse>builder()
                .code(2000)
                .result(result)
                .build();
    }
    @PostMapping("/logout") // Tiếp nhận yêu cầu HTTP POST đến đường dẫn /auth/log-in
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {

        // Gọi xuống tầng Service để kiểm tra thông tin đăng nhập
        authenticationService.logout(request);

        // Trả về kết quả theo cấu trúc ApiResponse chuẩn của dự án
        return ApiResponse.<Void>builder()
                .build();
    }
}