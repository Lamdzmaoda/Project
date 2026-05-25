package com.example.democode3.features.home.fake;

public class FakeProgressManager {

    // =====================================================
    // XP
    // =====================================================

    public static int xp = 1250;

    // =====================================================
    // STREAK
    // =====================================================

    public static int streak = 7;

    // =====================================================
    // CURRENT LANGUAGE
    // =====================================================

    public static long currentLanguageId = 1;

    // =====================================================
    // CURRENT CHAPTER
    // =====================================================

    public static long currentChapterId = 1;

    // =====================================================
    // CURRENT LESSON
    // =====================================================

    public static long currentLessonId = 3;

    // =====================================================
    // CHAPTER PROGRESS
    // =====================================================

    public static int chapterProgress = 65;

    // =====================================================
    // COMPLETE LESSON
    // =====================================================

    public static void completeLesson(
            long lessonId
    ) {

        currentLessonId = lessonId + 1;

        xp += 50;

        chapterProgress += 12;

        if (chapterProgress > 100) {

            chapterProgress = 100;
        }
    }
}