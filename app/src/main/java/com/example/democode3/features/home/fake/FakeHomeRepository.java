// features/home/fake/FakeHomeRepository.java

package com.example.democode3.features.home.fake;

import com.example.democode3.core.enums.Difficulty;
import com.example.democode3.features.home.model.Chapter;
import com.example.democode3.features.home.model.Language;
import com.example.democode3.features.home.model.Lesson;
import com.example.democode3.features.home.model.UserProgress;

import java.util.ArrayList;
import java.util.List;

public class FakeHomeRepository {

    // =====================================================
    // USER PROGRESS
    // =====================================================

    public UserProgress getUserProgress() {

        return new UserProgress(

                1,
                1,
                1,
                1,
                3,
                8L,
                65f,
                false,
                null,
                System.currentTimeMillis()
        );
    }

    // =====================================================
    // LANGUAGES
    // =====================================================

    public List<Language> getLanguages() {

        List<Language> list =
                new ArrayList<>();

        list.add(
                new Language(
                        1,
                        "Python",
                        "python",
                        null,
                        "Python Learning Path",
                        Difficulty.BEGINNER,
                        System.currentTimeMillis(),
                        false
                )
        );

        return list;
    }

    // =====================================================
    // CHAPTERS
    // =====================================================

    public List<Chapter> getChapters(
            long languageId
    ) {

        List<Chapter> list =
                new ArrayList<>();

        list.add(
                new Chapter(
                        1,
                        1,
                        "Biến và Input",
                        "Python variables",
                        1
                )
        );

        return list;
    }

    // =====================================================
    // LESSONS
    // =====================================================

    private final List<Lesson>
            allLessons =
            new ArrayList<>();

    public FakeHomeRepository() {

        seedLessons();
    }

    // =====================================================
    // SEED LESSONS
    // =====================================================

    private void seedLessons() {

        // =============================================
        // COMPLETED
        // =============================================

        allLessons.add(
                l(
                        1,
                        1,
                        "Chương trình Python đầu tiên"
                )
        );

        allLessons.add(
                l(
                        2,
                        1,
                        "Hàm print()"
                )
        );

        // =============================================
        // CURRENT
        // =============================================

        allLessons.add(
                l(
                        3,
                        1,
                        "Biến là gì?"
                )
        );

        // =============================================
        // LOCKED
        // =============================================

        allLessons.add(
                l(
                        4,
                        1,
                        "Kiểu dữ liệu"
                )
        );
    }

    // =====================================================
    // GET LESSONS
    // =====================================================

    public List<Lesson> getLessons(
            long chapterId
    ) {

        List<Lesson> result =
                new ArrayList<>();

        for (Lesson lesson : allLessons) {

            if (
                    lesson.chapterId
                            == chapterId
            ) {

                result.add(lesson);
            }
        }

        return result;
    }

    // =====================================================
    // LESSON BUILDER
    // =====================================================

    private Lesson l(

            long id,

            long chapterId,

            String title
    ) {

        return new Lesson(

                id,

                chapterId,

                title,

                title + " description",

                Difficulty.BEGINNER,

                5,

                null,

                (int) id,

                true
        );
    }
}