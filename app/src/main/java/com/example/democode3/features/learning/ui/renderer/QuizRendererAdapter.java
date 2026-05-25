package com.example.democode3.features.learning.ui.renderer;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.democode3.features.learning.model.LessonStep;

public class QuizRendererAdapter
        implements LessonStepRenderer {

    private final LinearLayout layoutQuiz;

    private final TextView txtContent;

    private final Button btnAnswerA;

    private final Button btnAnswerB;

    public QuizRendererAdapter(

            LinearLayout layoutQuiz,

            TextView txtContent,

            Button btnAnswerA,

            Button btnAnswerB
    ) {

        this.layoutQuiz = layoutQuiz;

        this.txtContent = txtContent;

        this.btnAnswerA = btnAnswerA;

        this.btnAnswerB = btnAnswerB;
    }

    @Override
    public void render(
            LessonStep step
    ) {

        QuizStepRenderer.render(

                step,

                layoutQuiz,

                txtContent,

                btnAnswerA,

                btnAnswerB
        );
    }
}