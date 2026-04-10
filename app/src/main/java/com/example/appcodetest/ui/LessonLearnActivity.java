package com.example.appcodetest.ui;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;
import com.example.appcodetest.api.ApiService;
import com.example.appcodetest.api.RetrofitClient;
import com.example.appcodetest.model.ApiResponse;
import com.example.appcodetest.model.LessonStep;
import com.example.appcodetest.utils.UserProgress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.*;

public class LessonLearnActivity extends AppCompatActivity {

    LinearLayout layoutInfo, layoutQuestion, layoutFillCode, layoutWords;

    TextView txtInfo, txtQuestion, txtExplanation, txtCodeResult;
    TextView txtXP, txtLevel;

    Button option1, option2, option3;
    Button btnCheck, btnNext, btnCheckFill;

    List<LessonStep> steps;
    int currentIndex = 0;
    String lessonId;

    int selectedIndex = -1;

    List<String> selectedWords = new ArrayList<>();
    List<String> correctWords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_learn);

        bindViews();

        lessonId = getIntent().getStringExtra("LESSON_ID");

        loadSteps();
        updateXPUI();
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

        txtXP = findViewById(R.id.txtXP);
        txtLevel = findViewById(R.id.txtLevel);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);

        btnCheck = findViewById(R.id.btnCheck);
        btnNext = findViewById(R.id.btnNext);
        btnCheckFill = findViewById(R.id.btnCheckFill);

        btnNext.setOnClickListener(v -> nextStep());
    }

    private void updateXPUI() {
        txtXP.setText("XP: " + UserProgress.getXP(this));
        txtLevel.setText("Level " + UserProgress.getLevel(this));
    }

    private void loadSteps() {

        String token = getSharedPreferences("APP", MODE_PRIVATE)
                .getString("TOKEN", "");

        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.getSteps(lessonId, "Bearer " + token)
                .enqueue(new Callback<ApiResponse<List<LessonStep>>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<List<LessonStep>>> call,
                                           Response<ApiResponse<List<LessonStep>>> response) {

                        steps = response.body().result;
                        showStep();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<LessonStep>>> call, Throwable t) {}
                });
    }

    private void showStep() {

        hideAll();

        LessonStep step = steps.get(currentIndex);
        LessonStep.StepData data = step.data;

        if (step.type.equals("INFO")) {

            layoutInfo.setVisibility(View.VISIBLE);
            txtInfo.setText(data.question);
            btnNext.setVisibility(View.VISIBLE);

        } else if (step.type.equals("QUIZ")) {

            layoutQuestion.setVisibility(View.VISIBLE);
            renderQuiz(data, step.xp);

        } else if (step.type.equals("FILL_CODE")) {

            layoutFillCode.setVisibility(View.VISIBLE);
            renderFillCode(data, step.xp);
        }
    }

    // 🔥 FIX: xp = double
    private void renderQuiz(LessonStep.StepData data, double xp) {

        txtQuestion.setText(data.question);

        option1.setText(data.options.get(0));
        option2.setText(data.options.get(1));
        option3.setText(data.options.get(2));

        selectedIndex = -1;
        resetOptions();

        option1.setOnClickListener(v -> select(0));
        option2.setOnClickListener(v -> select(1));
        option3.setOnClickListener(v -> select(2));

        btnCheck.setOnClickListener(v -> {

            if (selectedIndex == data.correctValue) {

                highlightCorrect(selectedIndex);
                txtExplanation.setText("✅ " + data.explanation);
                txtExplanation.setTextColor(0xFF22C55E);

                // 🎯 FIX: ép int
                UserProgress.addXP(this, (int) xp);
                updateXPUI();

                txtExplanation.postDelayed(this::nextStep, 1000);

            } else {

                highlightWrong(selectedIndex);
                txtExplanation.setText("❌ " + data.explanation);
                txtExplanation.setTextColor(0xFFEF4444);
            }

            txtExplanation.setVisibility(View.VISIBLE);
        });
    }

    // 🔥 FIX: xp = double
    private void renderFillCode(LessonStep.StepData data, double xp) {

        txtCodeResult.setText("");
        layoutWords.removeAllViews();

        selectedWords.clear();
        correctWords = new ArrayList<>(data.options);

        List<String> shuffled = new ArrayList<>(data.options);
        Collections.shuffle(shuffled);

        for (String word : shuffled) {

            Button b = new Button(this);
            b.setText(word);
            b.setOnClickListener(v -> {
                selectedWords.add(word);
                txtCodeResult.setText(String.join(" ", selectedWords));
            });

            layoutWords.addView(b);
        }

        btnCheckFill.setOnClickListener(v -> {

            if (selectedWords.equals(correctWords)) {

                txtExplanation.setText("✅ Chính xác!");
                txtExplanation.setTextColor(0xFF22C55E);

                // 🎯 FIX
                UserProgress.addXP(this, (int) xp);
                updateXPUI();

                txtExplanation.postDelayed(this::nextStep, 1000);

            } else {

                txtExplanation.setText("❌ Sai rồi!");
                txtExplanation.setTextColor(0xFFEF4444);
            }

            txtExplanation.setVisibility(View.VISIBLE);
        });
    }

    private void select(int i) {
        selectedIndex = i;
        resetOptions();

        if (i == 0) option1.setBackgroundTintList(ColorStateList.valueOf(0xFF6366F1));
        if (i == 1) option2.setBackgroundTintList(ColorStateList.valueOf(0xFF6366F1));
        if (i == 2) option3.setBackgroundTintList(ColorStateList.valueOf(0xFF6366F1));
    }

    private void resetOptions() {
        option1.setBackgroundTintList(ColorStateList.valueOf(0xFF1E293B));
        option2.setBackgroundTintList(ColorStateList.valueOf(0xFF1E293B));
        option3.setBackgroundTintList(ColorStateList.valueOf(0xFF1E293B));
    }

    private void highlightCorrect(int i) {
        if (i == 0) option1.setBackgroundTintList(ColorStateList.valueOf(0xFF22C55E));
        if (i == 1) option2.setBackgroundTintList(ColorStateList.valueOf(0xFF22C55E));
        if (i == 2) option3.setBackgroundTintList(ColorStateList.valueOf(0xFF22C55E));
    }

    private void highlightWrong(int i) {
        if (i == 0) option1.setBackgroundTintList(ColorStateList.valueOf(0xFFEF4444));
        if (i == 1) option2.setBackgroundTintList(ColorStateList.valueOf(0xFFEF4444));
        if (i == 2) option3.setBackgroundTintList(ColorStateList.valueOf(0xFFEF4444));
    }

    private void hideAll() {
        layoutInfo.setVisibility(View.GONE);
        layoutQuestion.setVisibility(View.GONE);
        layoutFillCode.setVisibility(View.GONE);
        txtExplanation.setVisibility(View.GONE);
    }

    private void nextStep() {
        currentIndex++;

        if (currentIndex < steps.size()) {
            showStep();
        } else {
            Toast.makeText(this, "🎉 Hoàn thành bài!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}