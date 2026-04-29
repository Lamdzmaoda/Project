package com.example.identity_servive.entity;

import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // Lombok: Mặc định mọi trường là 'private'
@Entity
public class UserStepProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "step_id", nullable = false)
    Step step;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_locked", nullable = false)
    @Builder.Default
    IsLocked lockedStatus = IsLocked.TRUE_LOCKED;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_completed", nullable = false)
    @Builder.Default
    IsCompleted completedStatus = IsCompleted.FALSE;

    @Builder.Default
    double earnedXp = 0.0;

    @CreationTimestamp
    LocalDateTime completedAt;
}
