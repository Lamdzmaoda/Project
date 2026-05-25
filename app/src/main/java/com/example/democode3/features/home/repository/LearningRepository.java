package com.example.democode3.features.home.repository;

import android.content.Context;

import com.example.democode3.features.home.fake.FakeLearningApi;
import com.example.democode3.features.home.model.Language;

import java.util.List;

public class LearningRepository {

    private final FakeLearningApi api;

    public LearningRepository(
            Context context
    ) {

        api =
                new FakeLearningApi(
                        context
                );
    }

    // =====================================
    // LANGUAGES
    // =====================================

    public List<Language> getLanguages() {

        return api.getLanguages();
    }

    // =====================================
    // ENROLL
    // =====================================

    public void enroll(
            long languageId
    ) {

        api.enroll(languageId);
    }
}