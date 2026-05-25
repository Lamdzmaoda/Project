package com.example.democode3.features.profile.model;

import java.util.List;

public class UserProfileResponse {

    public long id;

    public String username;

    public String displayName;

    public String email;

    public String avatarUrl;

    public String bio;

    public int xp;

    public int coin;

    public int level;

    public int streak;

    public boolean verified;

    public List<RoleResponse> roles;

    public static class RoleResponse {

        public String name;

        public String description;
    }
}