package com.example.identity_servive.dto.response.community;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowResponse {
    String id;
    String followerId;
    String followerName;
    String followerAvatar;
    String followeeId;
    String followeeName;
    String followeeAvatar;
    LocalDateTime createdAt;
}
