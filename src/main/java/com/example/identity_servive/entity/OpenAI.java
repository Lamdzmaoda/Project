/* (C)2026 */
package com.example.identity_servive.entity;

import com.example.identity_servive.entity.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.awt.image.RasterOp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
public class OpenAI {

  /**
   * Khóa chính (Primary Key) của bảng. @GeneratedValue: Tự động tạo giá trị cho ID. strategy =
   * GenerationType.UUID: Sử dụng chuỗi định danh duy nhất toàn cầu (UUID) thay vì số tự động tăng
   * (giúp bảo mật và dễ scale hệ thống).
   */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(columnDefinition = "TEXT")
    String userMessage;
    @Column(columnDefinition = "TEXT")
    String aiExplanation;
    @ElementCollection
    List<String> hints;
    @Column(columnDefinition = "TEXT")
    String suggestedCode;
    String motivationMessage;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne
    @JoinColumn(name = "step_id")
    Step step;
    @CreationTimestamp
    LocalDateTime createAt;
}
