package com.example.democode3.features.learning.ui.renderer;

import android.widget.Button;
import android.widget.TextView;

import com.example.democode3.features.learning.model.LessonStep;

public class TextRendererAdapter
        implements LessonStepRenderer {

    private final TextView txtContent;

    private final Button btnNext;

    public TextRendererAdapter(

            TextView txtContent,

            Button btnNext
    ) {

        this.txtContent =
                txtContent;

        this.btnNext =
                btnNext;
    }

    @Override
    public void render(
            LessonStep step
    ) {

        String content = "";

        if (
                step.data != null
                        &&
                        step.data.content != null
        ) {

            content =
                    step.data.content;
        }

        txtContent.setText(
                content
        );

        btnNext.setEnabled(true);

        btnNext.setAlpha(1f);

        btnNext.setText(
                "TIẾP TỤC"
        );
    }
}