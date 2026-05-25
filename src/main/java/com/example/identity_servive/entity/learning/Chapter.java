/* (C)2026 */
package com.example.identity_servive.entity.learning;

import com.example.identity_servive.enums.ContentStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Class Entity đại diện cho bảng 'chapter' trong Database.
 * Một Chương (Chapter) thuộc về một Khóa học (Course) và chứa nhiều Bài học (Lesson).
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // Lombok: Mặc định mọi trường là 'private'
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"language_id", "orderIndex"})})
public class Chapter {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(nullable = false)
  String title;

  int orderIndex;
  String description; // Mô tả chương
  String slug;
  // Chapter.java — thêm field
  @Builder.Default Long totalXp = 0L; // XP chapter có

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  @Builder.Default
  ContentStatus status = ContentStatus.ACTIVE; // Mặc định là ACTIVE

  @ManyToOne
  @JoinColumn(name = "language_id")
  Language language;

  @CreationTimestamp LocalDateTime createAt;
  @UpdateTimestamp LocalDateTime updateAt;

  @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("orderIndex ASC")
  Set<Lesson> lessons;
}
