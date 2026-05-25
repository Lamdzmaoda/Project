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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "user_id"})})
public class PostLike {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  User user; // Người like

  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false)
  Post post; // Bài được like

  @CreationTimestamp LocalDateTime createdAt;
}
