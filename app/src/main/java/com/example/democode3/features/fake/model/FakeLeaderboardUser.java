package com.example.democode3.features.fake.model;

public class FakeLeaderboardUser {

    public String userId;

    public String username;

    public String avatar;

    public int xp;

    public int level;

    public int streak;

    public int solvedProblems;

    public int rank;

    public FakeLeaderboardUser(
            String userId,
            String username,
            String avatar,
            int xp,
            int level,
            int streak,
            int solvedProblems,
            int rank
    ) {

        this.userId = userId;

        this.username = username;

        this.avatar = avatar;

        this.xp = xp;

        this.level = level;

        this.streak = streak;

        this.solvedProblems = solvedProblems;

        this.rank = rank;
    }
}