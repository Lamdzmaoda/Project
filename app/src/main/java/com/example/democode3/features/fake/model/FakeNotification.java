package com.example.democode3.features.fake.model;

public class FakeNotification {

    public String id;

    public String type;

    public String username;

    public String avatar;

    public String content;

    public boolean isRead;

    public String createdAt;

    public FakeNotification(
            String id,
            String type,
            String username,
            String avatar,
            String content,
            boolean isRead,
            String createdAt
    ) {

        this.id = id;

        this.type = type;

        this.username = username;

        this.avatar = avatar;

        this.content = content;

        this.isRead = isRead;

        this.createdAt = createdAt;
    }
}