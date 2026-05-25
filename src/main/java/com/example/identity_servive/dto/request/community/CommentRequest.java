/* (C)2026 */
package com.example.identity_servive.dto.request.community;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequest {
  String postId;
  String content;
  String parentId; // null nếu là comment gốc, có id nếu là reply
}
