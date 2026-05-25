package com.example.democode3.features.learning.ui.navigation;

import androidx.fragment.app.FragmentManager;

import com.example.democode3.features.home.fake.FakeProgressManager;
import com.example.democode3.features.learning.ui.component.LearningRewardDialog;
import com.example.democode3.features.learning.ui.viewmodel.LessonViewModel;

public class LessonNavigator {

    // =====================================================
    // VIEWMODEL
    // =====================================================

    private final LessonViewModel viewModel;

    // =====================================================
    // LESSON
    // =====================================================

    private final long lessonId;

    // =====================================================
    // FRAGMENT MANAGER
    // =====================================================

    private final FragmentManager fragmentManager;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public LessonNavigator(

            LessonViewModel viewModel,

            long lessonId,

            FragmentManager fragmentManager
    ) {

        this.viewModel =
                viewModel;

        this.lessonId =
                lessonId;

        this.fragmentManager =
                fragmentManager;
    }

    // =====================================================
    // NEXT STEP
    // =====================================================

    public boolean goToNextStep() {

        boolean hasNext =
                viewModel.nextStep();

        // =============================================
        // COMPLETE
        // =============================================

        if (!hasNext) {

            completeLesson();
        }

        return hasNext;
    }

    // =====================================================
    // COMPLETE LESSON
    // =====================================================

    private void completeLesson() {

        FakeProgressManager.completeLesson(
                lessonId
        );

        LearningRewardDialog dialog =
                new LearningRewardDialog(

                        50,

                        100
                );

        dialog.show(

                fragmentManager,

                "LearningReward"
        );
    }
}