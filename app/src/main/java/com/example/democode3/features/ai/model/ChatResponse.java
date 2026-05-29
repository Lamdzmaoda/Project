package com.example.democode3.features.ai.model;

public class ChatResponse {

    public int code;

    public String message;

    public Result result;

    public static class Result {

        public String aiExplanation;

        public String motivationMessage;

        public String suggestedCode;
    }
}