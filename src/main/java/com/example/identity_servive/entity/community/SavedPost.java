/* (C)2026 */
package com.example.identity_servive.entity.community;

import com.example.identity_servive.entity.auth.User;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "post_id"})})
public class SavedPost {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  User user; // Người lưu

  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false)
  Post post; // Bài được lưu

  @CreationTimestamp LocalDateTime savedAt;
}
