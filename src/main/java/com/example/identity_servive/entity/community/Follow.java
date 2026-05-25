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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"follower_id", "followee_id"})})
public class Follow {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @ManyToOne
  @JoinColumn(name = "follower_id", nullable = false)
  User follower; // Người theo dõi

  @ManyToOne
  @JoinColumn(name = "followee_id", nullable = false)
  User followee;

  @CreationTimestamp LocalDateTime createdAt;
}
