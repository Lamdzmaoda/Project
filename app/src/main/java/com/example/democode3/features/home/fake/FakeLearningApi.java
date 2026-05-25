package com.example.democode3.features.home.fake;

import android.content.Context;

import com.example.democode3.core.enums.Difficulty;
import com.example.democode3.core.session.LearningSessionManager;
import com.example.democode3.features.home.model.Language;

import java.util.ArrayList;
import java.util.List;

public class FakeLearningApi {

    private final Context context;

    public FakeLearningApi(
            Context context
    ) {

        this.context = context;
    }

    // =====================================
    // GET LANGUAGES
    // =====================================

    public List<Language> getLanguages() {

        long currentLanguageId =
                LearningSessionManager
                        .getLanguageId(
                                context
                        );

        List<Language> list =
                new ArrayList<>();

        list.add(

                new Language(
                        1,
                        "Python",
                        "python",
                        "",
                        "Beginner friendly",
                        Difficulty.BEGINNER,
                        System.currentTimeMillis(),
                        currentLanguageId == 1
                )
        );

        list.add(

                new Language(
                        2,
                        "Java",
                        "java",
                        "",
                        "OOP programming",
                        Difficulty.EASY,
                        System.currentTimeMillis(),
                        currentLanguageId == 2
                )
        );

        list.add(

                new Language(
                        3,
                        "JavaScript",
                        "javascript",
                        "",
                        "Frontend language",
                        Difficulty.EASY,
                        System.currentTimeMillis(),
                        currentLanguageId == 3
                )
        );

        return list;
    }

    // =====================================
    // ENROLL
    // =====================================

    public void enroll(
            long languageId
    ) {

        LearningSessionManager
                .saveLanguageId(
                        context,
                        languageId
                );
    }
}