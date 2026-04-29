package com.example.identity_servive.entity;

import com.example.identity_servive.enums.ContentStatus;
import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

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

public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String title;

    /**
     * Thứ tự của bài học trong chương.
     */
    int orderIndex;

    /**
     * Điểm kinh nghiệm nhận được khi hoàn thành bài học này.
     */
    @Builder.Default
    double xp = 0.0;

    /**
     * Tiến độ hoàn thành bài học (Ví dụ: 0.5 tương đương 50%).
     */
    @Builder.Default
    double progress = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_locked", nullable = false)
    @Builder.Default
    IsLocked lockedStatus = IsLocked.FALSE_LOCKED;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_completed", nullable = false)
    @Builder.Default
    IsCompleted completedStatus = IsCompleted.FALSE;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    ContentStatus status = ContentStatus.ACTIVE; // Mặc định là ACTIVE
    /**
     * Nội dung tóm tắt bài học (Markdown).
     */
    @Column(columnDefinition = "LONGTEXT")
    String contentMarkdown;

    /**
     * Quan hệ N-1: Nhiều Lesson thuộc về 1 Chapter.
     */
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    Chapter chapter;
    @CreationTimestamp
    LocalDateTime createAt;
    /**
     * Quan hệ 1-N: Một Lesson có nhiều Step.
     * mappedBy = "lesson": Khớp với trường 'lesson' trong class Step.java.
     */
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Step> steps;

}