/* (C)2026 */
package com.example.identity_servive.entity;

import com.example.identity_servive.enums.ContentStatus;
import com.example.identity_servive.enums.IsLocked;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

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
public class Chapter {

    /**
     * Khóa chính (Primary Key) của bảng sử dụng UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(nullable = false)
    String title;

    int orderIndex;


    @Enumerated(EnumType.STRING)
    @Column(name = "is_locked")
    @Builder.Default
    IsLocked lockedStatus = IsLocked.FALSE_LOCKED;

    /**
     * Quan hệ Nhiều - Một trỏ về Course.
     * JoinColumn định nghĩa cột 'course_id' trong bảng chapter để lưu khóa ngoại.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    ContentStatus status = ContentStatus.ACTIVE; // Mặc định là ACTIVE
    @ManyToOne
    @JoinColumn(name = "language_id")
    Language language;
    @CreationTimestamp
    LocalDateTime createAt;
    /**
     * Quan hệ Một - Nhiều với Lesson.
     * mappedBy = "chapter": Đối chiếu với trường 'chapter' bên class Lesson.
     * cascade = CascadeType.ALL: Tự động lưu/xóa các Lesson con khi thao tác với Chapter.
     */
    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Lesson> lessons;
}