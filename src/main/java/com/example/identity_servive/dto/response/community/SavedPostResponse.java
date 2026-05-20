package com.example.identity_servive.dto.response.community;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SavedPostResponse {
    String id;
    String postId;
    PostResponse post;
    LocalDateTime createdAt;
}
