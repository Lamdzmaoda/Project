/* (C)2026 */
package com.example.identity_servive.entity.learning;

import com.example.identity_servive.enums.ContentStatus;
import com.example.identity_servive.enums.Difficulty;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"lesson_id", "orderIndex"})})
public class Problem {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  String title; // Tiêu đề bài toán
  String slug; // Đường dẫn URL

  @Column(columnDefinition = "TEXT")
  String description; // Mô tả bài toán (markdown)

  @Enumerated(EnumType.STRING)
  Difficulty difficulty; // Độ khó: EASY, MEDIUM, HARD

  @Column(columnDefinition = "TEXT")
  String hint; // Gợi ý (nếu có)

  String methodName; // Tên hàm user phải viết, vd: add
  int orderIndex;

  @Column(columnDefinition = "TEXT")
  String solutionCode; // Code mẫu (đáp án)

  String expectedOutput; // Đáp án mong đợi, vd: "8"

  @ManyToOne
  @JoinColumn(name = "language_id")
  Language language; // Ngôn ngữ lập trình (Python, Java...)

  // Liên kết với Lesson (PRACTICE type) - có thể null nếu là bài tập standalone
  @ManyToOne
  @JoinColumn(name = "lesson_id", nullable = false)
  Lesson lesson;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  ContentStatus status = ContentStatus.ACTIVE;

  @CreationTimestamp LocalDateTime createAt;
  @UpdateTimestamp LocalDateTime updateAt;

  @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("orderIndex ASC")
  Set<ProblemCondition> conditions;
}
