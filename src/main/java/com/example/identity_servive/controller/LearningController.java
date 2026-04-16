package com.example.identity_servive.controller;


import com.example.identity_servive.dto.request.ApiResponse;
import com.example.identity_servive.dto.request.LessonBatchRequest;
import com.example.identity_servive.dto.response.LessonBatchResponse;
import com.example.identity_servive.service.LearningService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j // Hỗ trợ ghi log để theo dõi luồng dữ liệu
@RestController // Đánh dấu là REST Controller, tự động chuyển kết quả trả về thành JSON
@RequestMapping("/course")
// Định nghĩa đường dẫn gốc cho các API trong class này là /permissions
@RequiredArgsConstructor // Tự động tạo Constructor để Inject PermissionService
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LearningController {
    LearningService learningService;

    @PostMapping("/verify")
    ApiResponse<LessonBatchResponse> verifyLesson(@RequestBody LessonBatchRequest request) {
        return ApiResponse.<LessonBatchResponse>builder()
                .result(learningService.verifyLesson(request))
                .build();
    }

}
