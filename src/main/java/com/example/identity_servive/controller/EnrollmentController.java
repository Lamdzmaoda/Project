package com.example.identity_servive.controller;

import com.example.identity_servive.dto.request.ApiResponse;
import com.example.identity_servive.dto.request.EnrollmentRequest;
import com.example.identity_servive.dto.response.EnrollmentResponse;
import com.example.identity_servive.service.EnrollmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j // Hỗ trợ ghi log để theo dõi luồng dữ liệu
@RestController // Đánh dấu là REST Controller, tự động chuyển kết quả trả về thành JSON
 // Định nghĩa đường dẫn gốc cho các API trong class này là /permissions
@RequiredArgsConstructor // Tự động tạo Constructor để Inject PermissionService
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentController {
    EnrollmentService enrollmentService;

    @PostMapping("/enrollment")
    ApiResponse<EnrollmentResponse> portEnrollment(@RequestBody EnrollmentRequest request) {
        // 1. Nhận dữ liệu từ Client, đẩy xuống Service để lưu vào DB
        // 2. Bọc kết quả trả về vào đối tượng ApiResponse chuẩn
        return ApiResponse.<EnrollmentResponse>builder()
                .result(enrollmentService.enrollCourse(request))
                .build();
    }
}