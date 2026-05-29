package com.example.democode3.features.learning.ui.renderer;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.democode3.R;
import com.example.democode3.features.learning.model.LessonStep;

import java.util.ArrayList;
import java.util.List;

public class CodeRendererAdapter
        implements LessonStepRenderer {

    private final Context context;

    private final LinearLayout layoutCode;

    private final LinearLayout layoutCodeSlots;

    private final LinearLayout layoutCodeWords;

    private final TextView txtContent;

    private final List<String> selectedWords =
            new ArrayList<>();

    public CodeRendererAdapter(

            Context context,

            LinearLayout layoutCode,

            LinearLayout layoutCodeSlots,

            LinearLayout layoutCodeWords,

            TextView txtContent
    ) {

        this.context = context;

        this.layoutCode = layoutCode;

        this.layoutCodeSlots = layoutCodeSlots;

        this.layoutCodeWords = layoutCodeWords;

        this.txtContent = txtContent;
    }

    @Override
    public void render(
            LessonStep step
    ) {

        layoutCode.setVisibility(
                View.VISIBLE
        );

        txtContent.setText(
                step.data.content
        );

        layoutCodeSlots.removeAllViews();

        layoutCodeWords.removeAllViews();

        selectedWords.clear();

        // =========================================
        // TEMPLATE
        // =========================================

        String template =
                step.data.template;

        String[] split =
                template.split("\\[0\\]");

        // =========================================
        // LEFT
        // =========================================

        TextView left =
                createCodeView(
                        split[0]
                );

        layoutCodeSlots.addView(left);

        // =========================================
        // SLOT
        // =========================================

        TextView slot =
                createSlotView();

        layoutCodeSlots.addView(slot);

        // =========================================
        // RIGHT
        // =========================================

        if (split.length > 1) {

            TextView right =
                    createCodeView(
                            split[1]
                    );

            layoutCodeSlots.addView(right);
        }

        // =========================================
        // WORD BANK
        // =========================================

        if (step.data.answers != null) {

            for (String answer : step.data.answers) {

                TextView word =
                        createWordView(answer);

                word.setOnClickListener(v -> {

                    slot.setText(answer);

                    slot.setTextColor(
                            Color.WHITE
                    );

                    selectedWords.clear();

                    selectedWords.add(answer);
                });

                layoutCodeWords.addView(word);
            }
        }
    }

    // =============================================
    // WORD
    // =============================================

    private TextView createWordView(
            String text
    ) {

        TextView tv =
                new TextView(context);

        tv.setText(text);

        tv.setTextColor(Color.WHITE);

        tv.setTextSize(18);

        tv.setPadding(
                40,
                24,
                40,
                24
        );

        tv.setBackgroundResource(
                R.drawable.bg_quiz_option
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(

                        LinearLayout.LayoutParams.WRAP_CONTENT,

                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.rightMargin = 20;

        tv.setLayoutParams(params);

        return tv;
    }

    // =============================================
    // SLOT
    // =============================================

    private TextView createSlotView() {

        TextView tv =
                new TextView(context);

        tv.setText("____");

        tv.setTextSize(22);

        tv.setTextColor("#94A3B8".equals("")
                ? Color.GRAY
                : Color.GRAY);

        tv.setPadding(
                30,
                20,
                30,
                20
        );

        tv.setGravity(Gravity.CENTER);

        tv.setBackgroundResource(
                R.drawable.bg_quiz_option_selected
        );

        return tv;
    }

    // =============================================
    // CODE
    // =============================================

    private TextView createCodeView(
            String text
    ) {

        TextView tv =
                new TextView(context);

        tv.setText(text);

        tv.setTextColor(Color.WHITE);

        tv.setTextSize(22);

        return tv;
    }
}