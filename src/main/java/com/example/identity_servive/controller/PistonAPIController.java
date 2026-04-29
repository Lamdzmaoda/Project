package com.example.identity_servive.controller;

import com.example.identity_servive.dto.request.ApiResponse;
import com.example.identity_servive.dto.request.CodeRequest;
import com.example.identity_servive.dto.response.CodeResponse;
import com.example.identity_servive.service.Judge0APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/piston")
@RestController
public class PistonAPIController {
    @Autowired
    Judge0APIService judge0APIService;

    @PostMapping
    public ApiResponse<CodeResponse> runTest(@RequestBody CodeRequest request) {
        // Lấy code từ JSON request và gửi sang Service
        return ApiResponse.<CodeResponse>builder()
                .result(judge0APIService.executePythonCode(request))
                .build();
    }
}
