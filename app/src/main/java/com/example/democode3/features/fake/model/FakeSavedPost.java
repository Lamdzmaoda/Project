package com.example.democode3.features.fake.model;

public class FakeSavedPost {

    public String postId;

    public String title;

    public String username;

    public String savedAt;

    public FakeSavedPost(
            String postId,
            String title,
            String username,
            String savedAt
    ) {

        this.postId = postId;

        this.title = title;

        this.username = username;

        this.savedAt = savedAt;
    }
}