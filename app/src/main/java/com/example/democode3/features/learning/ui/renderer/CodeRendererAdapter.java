package com.example.democode3.features.learning.ui.renderer;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.democode3.features.learning.model.LessonStep;
import com.example.democode3.features.learning.model.LessonUiState;

import java.util.ArrayList;
import java.util.List;

public class CodeRendererAdapter
        implements LessonStepRenderer {

    private final Context context;

    private final LessonUiState uiState;

    private final LinearLayout layoutCode;

    private final TextView txtContent;

    private final TextView txtCodeQuestion;

    private final LinearLayout layoutCodeSlots;

    private final LinearLayout layoutCodeWords;

    private final Button btnCheckCode;

    private final Button btnResetCode;

    private final Runnable onCodeCorrect;

    public CodeRendererAdapter(

            Context context,

            LessonUiState uiState,

            LinearLayout layoutCode,

            TextView txtContent,

            TextView txtCodeQuestion,

            LinearLayout layoutCodeSlots,

            LinearLayout layoutCodeWords,

            Button btnCheckCode,

            Button btnResetCode,

            Runnable onCodeCorrect
    ) {

        this.context =
                context;

        this.uiState =
                uiState;

        this.layoutCode =
                layoutCode;

        this.txtContent =
                txtContent;

        this.txtCodeQuestion =
                txtCodeQuestion;

        this.layoutCodeSlots =
                layoutCodeSlots;

        this.layoutCodeWords =
                layoutCodeWords;

        this.btnCheckCode =
                btnCheckCode;

        this.btnResetCode =
                btnResetCode;

        this.onCodeCorrect =
                onCodeCorrect;
    }

    @Override
    public void render(
            LessonStep step
    ) {

        layoutCode.setVisibility(
                View.VISIBLE
        );

        txtContent.setVisibility(
                View.GONE
        );

        // QUESTION

        txtCodeQuestion.setText(

                step.data.content
        );

        // RESET

        layoutCodeSlots.removeAllViews();

        layoutCodeWords.removeAllViews();

        uiState.selectedCodeWords.clear();

        // =========================================
        // TEMPLATE
        // =========================================

        final String template =
                step.data.template == null
                        ? ""
                        : step.data.template;

        TextView txtTemplate =
                new TextView(context);

        txtTemplate.setText(template);

        txtTemplate.setTextSize(22);

        txtTemplate.setTextColor(
                0xFFFFFFFF
        );

        txtTemplate.setPadding(
                20,
                20,
                20,
                20
        );

        txtTemplate.setBackgroundColor(
                0xFF1E293B
        );

        layoutCodeSlots.addView(
                txtTemplate
        );

        // =========================================
        // ANSWERS
        // =========================================

        List<String> answers =
                step.data.answers;

        if (answers == null) {

            answers =
                    new ArrayList<>();
        }

        for (String answer : answers) {

            Button btn =
                    new Button(context);

            btn.setText(answer);

            btn.setAllCaps(false);

            btn.setOnClickListener(v -> {

                uiState.selectedCodeWords.clear();

                uiState.selectedCodeWords
                        .add(answer);

                txtTemplate.setText(

                        template.replace(
                                "[0]",
                                answer
                        )
                );
            });

            layoutCodeWords.addView(btn);
        }

        // =========================================
        // CHECK
        // =========================================

        btnCheckCode.setOnClickListener(v -> {

            boolean correct =
                    !uiState.selectedCodeWords
                            .isEmpty();

            if (correct) {

                btnCheckCode.setText(
                        "CHÍNH XÁC 🎉"
                );

                btnCheckCode.postDelayed(() -> {

                    if (onCodeCorrect != null) {

                        onCodeCorrect.run();
                    }

                }, 800);

            } else {

                btnCheckCode.setText(
                        "SAI RỒI 😢"
                );
            }
        });

        // =========================================
        // RESET
        // =========================================

        btnResetCode.setOnClickListener(v -> {

            render(step);
        });
    }
}