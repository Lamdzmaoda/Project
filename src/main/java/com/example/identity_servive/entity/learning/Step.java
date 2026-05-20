/* (C)2026 */
package com.example.identity_servive.entity.learning;

import com.example.identity_servive.enums.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Class Entity đại diện cho bảng 'lesson' trong Database.
 * Mỗi bài học (Lesson) thuộc về một Chương (Chapter) cụ thể.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // Lombok: Mặc định mọi trường là 'private'
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"lesson_id", "orderIndex"})
})
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    int orderIndex;        // Thứ tự trong lesson
    @Column(nullable = false)
    String title;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    Type type;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    ContentStatus status = ContentStatus.ACTIVE; // Mặc định là ACTIVE
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data")
    Map<String, Object> data;
    @CreationTimestamp
    LocalDateTime createAt;
    @UpdateTimestamp
    LocalDateTime updateAt;
    /**
     * Quan hệ N-1: Nhiều Step thuộc về 1 Lesson.
     * Tên cột khóa ngoại trong DB sẽ là 'lesson_id'.
     */
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    Lesson lesson;

}