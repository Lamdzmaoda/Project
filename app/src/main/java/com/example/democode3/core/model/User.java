package com.example.democode3.core.model;

import com.example.democode3.core.enums.RoleType;
import com.example.democode3.core.enums.UserStatus;

public class User {

    public String id;

    public String email;

    public String username;

    public String displayName;

    public String avatarUrl;

    public String bio;

    public RoleType role;

    public UserStatus status;

    public boolean verified;

    public int xp;

    public int coin;

    public int level;

    public int streak;

    public int longestStreak;

    public Long lastStudyDate;

    public long createdAt;

    public long updatedAt;

    public Long lastOnlineAt;
}