package com.example.identity_servive.entity.progress;

import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Language;
import com.example.identity_servive.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // Lombok: Mặc định mọi trường là 'private'
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "language_id"}) })
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    long currentXp;

    @Enumerated(EnumType.STRING)
    Status status;
    @Builder.Default
    double progressPercentage = 0;
    @CreationTimestamp
    LocalDateTime enrolledAt;

    LocalDateTime completedAt;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;
    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    Language language;
}
