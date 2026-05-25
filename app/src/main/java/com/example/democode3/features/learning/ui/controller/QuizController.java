package com.example.democode3.features.learning.ui.controller;

import android.widget.Button;

import com.example.democode3.features.learning.model.LessonUiState;
import com.example.democode3.features.learning.ui.viewmodel.LessonViewModel;

public class QuizController {

    // =====================================================
    // VIEWMODEL
    // =====================================================

    private final LessonViewModel viewModel;

    // =====================================================
    // UI STATE
    // =====================================================

    private final LessonUiState uiState;

    // =====================================================
    // VIEW
    // =====================================================

    private final Button btnAnswerA;

    private final Button btnAnswerB;

    private final Button btnNext;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public QuizController(

            LessonViewModel viewModel,

            LessonUiState uiState,

            Button btnAnswerA,

            Button btnAnswerB,

            Button btnNext
    ) {

        this.viewModel =
                viewModel;

        this.uiState =
                uiState;

        this.btnAnswerA =
                btnAnswerA;

        this.btnAnswerB =
                btnAnswerB;

        this.btnNext =
                btnNext;
    }

    // =====================================================
    // SELECT ANSWER
    // =====================================================

    public void selectAnswer(
            int index
    ) {

        if (uiState.answered) {

            return;
        }

        // =============================================
        // SAVE INDEX
        // =============================================

        uiState.selectedIndex =
                index;

        // =============================================
        // VIEWMODEL
        // =============================================

        viewModel.selectAnswer(
                index
        );

        // =============================================
        // RESET COLOR
        // =============================================

        btnAnswerA.setBackgroundColor(
                0xFF4C4AA1
        );

        btnAnswerB.setBackgroundColor(
                0xFF4C4AA1
        );

        // =============================================
        // ACTIVE
        // =============================================

        if (index == 0) {

            btnAnswerA.setBackgroundColor(
                    0xFF38BDF8
            );

        } else {

            btnAnswerB.setBackgroundColor(
                    0xFF38BDF8
            );
        }

        // =============================================
        // ENABLE NEXT
        // =============================================

        btnNext.setEnabled(true);

        btnNext.setAlpha(1f);
    }
}