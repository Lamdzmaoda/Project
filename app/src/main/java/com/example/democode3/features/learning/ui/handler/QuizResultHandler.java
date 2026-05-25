package com.example.democode3.features.learning.ui.handler;

import android.widget.Button;

import androidx.fragment.app.FragmentManager;

import com.example.democode3.features.learning.model.LessonStep;
import com.example.democode3.features.learning.ui.component.QuizResultSheet;

public class QuizResultHandler {

    public void handleCorrect(

            int selectedIndex,

            Button btnAnswerA,

            Button btnAnswerB,

            LessonStep step,

            FragmentManager fragmentManager,

            Runnable onContinue,

            Button btnNext
    ) {

        if (selectedIndex == 0) {

            btnAnswerA.setBackgroundColor(
                    0xFF4ADE80
            );

        } else {

            btnAnswerB.setBackgroundColor(
                    0xFF4ADE80
            );
        }

        QuizResultSheet sheet =
                new QuizResultSheet(

                        true,

                        step.data.explanation,

                        onContinue
                );

        sheet.show(

                fragmentManager,

                "QuizResult"
        );

        btnNext.setText(
                "TIẾP TỤC"
        );
    }

    public void handleWrong(

            int selectedIndex,

            Button btnAnswerA,

            Button btnAnswerB,

            LessonStep step,

            FragmentManager fragmentManager,

            Runnable onRetry,

            Button btnNext
    ) {

        if (selectedIndex == 0) {

            btnAnswerA.setBackgroundColor(
                    0xFFFB7185
            );

        } else {

            btnAnswerB.setBackgroundColor(
                    0xFFFB7185
            );
        }

        QuizResultSheet sheet =
                new QuizResultSheet(

                        false,

                        "Sai rồi 😭🔥",

                        onRetry
                );

        sheet.show(

                fragmentManager,

                "QuizResult"
        );

        btnNext.setText(
                "THỬ LẠI"
        );
    }
}