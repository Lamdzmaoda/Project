/* (C)2026 */
package com.example.identity_servive.entity.System;

import com.example.identity_servive.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Code {

  /**
   * Khóa chính (Primary Key) của bảng. @GeneratedValue: Tự động tạo giá trị cho ID. strategy =
   * GenerationType.UUID: Sử dụng chuỗi định danh duy nhất toàn cầu (UUID) thay vì số tự động tăng
   * (giúp bảo mật và dễ scale hệ thống).
   */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  String language; // Ngôn ngữ dùng (python, java, c++)
  Status status;
  String errorType;
  Integer line;
  Integer columnIndex;
  String errorLineCode;
  String pointer;
  String messageVn;
  String input;
  String output;
}
