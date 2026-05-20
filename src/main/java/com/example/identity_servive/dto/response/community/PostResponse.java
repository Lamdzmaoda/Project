package com.example.identity_servive.dto.response.community;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang
// Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    String id;
    String userId;
    String username;
    String userAvatar;
    String content;
    String imageUrl;
    String codeSnippet;
    int likeCount;
    int commentCount;
    boolean likedByMe;
    boolean savedByMe;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
