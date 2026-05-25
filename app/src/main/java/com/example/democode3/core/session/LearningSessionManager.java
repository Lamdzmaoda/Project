package com.example.democode3.core.session;

import android.content.Context;
import android.content.SharedPreferences;

public class LearningSessionManager {

    private static final String PREF_NAME =
            "learning_session";

    // =====================================
    // LANGUAGE
    // =====================================

    private static final String KEY_LANGUAGE_ID =
            "current_language_id";

    // =====================================
    // CHAPTER
    // =====================================

    private static final String KEY_CHAPTER_ID =
            "current_chapter_id";

    // =====================================
    // LESSON
    // =====================================

    private static final String KEY_LESSON_ID =
            "current_lesson_id";

    // =====================================
    // PREFS
    // =====================================

    private static SharedPreferences getPrefs(
            Context context
    ) {

        return context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }

    // =====================================
    // SAVE LANGUAGE
    // =====================================

    public static void saveLanguageId(
            Context context,
            long languageId
    ) {

        getPrefs(context)
                .edit()
                .putLong(
                        KEY_LANGUAGE_ID,
                        languageId
                )
                .apply();
    }

    // =====================================
    // GET LANGUAGE
    // =====================================

    public static long getLanguageId(
            Context context
    ) {

        return getPrefs(context)
                .getLong(
                        KEY_LANGUAGE_ID,
                        1
                );
    }

    // =====================================
    // SAVE CHAPTER
    // =====================================

    public static void saveChapterId(
            Context context,
            long chapterId
    ) {

        getPrefs(context)
                .edit()
                .putLong(
                        KEY_CHAPTER_ID,
                        chapterId
                )
                .apply();
    }

    // =====================================
    // GET CHAPTER
    // =====================================

    public static long getChapterId(
            Context context
    ) {

        return getPrefs(context)
                .getLong(
                        KEY_CHAPTER_ID,
                        1
                );
    }

    // =====================================
    // SAVE LESSON
    // =====================================

    public static void saveLessonId(
            Context context,
            long lessonId
    ) {

        getPrefs(context)
                .edit()
                .putLong(
                        KEY_LESSON_ID,
                        lessonId
                )
                .apply();
    }

    // =====================================
    // GET LESSON
    // =====================================

    public static long getLessonId(
            Context context
    ) {

        return getPrefs(context)
                .getLong(
                        KEY_LESSON_ID,
                        1
                );
    }
}