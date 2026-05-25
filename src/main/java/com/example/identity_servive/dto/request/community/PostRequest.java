/* (C)2026 */
package com.example.identity_servive.dto.request.community;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequest {
  String title;
  String content;
  String imageUrl;
  String codeSnippet;
}
