package com.example.appcodetest.model;

import java.util.List;

public class UserResponse {
    public String id;
    public String username;
    public String firstName;
    public String lastName;
    public String email;
    public String birthDate;

    public List<Role> roles; // 🔥 FIX

    public static class Role {
        public String name;
        public String description;
    }
}