package com.example.identity_servive.controller.learning;

import com.example.identity_servive.dto.response.ApiResponse;
import com.example.identity_servive.dto.request.ai.CodeRequest;
import com.example.identity_servive.dto.response.ai.CodeResponse;
import com.example.identity_servive.service.system.Judge0APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/code")
@RestController
public class PistonAPIController {
    @Autowired
    Judge0APIService judge0APIService;

    @PostMapping("/run")
    public ApiResponse<CodeResponse> runTest(@RequestBody CodeRequest request) {
        // Lấy code từ JSON request và gửi sang Service
        return ApiResponse.<CodeResponse>builder()
                .result(judge0APIService.executePythonCode(request))
                .build();
    }
}
