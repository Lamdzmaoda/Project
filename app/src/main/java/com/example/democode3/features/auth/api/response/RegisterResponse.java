package com.example.democode3.features.auth.api.response;

public class RegisterResponse {

    // =====================================
    // CODE
    // =====================================

    public int code;

    // =====================================
    // MESSAGE
    // =====================================

    public String message;

    // =====================================
    // RESULT
    // =====================================

    public UserResult result;

    // =====================================
    // USER RESULT
    // =====================================

    public static class UserResult {

        public String id;

        public String username;

        public String displayName;

        public String email;

        public int xp;

        public int coin;

        public int level;
    }
}