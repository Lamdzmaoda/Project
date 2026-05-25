package com.example.democode3.features.admin.model;

import java.util.List;

public class UserItem {

    // =====================================
    // BASIC
    // =====================================

    public String id;

    public String username;

    public String displayName;

    public String email;

    // =====================================
    // GAME
    // =====================================

    public int xp;

    public int level;

    // =====================================
    // VERIFIED
    // =====================================

    public boolean verified;

    // =====================================
    // ROLE
    // =====================================

    public List<?> roles;
}