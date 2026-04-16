/* (C)2026 */
package com.example.identity_servive.entity;

import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Class Entity đại diện cho bảng 'user' trong Database. Sử dụng JPA (Java Persistence API) để ánh
 * xạ các thuộc tính vào các cột của bảng.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // Lombok: Mặc định mọi trường là 'private'
@Entity
public class UserProgress {

  /**
   * Khóa chính (Primary Key) của bảng. @GeneratedValue: Tự động tạo giá trị cho ID. strategy =
   * GenerationType.UUID: Sử dụng chuỗi định danh duy nhất toàn cầu (UUID) thay vì số tự động tăng
   * (giúp bảo mật và dễ scale hệ thống).
   */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;
  @Column(nullable = false)
  String userID;

  @ManyToOne
  @JoinColumn(name = "step_id")
  Step step;

  @Column(nullable = false)
  IsCompleted completedStatus;

  @Column(nullable = false)
  IsLocked isLocked;

  @Column(nullable = false)
  double earnedXp;

  @CreationTimestamp
  LocalDateTime completedAt;
}
