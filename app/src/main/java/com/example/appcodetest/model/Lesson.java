package com.example.appcodetest.model;

public class Lesson {

    public String id;
    public String chapterId;

    public String title;
    public String contentMarkdown;

    public int order;

    // 🔥 QUAN TRỌNG NHẤT
    public String mode; // LEARN hoặc PRACTICE

    public int xp;

    public double progress;

    public boolean isLocked;
    public boolean isCompleted;
}