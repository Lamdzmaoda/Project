package com.example.appcodetest.model;

import java.util.List;

public class LessonStep {

    public String id;
    public String title;

    public String type;
    public String mode;

    public int orderIndex;

    public String lockedStatus;
    public String completedStatus;

    public double xp;
    public boolean requiredToUnlockNext;

    public String createAt;

    public StepData data;

    public static class StepData {
        public String question;
        public List<String> options;
        public int correctValue;
        public String explanation;
    }

    @Override
    public String toString() {
        return title;
    }
}