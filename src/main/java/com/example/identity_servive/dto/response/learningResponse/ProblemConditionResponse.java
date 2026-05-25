/* (C)2026 */
package com.example.identity_servive.dto.response.learningResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProblemConditionResponse {
  String expectedCode;
  String hint;
  int orderIndex;
  boolean passed;
}
