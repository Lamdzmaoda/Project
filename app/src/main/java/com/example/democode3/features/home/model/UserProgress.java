// ======================================================
// FILE:
// features/home/model/UserProgress.java
// ======================================================

package com.example.democode3.features.home.model;

public class UserProgress {

    public long id;

    public long userId;

    public long languageId;

    public long chapterId;

    public long lessonId;

    public Long lastStepId;

    public float progress;

    public boolean completed;

    public Long completedAt;

    public long updatedAt;

    public UserProgress(
            long id,
            long userId,
            long languageId,
            long chapterId,
            long lessonId,
            Long lastStepId,
            float progress,
            boolean completed,
            Long completedAt,
            long updatedAt
    ) {

        this.id = id;

        this.userId = userId;

        this.languageId = languageId;

        this.chapterId = chapterId;

        this.lessonId = lessonId;

        this.lastStepId = lastStepId;

        this.progress = progress;

        this.completed = completed;

        this.completedAt = completedAt;

        this.updatedAt = updatedAt;
    }
}