package com.example.identity_servive.entity;

import com.example.identity_servive.enums.ContentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // Lombok: Mặc định mọi trường là 'private'
@Entity
public class Language {
    @Id
    @Column(
            name = "language_Name",
            unique = true
            )
    String name;
    String description;
    String icon;
    int level;
    LocalDateTime durationDays;
    double currentXp;
    double totalXp;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    ContentStatus status = ContentStatus.ACTIVE; // Mặc định là ACTIVE
    @CreationTimestamp
    LocalDateTime createAt;
    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Chapter> chapters;
}
