/* (C)2026 */
package com.example.identity_servive.entity.community;

import com.example.identity_servive.entity.auth.User;
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
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  User user; // Người tạo bài

  String title;

  @Column(columnDefinition = "LONGTEXT")
  String content; // Nội dung bài viết

  String imageUrl; // Ảnh đính kèm

  @Column(columnDefinition = "LONGTEXT")
  String codeSnippet; // Code snippet (nếu có)

  @Builder.Default int likeCount = 0; // Số lượt like

  @Builder.Default int commentCount = 0; // Số lượt bình luận

  @CreationTimestamp LocalDateTime createdAt;

  @UpdateTimestamp LocalDateTime updatedAt;
}
