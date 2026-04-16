package com.example.identity_servive.dto.response;

import com.example.identity_servive.enums.Role;

import java.time.LocalDateTime;
import java.util.List;

public record ChatResponse(
        String userMessage,
        String aiExplanation,
        List<String> hints,
        String suggestedCode,
        String motivationMessage,
        LocalDateTime createAt) {
}
