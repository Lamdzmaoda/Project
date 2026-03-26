package com.example.appcodetest.model;

public class AuthResponse {
    public int code;
    public Result result;

    public class Result {
        public boolean authenticated;
        public String token;
    }
}