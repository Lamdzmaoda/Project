/* (C)2026 */
package com.example.identity_servive.entity.progress;

import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Chapter;
import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // Lombok: Mặc định mọi trường là 'private'
@Entity
public class UserChapterProgress {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  User user;

  @ManyToOne
  @JoinColumn(name = "chapter_id", nullable = false)
  Chapter chapter;

  double progressPercentage = 0;

  @Enumerated(EnumType.STRING)
  @Column(name = "is_locked", nullable = false)
  @Builder.Default
  IsLocked lockedStatus = IsLocked.TRUE_LOCKED;

  @Enumerated(EnumType.STRING)
  @Column(name = "is_completed", nullable = false)
  @Builder.Default
  IsCompleted completedStatus = IsCompleted.FALSE;

  @CreationTimestamp LocalDateTime createdAt;

  @UpdateTimestamp LocalDateTime updatedAt;
}
