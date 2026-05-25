package com.example.democode3.features.home.model;

import com.example.democode3.core.enums.Difficulty;

public class Lesson {

    // =====================================
    // ID
    // =====================================

    public long id;

    // =====================================
    // CHAPTER
    // =====================================

    public long chapterId;

    // =====================================
    // TITLE
    // =====================================

    public String title;

    // =====================================
    // DESCRIPTION
    // =====================================

    public String description;

    // =====================================
    // DIFFICULTY
    // =====================================

    public Difficulty difficulty;

    // =====================================
    // ESTIMATED MINUTE
    // =====================================

    public int estimatedMinute;

    // =====================================
    // THUMBNAIL
    // =====================================

    public String thumbnailUrl;

    // =====================================
    // ORDER
    // =====================================

    public int orderIndex;

    // =====================================
    // PUBLISHED
    // =====================================

    public boolean published;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public Lesson(
            long id,
            long chapterId,
            String title,
            String description,
            Difficulty difficulty,
            int estimatedMinute,
            String thumbnailUrl,
            int orderIndex,
            boolean published
    ) {

        this.id = id;

        this.chapterId = chapterId;

        this.title = title;

        this.description = description;

        this.difficulty = difficulty;

        this.estimatedMinute = estimatedMinute;

        this.thumbnailUrl = thumbnailUrl;

        this.orderIndex = orderIndex;

        this.published = published;
    }
}