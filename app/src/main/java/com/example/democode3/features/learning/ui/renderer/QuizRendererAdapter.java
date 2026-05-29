package com.example.democode3.features.learning.ui.renderer;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.democode3.features.learning.model.LessonStep;

public class QuizRendererAdapter
        implements LessonStepRenderer {

    private final LinearLayout layoutQuiz;

    private final TextView txtContent;

    private final TextView btnAnswerA;

    private final TextView btnAnswerB;

    private final View viewCircleA;

    private final View viewCircleB;

    private final LinearLayout layoutAnswerA;

    private final LinearLayout layoutAnswerB;

    public QuizRendererAdapter(

            LinearLayout layoutQuiz,

            TextView txtContent,

            TextView btnAnswerA,

            TextView btnAnswerB,

            View viewCircleA,

            View viewCircleB,

            LinearLayout layoutAnswerA,

            LinearLayout layoutAnswerB
    ) {

        this.layoutQuiz = layoutQuiz;

        this.txtContent = txtContent;

        this.btnAnswerA = btnAnswerA;

        this.btnAnswerB = btnAnswerB;

        this.viewCircleA = viewCircleA;

        this.viewCircleB = viewCircleB;

        this.layoutAnswerA = layoutAnswerA;

        this.layoutAnswerB = layoutAnswerB;
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

                btnAnswerB,

                viewCircleA,

                viewCircleB,

                layoutAnswerA,

                layoutAnswerB
        );
    }
}