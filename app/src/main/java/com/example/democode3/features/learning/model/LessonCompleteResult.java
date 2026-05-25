package com.example.democode3.features.learning.model;

public class LessonCompleteResult {

    public boolean completed;

    public int xpReward;

    public int coinReward;

    public long nextLessonId;

    public LessonCompleteResult(
            boolean completed,
            int xpReward,
            int coinReward,
            long nextLessonId
    ) {

        this.completed = completed;

        this.xpReward = xpReward;

        this.coinReward = coinReward;

        this.nextLessonId = nextLessonId;
    }
}