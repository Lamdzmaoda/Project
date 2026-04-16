package com.example.identity_servive.controller;

import com.example.identity_servive.dto.request.ApiResponse;
import com.example.identity_servive.dto.request.ChatRequest;
import com.example.identity_servive.dto.response.ChatResponse;
import com.example.identity_servive.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j // Hỗ trợ ghi log để theo dõi luồng dữ liệu
@RestController // Đánh dấu là REST Controller, tự động chuyển kết quả trả về thành JSON // Định nghĩa đường dẫn gốc cho các API trong class này là /permissions
@RequiredArgsConstructor // Tự động tạo Constructor để Inject PermissionService
@RequestMapping("/chats")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
    ChatService chatService;
    @PostMapping
    ApiResponse<ChatResponse> chat(@RequestBody ChatRequest chatRequest) throws JsonProcessingException {
        return ApiResponse.<ChatResponse>builder()
                .result(chatService.chat(chatRequest))
                .build();
    }
    @GetMapping
    ApiResponse<List<ChatResponse>> getChat(){
        return ApiResponse.<List<ChatResponse>>builder()
                .result(chatService.getHistorys())
                .build();
    }
    @GetMapping("/user")
    ApiResponse<List<ChatResponse>> getChatOfUser(){
        return ApiResponse.<List<ChatResponse>>builder()
                .result(chatService.getHistoryByUser())
                .build();
    }

}
