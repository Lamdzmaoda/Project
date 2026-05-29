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

        // =====================================
        // PYTHON
        // =====================================

        list.add(

                new Language(
                        1,
                        "Python",
                        "python",
                        "",
                        "Ngôn ngữ dễ học dành cho người mới bắt đầu 😭🔥",
                        Difficulty.BEGINNER,
                        System.currentTimeMillis(),
                        currentLanguageId == 1
                )
        );

        // =====================================
        // JAVA
        // =====================================

        list.add(

                new Language(
                        2,
                        "Java",
                        "java",
                        "",
                        "Lập trình hướng đối tượng mạnh mẽ 😭🔥",
                        Difficulty.EASY,
                        System.currentTimeMillis(),
                        currentLanguageId == 2
                )
        );

        // =====================================
        // JAVASCRIPT
        // =====================================

        list.add(

                new Language(
                        3,
                        "JavaScript",
                        "javascript",
                        "",
                        "Ngôn ngữ phổ biến cho web 😭🔥",
                        Difficulty.EASY,
                        System.currentTimeMillis(),
                        currentLanguageId == 3
                )
        );

        // =====================================
        // C++
        // =====================================

        list.add(

                new Language(
                        4,
                        "C++",
                        "cpp",
                        "",
                        "Hiệu năng cao và mạnh về thuật toán 😭🔥",
                        Difficulty.MEDIUM,
                        System.currentTimeMillis(),
                        currentLanguageId == 4
                )
        );

        // =====================================
        // C#
        // =====================================

        list.add(

                new Language(
                        5,
                        "C#",
                        "csharp",
                        "",
                        "Ngôn ngữ hiện đại dành cho .NET 😭🔥",
                        Difficulty.EASY,
                        System.currentTimeMillis(),
                        currentLanguageId == 5
                )
        );

        // =====================================
        // GO
        // =====================================

        list.add(

                new Language(
                        6,
                        "Go",
                        "golang",
                        "",
                        "Ngôn ngữ backend cực nhanh 😭🔥",
                        Difficulty.MEDIUM,
                        System.currentTimeMillis(),
                        currentLanguageId == 6
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