/* (C)2026 */
package com.example.identity_servive.entity;

import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
import com.example.identity_servive.enums.Mode;
import com.example.identity_servive.enums.Type;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    
    @Column(nullable = false)
    String title;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Builder.Default
    Type type = Type.INFO;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode")
    @Builder.Default
    Mode mode = Mode.LEARN;

    int orderIndex;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_locked", nullable = false)
    @Builder.Default
    IsLocked lockedStatus = IsLocked.FALSE_LOCKED;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_completed",  nullable = false)
    @Builder.Default
    IsCompleted completedStatus = IsCompleted.FALSE;

    @Builder.Default
    double xp = 0.0;

    @Builder.Default
    boolean requiredToUnlockNext = false;

    @Column(columnDefinition = "LONGTEXT")
    String data;
    @CreationTimestamp
    LocalDateTime createAt;
    /**
     * Quan hệ N-1: Nhiều Step thuộc về 1 Lesson.
     * Tên cột khóa ngoại trong DB sẽ là 'lesson_id'.
     */
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    Lesson lesson;

}