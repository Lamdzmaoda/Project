/* (C)2026 */
package com.example.identity_servive.entity.learning;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // Lombok: Mặc định mọi trường là 'private'
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"problem_id", "orderIndex"})})
public class ProblemCondition {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @ManyToOne
  @JoinColumn(name = "problem_id", nullable = false)
  Problem problem;

  @Column(columnDefinition = "TEXT", nullable = false)
  String expectedCode; // Dòng code kỳ vọng

  @Column(columnDefinition = "TEXT")
  String hint; // Gợi ý nếu thiếu

  int orderIndex;
}
