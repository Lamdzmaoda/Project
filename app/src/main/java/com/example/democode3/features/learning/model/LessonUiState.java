package com.example.democode3.features.learning.model;

import java.util.ArrayList;
import java.util.List;

public class LessonUiState {

    // =====================================================
    // STEP
    // =====================================================

    public int currentStepIndex = 0;

    // =====================================================
    // TOTAL STEP
    // =====================================================

    public int totalSteps = 0;

    // =====================================================
    // LESSON STEPS
    // =====================================================

    public List<LessonStep> lessonSteps =
            new ArrayList<>();

    // =====================================================
    // QUIZ
    // =====================================================

    public int selectedIndex = -1;

    public boolean answered = false;

    public boolean isCorrect = false;

    // =====================================================
    // CODE
    // =====================================================

    public List<String> selectedCodeWords =
            new ArrayList<>();
}