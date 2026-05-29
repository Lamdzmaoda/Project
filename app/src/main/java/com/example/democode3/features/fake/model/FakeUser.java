package com.example.democode3.features.fake.model;

public class FakeUser {

    public String id;

    public String username;

    public String fullName;

    public String avatar;

    public String bio;

    public int xp;

    public int level;

    public int streak;

    public int followers;

    public int following;

    public int solvedProblems;

    public boolean verified;

    public String favoriteLanguage;

    public String githubUrl;

    public String rankTitle;

    public String joinedAt;

    public FakeUser(
            String id,
            String username,
            String fullName,
            String avatar,
            String bio,
            int xp,
            int level,
            int streak,
            int followers,
            int following,
            int solvedProblems,
            boolean verified,
            String favoriteLanguage,
            String githubUrl,
            String rankTitle,
            String joinedAt
    ) {

        this.id = id;

        this.username = username;

        this.fullName = fullName;

        this.avatar = avatar;

        this.bio = bio;

        this.xp = xp;

        this.level = level;

        this.streak = streak;

        this.followers = followers;

        this.following = following;

        this.solvedProblems = solvedProblems;

        this.verified = verified;

        this.favoriteLanguage = favoriteLanguage;

        this.githubUrl = githubUrl;

        this.rankTitle = rankTitle;

        this.joinedAt = joinedAt;
    }
}