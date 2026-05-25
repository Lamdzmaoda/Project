// ======================================================
// FILE:
// features/home/model/Chapter.java
// ======================================================

package com.example.democode3.features.home.model;

public class Chapter {

    public long id;

    public long languageId;

    public String title;

    public String description;

    public int orderIndex;

    public Chapter(
            long id,
            long languageId,
            String title,
            String description,
            int orderIndex
    ) {

        this.id = id;

        this.languageId = languageId;

        this.title = title;

        this.description = description;

        this.orderIndex = orderIndex;
    }
}