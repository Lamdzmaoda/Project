package com.example.identity_servive.entity.community;

import com.example.identity_servive.entity.auth.User;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    Post post;                    // Bài viết được bình luận

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;                   // Người bình luận

    @ManyToOne
    @JoinColumn(name = "parent_id")
    Comment parent;

    @Column(columnDefinition = "LONGTEXT")
    String content;               // Nội dung bình luận

    @CreationTimestamp
    LocalDateTime createdAt;
}
