package com.example.identity_servive.dto.response.learningResponse;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StepVerificationResult {
    boolean isCorrect;
    String logs;         // Chứa Output từ Console hoặc Message lỗi
    Object expectedValue; // Giá trị mong đợi từ DB (data của Step)
}
