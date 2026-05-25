package com.example.democode3.features.community.model;

import java.io.Serializable;

public class Post implements Serializable {

    // =====================================
    // ID
    // =====================================

    public String id;

    // =====================================
    // USER
    // =====================================

    public String userId;

    public String username;

    public String userAvatar;

    // =====================================
    // CONTENT
    // =====================================

    public String title;

    public String content;

    public String imageUrl;

    public String codeSnippet;

    // =====================================
    // COUNT
    // =====================================

    public int likeCount;

    public int commentCount;

    // =====================================
    // STATE
    // =====================================

    public boolean likedByMe;

    public boolean savedByMe;

    // =====================================
    // TIME
    // =====================================

    public String createdAt;

    public String updatedAt;
}