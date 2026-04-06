package com.example.identity_servive.controller;

import com.example.identity_servive.dto.request.ApiResponse;
import com.example.identity_servive.dto.request.CodeRequest;
import com.example.identity_servive.dto.request.PythonRequest;
import com.example.identity_servive.dto.response.CodeResponse;
import com.example.identity_servive.service.PistonAPISevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/piston")
@RestController
public class PistonAPIController {
    @Autowired
    PistonAPISevice pistonAPISevice;

    @PostMapping
    public ApiResponse<CodeResponse> runTest(@RequestBody CodeRequest request) {
        // Lấy code từ JSON request và gửi sang Service
        return ApiResponse.<CodeResponse>builder()
                .result(pistonAPISevice.executePythonCode(request))
                .build();
    }
}
