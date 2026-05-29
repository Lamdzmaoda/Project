package com.example.democode3.features.fake.model;

public class FakeComment {

    public String id;

    public String postId;

    public String userId;

    public String username;

    public String avatar;

    public String content;

    public int likeCount;

    public boolean likedByMe;

    public String createdAt;

    public FakeComment(
            String id,
            String postId,
            String userId,
            String username,
            String avatar,
            String content,
            int likeCount,
            boolean likedByMe,
            String createdAt
    ) {

        this.id = id;

        this.postId = postId;

        this.userId = userId;

        this.username = username;

        this.avatar = avatar;

        this.content = content;

        this.likeCount = likeCount;

        this.likedByMe = likedByMe;

        this.createdAt = createdAt;
    }
}