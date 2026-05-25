package com.example.democode3.features.learning.ui.renderer;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProgressRenderer {

    // =====================================================
    // VIEW
    // =====================================================

    private final TextView txtStepProgress;

    private final View progressView;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ProgressRenderer(

            TextView txtStepProgress,

            View progressView
    ) {

        this.txtStepProgress =
                txtStepProgress;

        this.progressView =
                progressView;
    }

    // =====================================================
    // RENDER
    // =====================================================

    public void render(

            int current,

            int total
    ) {

        // =============================================
        // STEP TEXT
        // =============================================

        txtStepProgress.setText(

                current
                        + " / "
                        + total
        );

        // =============================================
        // PERCENT
        // =============================================

        float percent =
                (float) current / total;

        progressView.post(() -> {

            int parentWidth =
                    ((LinearLayout)
                            progressView.getParent())
                            .getWidth();

            int width =
                    (int) (parentWidth * percent);

            LinearLayout.LayoutParams params =
                    (LinearLayout.LayoutParams)
                            progressView.getLayoutParams();

            params.width = width;

            progressView.setLayoutParams(
                    params
            );
        });
    }
}