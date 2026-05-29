package com.example.democode3.features.fake.model;

public class FakeAchievement {

    public String id;

    public String title;

    public String description;

    public String icon;

    public boolean unlocked;

    public int progress;

    public int maxProgress;

    public String unlockedAt;

    public FakeAchievement(
            String id,
            String title,
            String description,
            String icon,
            boolean unlocked,
            int progress,
            int maxProgress,
            String unlockedAt
    ) {

        this.id = id;

        this.title = title;

        this.description = description;

        this.icon = icon;

        this.unlocked = unlocked;

        this.progress = progress;

        this.maxProgress = maxProgress;

        this.unlockedAt = unlockedAt;
    }
}