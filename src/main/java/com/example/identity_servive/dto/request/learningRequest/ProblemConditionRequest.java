/* (C)2026 */
package com.example.identity_servive.dto.request.learningRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProblemConditionRequest {
  String expectedCode;
  String hint;
  int orderIndex;
}
