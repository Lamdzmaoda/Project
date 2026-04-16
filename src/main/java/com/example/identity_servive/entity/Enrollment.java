package com.example.identity_servive.entity;

import com.example.identity_servive.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

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
    double currentXp;

    @Enumerated(EnumType.STRING)
    Status status;
    @Builder.Default
    Double progressPercentage = 0.0;
    @CreationTimestamp
    LocalDateTime enrolledAt;

    LocalDateTime completedAt;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    Language language;
}
