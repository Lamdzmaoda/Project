/* (C)2026 */
package com.example.identity_servive.entity.auth;


import com.example.identity_servive.enums.Status;
import com.example.identity_servive.exception.ErrorCode;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "users")
public class User {

  /**
   * Khóa chính (Primary Key) của bảng. @GeneratedValue: Tự động tạo giá trị cho ID. strategy =
   * GenerationType.UUID: Sử dụng chuỗi định danh duy nhất toàn cầu (UUID) thay vì số tự động tăng
   * (giúp bảo mật và dễ scale hệ thống).
   */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  // Tên đăng nhập (Username)
  @Column(
      name = "username",
      unique = true
  )
  String username;

  // Mật khẩu (Lưu ý: Trong thực tế, trường này PHẢI lưu mật khẩu đã băm/hash) ;
  String password;
  @Email
  @NonNull
  String email;

  String displayName;

    String avatarUrl;        // Ảnh đại diện

    String bio;              // Tiểu sử

    int coin = 0;           // Tiền trong game

    int longestStreak = 0;  // Streak cao nhất

    boolean verified = false;  // Đã xác minh email

    @Builder.Default
    Status status = Status.ACTIVE;  // ACTIVE / BANNED / DISABLED

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;
  static final int XP_PER_LEVEL = 1000;

  @Builder.Default
  int streak = 0;

  LocalDate lastActivityDate;

  // Ngày sinh (Ánh xạ kiểu DATE trong Database)
  LocalDate birthDate;

  @Builder.Default
  @Column(name = "total_xp")
  Long totalXp = 0L;

  @ManyToMany Set<Role> roles;
  
  @Transient
  public int getLevel() {
    if (totalXp == null) return 1;
    return (int) (this.totalXp / XP_PER_LEVEL) + 1;
  }
}
