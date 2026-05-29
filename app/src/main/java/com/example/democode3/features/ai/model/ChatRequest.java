package com.example.democode3.features.ai.model;

public class ChatRequest {

    public String message;

    public String stepId;

    public String problemId;

    public ChatRequest(
            String message,
            String stepId,
            String problemId
    ) {

        this.message = message;
        this.stepId = stepId;
        this.problemId = problemId;
    }
}