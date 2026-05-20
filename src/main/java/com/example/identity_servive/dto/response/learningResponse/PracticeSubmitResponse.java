package com.example.identity_servive.dto.response.learningResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PracticeSubmitResponse {
    boolean passed;
    long xp;
    String message;
}
