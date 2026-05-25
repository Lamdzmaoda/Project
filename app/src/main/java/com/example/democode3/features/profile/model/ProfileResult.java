package com.example.democode3.features.profile.model;

import java.util.List;

public class ProfileResult {

    // =====================================
    // BASIC
    // =====================================

    public long id;

    public String username;

    public String displayName;

    public String email;

    // =====================================
    // PROFILE
    // =====================================

    public String avatarUrl;

    public String bio;

    // =====================================
    // GAME
    // =====================================

    public int xp;

    public int coin;

    public int level;

    public int streak;

    // =====================================
    // VERIFIED
    // =====================================

    public boolean verified;

    // =====================================
    // ROLES
    // =====================================

    public List<UserProfileResponse.RoleResponse> roles;
}