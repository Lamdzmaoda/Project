package com.example.appcodetest.model;

import org.json.JSONObject;
import java.util.List;

public class LessonStep {

    public String id;
    public String lessonId;

    public String type; // INFO, QUESTION, FILL_CODE

    public int order;

    public boolean isLocked;
    public boolean isCompleted;

    public int xp;
    public boolean requiredToUnlockNext;

    // =========================
    // 🔥 FIX CHUẨN
    // =========================
    public JSONObject data;

    // =========================
    // ⚠️ OLD (GIỮ LẠI)
    // =========================

    public String content;

    public String question;
    public List<String> options;
    public int correctIndex;

    public List<String> words;
    public String correctAnswer;

    public String title;
    public String guide;
    public String starterCode;
    public String expectedOutput;
    public String language;

    public String explanation;
}