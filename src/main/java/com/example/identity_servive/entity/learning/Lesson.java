package com.example.identity_servive.entity.learning;

import com.example.identity_servive.enums.ContentStatus;
import com.example.identity_servive.enums.LessonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;
import java.util.Set;

/**
 * Entity Lesson đại diện cho một bài học trong một Chương (Chapter).
 * Một bài học chứa nhiều bước nhỏ (Step).
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"chapter_id", "orderIndex"})
})
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(nullable = false)
    String title;
    String description;
    String slug;                   // Đường dẫn URL
    String thumbnailUrl;     // Ảnh thumbnail
    int orderIndex;
    @Builder.Default
    long xp = 0;
    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_type")
    LessonType lessonType;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    ContentStatus status = ContentStatus.ACTIVE; // Mặc định là ACTIVE
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    Chapter chapter;
    @CreationTimestamp
    LocalDateTime createAt;
    @UpdateTimestamp
    LocalDateTime updateAt;
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    Set<Step> steps;
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    Set<Problem> problems;


}