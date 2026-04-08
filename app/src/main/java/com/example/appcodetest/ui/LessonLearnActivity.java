package com.example.appcodetest.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;

import com.example.appcodetest.R;
import com.example.appcodetest.model.LessonStep;
import com.example.appcodetest.utils.FakeDataProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class LessonLearnActivity extends AppCompatActivity {

    LinearLayout layoutInfo, layoutQuestion, layoutFillCode, layoutWords;

    TextView txtInfo, txtQuestion, txtExplanation, txtCodeResult;
    Button option1, option2, option3, btnCheck, btnNext, btnCheckFill, btnResetFill;

    List<LessonStep> steps;
    int currentIndex = 0;
    String lessonId;

    int selectedIndex = -1;
    String currentCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_learn);

        bindViews();

        lessonId = getIntent().getStringExtra("LESSON_ID");

        // 🔥 FIX NULL
        if (lessonId == null) lessonId = "lesson_1";

        loadSteps();
    }

    private void bindViews() {

        layoutInfo = findViewById(R.id.layoutInfo);
        layoutQuestion = findViewById(R.id.layoutQuestion);
        layoutFillCode = findViewById(R.id.layoutFillCode);
        layoutWords = findViewById(R.id.layoutWords);

        txtInfo = findViewById(R.id.txtInfo);
        txtQuestion = findViewById(R.id.txtQuestion);
        txtExplanation = findViewById(R.id.txtExplanation);
        txtCodeResult = findViewById(R.id.txtCodeResult);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);

        btnCheck = findViewById(R.id.btnCheck);
        btnNext = findViewById(R.id.btnNext);
        btnCheckFill = findViewById(R.id.btnCheckFill);
        btnResetFill = findViewById(R.id.btnResetFill);

        btnNext.setOnClickListener(v -> nextStep());

        // 🔥 CLICK INFO để next
        layoutInfo.setOnClickListener(v -> nextStep());
    }

    private void loadSteps() {
        steps = FakeDataProvider.getSteps(lessonId);

        currentIndex = 0;
        showStep();
    }

    private void showStep() {

        if (steps == null || steps.isEmpty()) return;

        LessonStep step = steps.get(currentIndex);

        hideAll();

        JSONObject data = step.data;
        if (data == null) data = new JSONObject();

        switch (step.type) {

            case "INFO":
                layoutInfo.setVisibility(View.VISIBLE);
                txtInfo.setText(data.optString("content"));
                btnNext.setVisibility(View.VISIBLE);
                break;

            case "QUESTION":
                layoutQuestion.setVisibility(View.VISIBLE);
                renderQuestion(data);
                break;

            case "FILL_CODE":
                layoutFillCode.setVisibility(View.VISIBLE);
                renderFillCode(data);
                break;
        }
    }

    private void hideAll() {
        layoutInfo.setVisibility(View.GONE);
        layoutQuestion.setVisibility(View.GONE);
        layoutFillCode.setVisibility(View.GONE);

        txtExplanation.setVisibility(View.GONE);

        btnNext.setVisibility(View.GONE);
        btnCheck.setVisibility(View.GONE);
    }

    private void renderQuestion(JSONObject data) {

        txtQuestion.setText(data.optString("question"));

        JSONArray options = data.optJSONArray("options");

        try {
            option1.setText(options.getString(0));
            option2.setText(options.getString(1));
            option3.setText(options.getString(2));
        } catch (Exception e) {
            e.printStackTrace();
        }

        selectedIndex = -1;
        resetOptions();

        btnCheck.setVisibility(View.VISIBLE);
        btnCheck.setEnabled(false);
        btnCheck.setAlpha(0.5f);

        btnNext.setVisibility(View.GONE);

        option1.setOnClickListener(v -> selectOption(0));
        option2.setOnClickListener(v -> selectOption(1));
        option3.setOnClickListener(v -> selectOption(2));

        btnCheck.setOnClickListener(v -> {

            int correctIndex = data.optInt("correctIndex", -1);

            if (selectedIndex == correctIndex) {

                highlightCorrect(selectedIndex);

                txtExplanation.setText("✅ " + data.optString("explanation"));
                txtExplanation.setTextColor(0xFF22C55E);
                txtExplanation.setVisibility(View.VISIBLE);

                btnCheck.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);

            } else {

                highlightWrong(selectedIndex);

                txtExplanation.setText("❌ " + data.optString("explanation"));
                txtExplanation.setTextColor(0xFFEF4444);
                txtExplanation.setVisibility(View.VISIBLE);
            }
        });
    }

    private void selectOption(int index) {

        selectedIndex = index;
        resetOptions();

        if (index == 0) option1.setBackgroundTintList(ColorStateList.valueOf(0xFF6366F1));
        if (index == 1) option2.setBackgroundTintList(ColorStateList.valueOf(0xFF6366F1));
        if (index == 2) option3.setBackgroundTintList(ColorStateList.valueOf(0xFF6366F1));

        btnCheck.setEnabled(true);
        btnCheck.setAlpha(1f);
    }

    private void resetOptions() {
        option1.setBackgroundTintList(ColorStateList.valueOf(0xFF312E81));
        option2.setBackgroundTintList(ColorStateList.valueOf(0xFF312E81));
        option3.setBackgroundTintList(ColorStateList.valueOf(0xFF312E81));
    }

    private void highlightCorrect(int index) {
        if (index == 0) option1.setBackgroundTintList(ColorStateList.valueOf(0xFF22C55E));
        if (index == 1) option2.setBackgroundTintList(ColorStateList.valueOf(0xFF22C55E));
        if (index == 2) option3.setBackgroundTintList(ColorStateList.valueOf(0xFF22C55E));
    }

    private void highlightWrong(int index) {
        if (index == 0) option1.setBackgroundTintList(ColorStateList.valueOf(0xFFEF4444));
        if (index == 1) option2.setBackgroundTintList(ColorStateList.valueOf(0xFFEF4444));
        if (index == 2) option3.setBackgroundTintList(ColorStateList.valueOf(0xFFEF4444));
    }

    private void renderFillCode(JSONObject data) {

        currentCode = "";
        txtCodeResult.setText("");
        layoutWords.removeAllViews();

        JSONArray words = data.optJSONArray("words");

        for (int i = 0; i < words.length(); i++) {

            String word = words.optString(i);

            Button btn = new Button(this);
            btn.setText(word);
            btn.setAllCaps(false);
            btn.setTextColor(0xFFFFFFFF);
            btn.setBackgroundTintList(ColorStateList.valueOf(0xFF4338CA));

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(10, 10, 10, 10);
            btn.setLayoutParams(params);

            btn.setOnClickListener(v -> {
                currentCode += word + " ";
                txtCodeResult.setText(currentCode);

                btn.setEnabled(false);
                btn.setAlpha(0.4f);
            });

            layoutWords.addView(btn);
        }

        btnCheckFill.setOnClickListener(v -> {

            if (currentCode.trim().equals(data.optString("correctAnswer"))) {
                Toast.makeText(this, "Đúng!", Toast.LENGTH_SHORT).show();
                btnNext.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Sai rồi!", Toast.LENGTH_SHORT).show();
            }
        });

        btnResetFill.setOnClickListener(v -> renderFillCode(data));
    }

    private void nextStep() {

        currentIndex++;

        if (currentIndex < steps.size()) {
            showStep();
        } else {
            Toast.makeText(this, "Hoàn thành!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}