package com.example.democode3.features.learning.ui.renderer;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.democode3.R;
import com.example.democode3.features.learning.model.LessonStep;

public class QuizStepRenderer {

    public static void render(

            LessonStep step,

            LinearLayout layoutQuiz,

            TextView txtContent,

            TextView btnAnswerA,

            TextView btnAnswerB,

            View viewCircleA,

            View viewCircleB,

            LinearLayout layoutAnswerA,

            LinearLayout layoutAnswerB
    ) {

        // =================================================
        // SHOW QUIZ
        // =================================================

        layoutQuiz.setVisibility(
                View.VISIBLE
        );

        // =================================================
        // QUESTION
        // =================================================

        txtContent.setText(
                step.data.question
        );

        // =================================================
        // ANSWER
        // =================================================

        if (

                step.data.options != null

                        &&

                        step.data.options.size() >= 2
        ) {

            btnAnswerA.setText(

                    step.data.options
                            .get(0)
                            .text
            );

            btnAnswerB.setText(

                    step.data.options
                            .get(1)
                            .text
            );
        }

        // =================================================
        // RESET STYLE
        // =================================================

        layoutAnswerA.setBackgroundResource(
                R.drawable.bg_quiz_option
        );

        layoutAnswerB.setBackgroundResource(
                R.drawable.bg_quiz_option
        );

        viewCircleA.setBackgroundResource(
                R.drawable.bg_radio_unselected
        );

        viewCircleB.setBackgroundResource(
                R.drawable.bg_radio_unselected
        );

        btnAnswerA.setTextColor(
                Color.WHITE
        );

        btnAnswerB.setTextColor(
                Color.WHITE
        );
    }
}