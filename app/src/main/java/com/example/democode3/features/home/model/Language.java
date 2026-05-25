// features/home/model/Language.java

package com.example.democode3.features.home.model;

import com.example.democode3.core.enums.Difficulty;

public class Language {

    // =====================================
    // ID
    // =====================================

    public long id;

    // =====================================
    // NAME
    // =====================================

    public String name;

    // =====================================
    // SLUG
    // =====================================

    public String slug;

    // =====================================
    // ICON
    // =====================================

    public String iconUrl;

    // =====================================
    // DESCRIPTION
    // =====================================

    public String description;

    // =====================================
    // DIFFICULTY
    // =====================================

    public Difficulty difficulty;

    // =====================================
    // CREATED
    // =====================================

    public long createdAt;

    // =====================================
    // UI HELPER
    // =====================================

    public boolean enrolled;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public Language(
            long id,
            String name,
            String slug,
            String iconUrl,
            String description,
            Difficulty difficulty,
            long createdAt,
            boolean enrolled
    ) {

        this.id = id;

        this.name = name;

        this.slug = slug;

        this.iconUrl = iconUrl;

        this.description = description;

        this.difficulty = difficulty;

        this.createdAt = createdAt;

        this.enrolled = enrolled;
    }
}