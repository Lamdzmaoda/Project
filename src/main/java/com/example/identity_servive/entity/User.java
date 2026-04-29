/* (C)2026 */
package com.example.identity_servive.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
      name = "userName",
      unique = true,
      columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
  String username;

  // Mật khẩu (Lưu ý: Trong thực tế, trường này PHẢI lưu mật khẩu đã băm/hash)
  String password;

  String email;

  // Tên của người dùng
  String firstName;

  // Họ của người dùng
  String lastName;

  static final int XP_PER_LEVEL = 1000;

  @Builder.Default
  int streak = 0;

  LocalDate lastActivityDate;

  // Ngày sinh (Ánh xạ kiểu DATE trong Database)
  LocalDate birthDate;

  @Builder.Default
  @Column(name = "total_xp")
  Double totalXp = 0.0;

  @ManyToMany Set<Role> roles;
  
  @Transient
  public int getLevel() {
    if (totalXp == null) return 1;
    return (int) (this.totalXp / XP_PER_LEVEL) + 1;
  }
}
