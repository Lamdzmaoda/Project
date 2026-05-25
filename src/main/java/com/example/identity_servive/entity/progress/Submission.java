/* (C)2026 */
package com.example.identity_servive.entity.progress;

import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Lesson;
import com.example.identity_servive.entity.learning.Problem;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // Lombok: Mặc định mọi trường là 'private'
@Entity
public class Submission {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  User user;

  @ManyToOne
  @JoinColumn(name = "lesson_id", nullable = false)
  Lesson lesson;

  @ManyToOne
  @JoinColumn(name = "problem_id", nullable = false)
  Problem problem;

  @Column(columnDefinition = "LONGTEXT", nullable = false)
  String code;

  @Column(columnDefinition = "TEXT")
  String output; // Output từ Judge0

  boolean passed; // Kết quả tổng thể

  @CreationTimestamp LocalDateTime createdAt;
}
