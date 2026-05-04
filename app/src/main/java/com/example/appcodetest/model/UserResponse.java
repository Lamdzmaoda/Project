package com.example.appcodetest.model;

import java.util.List;
import java.util.Set;

public class UserResponse {

    public String id;
    public String username;
    public String firstName;
    public String lastName;
    public String email;
    public String birthDate;

    public int streak;
    public int level;

    public Set<Role> roles;

    // =========================
    // 🔥 HELPER
    // =========================

    public String getFullName() {
        return (firstName != null ? firstName : "") + " " +
                (lastName != null ? lastName : "");
    }

    public String getRoleName() {
        if (roles != null && !roles.isEmpty()) {
            return roles.iterator().next().name; // 🔥 Set phải dùng iterator
        }
        return "USER";
    }

    public String getPermission() {
        return ""; // backend không có permission → bỏ
    }

    // =========================
    // 🔥 ROLE MODEL CHUẨN BACKEND
    // =========================

    public static class Role {
        public String name;
    }
}