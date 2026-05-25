package com.example.democode3.features.learning.ui.renderer;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.democode3.features.learning.model.LessonStep;

public class QuizStepRenderer {

    public static void render(

            LessonStep step,

            LinearLayout layoutQuiz,

            TextView txtContent,

            Button btnAnswerA,

            Button btnAnswerB
    ) {

        layoutQuiz.setVisibility(
                View.VISIBLE
        );

        txtContent.setText(
                step.data.question
        );

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

        btnAnswerA.setBackgroundColor(
                0xFF4C4AA1
        );

        btnAnswerB.setBackgroundColor(
                0xFF4C4AA1
        );
    }
}