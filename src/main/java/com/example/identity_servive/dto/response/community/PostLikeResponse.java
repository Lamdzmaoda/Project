/* (C)2026 */
package com.example.identity_servive.dto.response.community;

import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostLikeResponse {
  String id;
  String postId;
  String userId;
  String username;
  LocalDateTime createdAt;
}
