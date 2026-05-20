/* (C)2026 */
package com.example.identity_servive.entity.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
public class Role {

  /**
   * Khóa chính (Primary Key) của bảng. @GeneratedValue: Tự động tạo giá trị cho ID. strategy =
   * GenerationType.UUID: Sử dụng chuỗi định danh duy nhất toàn cầu (UUID) thay vì số tự động tăng
   * (giúp bảo mật và dễ scale hệ thống).
   */
  @Id String name;

  // Tên đăng nhập (Username)
  String description;

  @ManyToMany Set<Permission> permissions;
}
