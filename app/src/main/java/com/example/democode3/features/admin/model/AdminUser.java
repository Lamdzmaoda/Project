package com.example.democode3.features.admin.model;

import com.example.democode3.core.enums.RoleType;

public class AdminUser {

    public long id;

    public String username;

    public RoleType role;

    public int xp;

    public AdminUser(
            long id,
            String username,
            RoleType role,
            int xp
    ) {

        this.id = id;
        this.username = username;
        this.role = role;
        this.xp = xp;
    }
}