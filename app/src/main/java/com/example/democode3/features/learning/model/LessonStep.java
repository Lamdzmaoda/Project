package com.example.democode3.features.learning.model;

import java.util.List;

public class LessonStep {

    // =====================================
    // ID
    // =====================================

    public String id;

    // =====================================
    // TITLE
    // =====================================

    public String title;

    // =====================================
    // TYPE
    // =====================================

    // TEXT
    // QUIZ
    // CODE

    public String type;

    // =====================================
    // MODE
    // =====================================

    public String mode;

    // =====================================
    // ORDER
    // =====================================

    public int orderIndex;

    // =====================================
    // STATUS
    // =====================================

    public String status;

    // =====================================
    // LOCKED
    // =====================================

    public String lockedStatus;

    // =====================================
    // COMPLETE
    // =====================================

    public String completedStatus;

    // =====================================
    // XP
    // =====================================

    public double xp;

    // =====================================
    // DATA
    // =====================================

    public StepData data;

    // =====================================
    // STEP DATA
    // =====================================

    public static class StepData {

        // QUIZ

        public String question;

        public List<Option> options;

        public String correctValue;

        public String explanation;

        // TEXT / CODE

        public String content;

        // CODE

        public String template;

        public List<String> answers;
    }

    // =====================================
    // OPTION
    // =====================================

    public static class Option {

        public String id;

        public String text;

        public Option(
                String id,
                String text
        ) {

            this.id = id;
            this.text = text;
        }
    }

    @Override
    public String toString() {

        return title;
    }
}