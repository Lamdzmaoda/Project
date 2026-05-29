package com.example.democode3.features.fake.model;

public class FakePost {

    public String id;

    public String userId;

    public String username;

    public String userAvatar;

    public String title;

    public String content;

    public String imageUrl;

    public String codeSnippet;

    public int likeCount;

    public int commentCount;

    public boolean likedByMe;

    public boolean savedByMe;

    public String createdAt;

    public FakePost(
            String id,
            String userId,
            String username,
            String userAvatar,
            String title,
            String content,
            String imageUrl,
            String codeSnippet,
            int likeCount,
            int commentCount,
            boolean likedByMe,
            boolean savedByMe,
            String createdAt
    ) {

        this.id = id;

        this.userId = userId;

        this.username = username;

        this.userAvatar = userAvatar;

        this.title = title;

        this.content = content;

        this.imageUrl = imageUrl;

        this.codeSnippet = codeSnippet;

        this.likeCount = likeCount;

        this.commentCount = commentCount;

        this.likedByMe = likedByMe;

        this.savedByMe = savedByMe;

        this.createdAt = createdAt;
    }
}