package com.example.appcodetest.ui;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;
import com.example.appcodetest.api.*;
import com.example.appcodetest.model.*;
import com.example.appcodetest.utils.Prefs;
import com.example.appcodetest.utils.UserProgress;

import java.util.*;

import retrofit2.*;

public class LessonLearnActivity extends AppCompatActivity {

    // =========================
    // Layout chính
    // =========================

    LinearLayout layoutInfo, layoutQuestion, layoutFillCode;
    LinearLayout layoutAnswers, layoutWords;

    // =========================
    // Text hiển thị
    // =========================

    TextView txtInfo, txtQuestion, txtExplanation, txtCodeResult;
    TextView txtXP, txtLevel;

    // =========================
    // Button chức năng
    // =========================

    Button btnCheck, btnNext, btnCheckFill;
    LinearLayout btnAI;

    // =========================
    // Data lesson
    // =========================

    List<LessonStep> steps = new ArrayList<>();

    int currentIndex = 0;
    String lessonId;

    // =========================
    // QUIZ state
    // =========================

    int selectedIndex = -1;
    List<Button> answerButtons = new ArrayList<>();

    // =========================
    // FILL state
    // =========================

    List<String> selectedWords = new ArrayList<>();

    // Chỉ khi trả lời đúng mới được next
    boolean isAnsweredCorrect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_learn);

        bindViews();

        // Lấy lessonId từ màn trước
        lessonId = getIntent().getStringExtra("LESSON_ID");

        if (lessonId == null) {
            Toast.makeText(this,
                    "Thiếu lessonId",
                    Toast.LENGTH_SHORT).show();

            finish();
            return;
        }

        // Load step từ API
        loadSteps();

        // Load XP + Level local
        updateXPUI();
    }

    // =========================
    // BIND VIEW
    // =========================

    private void bindViews() {

        layoutInfo = findViewById(R.id.layoutInfo);
        layoutQuestion = findViewById(R.id.layoutQuestion);
        layoutFillCode = findViewById(R.id.layoutFillCode);

        layoutAnswers = findViewById(R.id.layoutAnswers);
        layoutWords = findViewById(R.id.layoutWords);

        txtInfo = findViewById(R.id.txtInfo);
        txtQuestion = findViewById(R.id.txtQuestion);
        txtExplanation = findViewById(R.id.txtExplanation);

        txtXP = findViewById(R.id.txtXP);
        txtLevel = findViewById(R.id.txtLevel);

        btnCheck = findViewById(R.id.btnCheck);
        btnNext = findViewById(R.id.btnNext);
        btnCheckFill = findViewById(R.id.btnCheckFill);

        btnAI = findViewById(R.id.btnAI);

        // Chỉ khi trả lời đúng mới được next
        btnNext.setOnClickListener(v -> {
            if (isAnsweredCorrect) {
                nextStep();
            }
        });
    }

    // =========================
    // UPDATE XP UI
    // =========================

    private void updateXPUI() {

        txtXP.setText("XP: " + UserProgress.getXP(this));
        txtLevel.setText("Level " + UserProgress.getLevel(this));
    }

    // =========================
    // LOAD STEP API
    // =========================

    private void loadSteps() {

        String token = Prefs.getToken(this);

        // check token tránh crash
        if (token == null || token.isEmpty()) {
            Toast.makeText(this,
                    "Chưa đăng nhập!",
                    Toast.LENGTH_SHORT).show();

            finish();
            return;
        }

        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.getSteps(lessonId, "Bearer " + token)
                .enqueue(new Callback<ApiResponse<List<LessonStep>>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<List<LessonStep>>> call,
                                           Response<ApiResponse<List<LessonStep>>> response) {

                        Log.d("API_STEP",
                                "code = " + response.code());

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().result != null) {

                            steps = response.body().result;

                            if (steps.isEmpty()) {
                                Toast.makeText(LessonLearnActivity.this,
                                        "Không có step!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            currentIndex = 0;
                            showStep();

                        } else {
                            Toast.makeText(LessonLearnActivity.this,
                                    "API lỗi!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<LessonStep>>> call,
                                          Throwable t) {

                        Toast.makeText(LessonLearnActivity.this,
                                "Lỗi kết nối server!",
                                Toast.LENGTH_SHORT).show();

                        t.printStackTrace();
                        Log.e("API_STEP",
                                "fail = " + t.getMessage());
                    }
                });
    }

    // =========================
    // SHOW STEP
    // =========================

    private void showStep() {

        hideAll();

        // mặc định chưa được qua step tiếp theo
        isAnsweredCorrect = false;

        btnNext.setEnabled(false);
        btnNext.setAlpha(0.3f);

        btnCheck.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.GONE);

        if (btnAI != null) {
            btnAI.setVisibility(View.GONE);
        }

        if (steps == null || steps.isEmpty()) return;

        LessonStep step = steps.get(currentIndex);

        // nếu step lỗi thì bỏ qua
        if (step == null || step.data == null) {
            nextStep();
            return;
        }

        LessonStep.StepData data = step.data;

        // =========================
        // INFO
        // =========================

        if ("INFO".equals(step.type)) {

            layoutInfo.setVisibility(View.VISIBLE);

            txtInfo.setText(
                    data.question != null ? data.question : ""
            );

            // INFO không cần trả lời
            isAnsweredCorrect = true;

            btnCheck.setVisibility(View.GONE);

            btnNext.setVisibility(View.VISIBLE);
            btnNext.setEnabled(true);
            btnNext.setAlpha(1f);
        }

        // =========================
        // CODE
        // =========================

        else if ("CODE".equals(step.type)) {

            layoutInfo.setVisibility(View.VISIBLE);

            txtInfo.setText(
                    data.question != null ? data.question : ""
            );

            isAnsweredCorrect = true;

            btnCheck.setVisibility(View.GONE);

            btnNext.setVisibility(View.VISIBLE);
            btnNext.setEnabled(true);
            btnNext.setAlpha(1f);
        }

        // =========================
        // QUESTION / QUIZ
        // =========================

        else if ("QUESTION".equals(step.type)
                || "QUIZ".equals(step.type)) {

            layoutQuestion.setVisibility(View.VISIBLE);
            renderQuiz(data, step.xp);
        }

        // =========================
        // FILL_INLINE / FILL_CHOICE
        // =========================

        else if ("FILL_INLINE".equals(step.type)
                || "FILL_CHOICE".equals(step.type)) {

            layoutFillCode.setVisibility(View.VISIBLE);
            renderFillCode(data, step.xp);
        }

        // =========================
        // TYPE CHƯA HỖ TRỢ
        // =========================

        else {

            Toast.makeText(this,
                    "Step type chưa hỗ trợ: " + step.type,
                    Toast.LENGTH_SHORT).show();

            nextStep();
        }
    }

    // =========================
    // ẨN TẤT CẢ LAYOUT
    // =========================

    private void hideAll() {

        layoutInfo.setVisibility(View.GONE);
        layoutQuestion.setVisibility(View.GONE);
        layoutFillCode.setVisibility(View.GONE);

        btnCheck.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
        btnCheckFill.setVisibility(View.GONE);

        txtExplanation.setText("");

        layoutAnswers.removeAllViews();
        layoutWords.removeAllViews();

        answerButtons.clear();
        selectedWords.clear();

        selectedIndex = -1;
    }

    // =========================
    // QUIZ / QUESTION
    // =========================

    private void renderQuiz(LessonStep.StepData data, double xp) {

        txtQuestion.setText(
                data.question != null ? data.question : ""
        );

        layoutAnswers.removeAllViews();
        answerButtons.clear();

        if (data.options == null || data.options.isEmpty()) {
            return;
        }

        for (int i = 0; i < data.options.size(); i++) {

            Button btn = new Button(this);

            String answer = data.options.get(i);
            int index = i;

            btn.setText(answer);
            btn.setAllCaps(false);

            btn.setOnClickListener(v -> {

                selectedIndex = index;

                for (Button b : answerButtons) {
                    b.setBackgroundTintList(
                            ColorStateList.valueOf(0xFF334155)
                    );
                }

                btn.setBackgroundTintList(
                        ColorStateList.valueOf(0xFF7C3AED)
                );
            });

            layoutAnswers.addView(btn);
            answerButtons.add(btn);
        }

        btnCheck.setVisibility(View.VISIBLE);

        btnCheck.setOnClickListener(v -> {

            if (selectedIndex == -1) {
                Toast.makeText(this,
                        "Chọn đáp án trước!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // trả lời đúng
            if (selectedIndex == data.correctValue) {

                txtExplanation.setText("✅ Chính xác!");

                isAnsweredCorrect = true;

                UserProgress.addXP(this, (int) xp);
                updateXPUI();

                btnNext.setVisibility(View.VISIBLE);
                btnNext.setEnabled(true);
                btnNext.setAlpha(1f);

                btnCheck.setVisibility(View.GONE);
            }

            // trả lời sai
            else {

                txtExplanation.setText(
                        "❌ Sai rồi!\n" +
                                (data.explanation != null
                                        ? data.explanation
                                        : "")
                );
            }
        });
    }

    // =========================
    // FILL CODE
    // =========================

    private void renderFillCode(LessonStep.StepData data, double xp) {

        txtQuestion.setText(
                data.question != null ? data.question : ""
        );

        layoutWords.removeAllViews();
        selectedWords.clear();

        if (data.options == null || data.options.isEmpty()) {
            return;
        }

        for (String word : data.options) {

            Button btn = new Button(this);

            btn.setText(word);
            btn.setAllCaps(false);

            btn.setOnClickListener(v -> {

                selectedWords.add(word);

                btn.setEnabled(false);
                btn.setAlpha(0.5f);
            });

            layoutWords.addView(btn);
        }

        btnCheckFill.setVisibility(View.VISIBLE);

        btnCheckFill.setOnClickListener(v -> {

            if (data.answers == null || data.answers.isEmpty()) {
                return;
            }

            boolean correct = true;

            if (selectedWords.size() != data.answers.size()) {
                correct = false;

            }

            if (correct) {
                for (int i = 0; i < data.answers.size(); i++) {

                    int correctIndex =
                            (int) Double.parseDouble(
                                    data.answers.get(i).toString()
                            );

                    if (!selectedWords.get(i).equals(
                            data.options.get(correctIndex)
                    )) {
                        correct = false;
                        break;
                    }
                }
            }

            // đúng
            if (correct) {

                txtExplanation.setText("✅ Chính xác!");

                isAnsweredCorrect = true;

                UserProgress.addXP(this, (int) xp);
                updateXPUI();

                btnNext.setVisibility(View.VISIBLE);
                btnNext.setEnabled(true);
                btnNext.setAlpha(1f);

                btnCheckFill.setVisibility(View.GONE);
            }

            // sai
            else {
                txtExplanation.setText("❌ Sai rồi!");
            }
        });
    }

    // =========================
    // STEP TIẾP THEO
    // =========================

    private void nextStep() {

        currentIndex++;

        // hết lesson
        if (currentIndex >= steps.size()) {

            new AlertDialog.Builder(this)
                    .setTitle("Hoàn thành 🎉")
                    .setMessage("Bạn đã hoàn thành lesson này!")
                    .setPositiveButton("OK",
                            (dialog, which) -> finish())
                    .show();

            return;
        }

        showStep();
    }
}