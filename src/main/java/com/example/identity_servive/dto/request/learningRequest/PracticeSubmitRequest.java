/* (C)2026 */
package com.example.identity_servive.dto.request.learningRequest;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PracticeSubmitRequest {
  @Column(columnDefinition = "TEXT")
  String problemId;

  @Column(columnDefinition = "TEXT")
  String code;
}
