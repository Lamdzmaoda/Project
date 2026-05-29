package com.example.democode3.features.learning.ui.handler;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.democode3.R;

public class QuizResultHandler {

    // =================================================
    // CORRECT
    // =================================================

    public void handleCorrect(

            int selectedIndex,

            LinearLayout layoutAnswerA,

            LinearLayout layoutAnswerB,

            LinearLayout layoutResult,

            LinearLayout layoutAi,

            TextView txtResult,

            Button btnNext,

            Runnable onContinue
    ) {

        // =================================================
        // SHOW RESULT
        // =================================================

        layoutResult.setVisibility(
                View.VISIBLE
        );

        layoutAi.setVisibility(
                View.VISIBLE
        );

        txtResult.setText(
                "🎉 Chính xác!"
        );

        layoutResult.setBackgroundResource(
                R.drawable.bg_result_correct
        );

        // =================================================
        // GREEN ANSWER
        // =================================================

        if (selectedIndex == 0) {

            layoutAnswerA.setBackgroundResource(
                    R.drawable.bg_quiz_correct
            );

        } else {

            layoutAnswerB.setBackgroundResource(
                    R.drawable.bg_quiz_correct
            );
        }

        // =================================================
        // BUTTON
        // =================================================

        btnNext.setVisibility(
                View.VISIBLE
        );

        btnNext.setEnabled(true);

        btnNext.setClickable(true);

        btnNext.setAlpha(1f);

        btnNext.setText(
                "TIẾP TỤC"
        );

        btnNext.setTextColor(
                Color.WHITE
        );

        btnNext.setBackgroundResource(
                R.drawable.bg_primary_button
        );

        btnNext.setOnClickListener(v -> {

            if (onContinue != null) {

                onContinue.run();
            }
        });
    }

    // =================================================
    // WRONG
    // =================================================

    public void handleWrong(

            int selectedIndex,

            LinearLayout layoutAnswerA,

            LinearLayout layoutAnswerB,

            LinearLayout layoutResult,

            LinearLayout layoutAi,

            TextView txtResult,

            Button btnNext,

            Runnable onRetry
    ) {

        // =================================================
        // SHOW RESULT
        // =================================================

        layoutResult.setVisibility(
                View.VISIBLE
        );

        layoutAi.setVisibility(
                View.VISIBLE
        );

        txtResult.setText(
                "😭 Sai rồi!"
        );

        layoutResult.setBackgroundResource(
                R.drawable.bg_result_wrong
        );

        // =================================================
        // RED ANSWER
        // =================================================

        if (selectedIndex == 0) {

            layoutAnswerA.setBackgroundResource(
                    R.drawable.bg_quiz_wrong
            );

        } else {

            layoutAnswerB.setBackgroundResource(
                    R.drawable.bg_quiz_wrong
            );
        }

        // =================================================
        // BUTTON
        // =================================================

        btnNext.setVisibility(
                View.VISIBLE
        );

        btnNext.setEnabled(true);

        btnNext.setClickable(true);

        btnNext.setAlpha(1f);

        btnNext.setText(
                "THỬ LẠI"
        );

        btnNext.setTextColor(
                Color.WHITE
        );

        btnNext.setBackgroundResource(
                R.drawable.bg_primary_button
        );

        btnNext.setOnClickListener(v -> {

            if (onRetry != null) {

                onRetry.run();
            }
        });
    }
}