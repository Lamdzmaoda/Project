package com.example.democode3.features.auth.api.response;

public class LoginResponse {

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

    public LoginResult result;

    // =====================================
    // INNER RESULT
    // =====================================

    public static class LoginResult {

        public boolean authenticated;

        public String token;
    }
}