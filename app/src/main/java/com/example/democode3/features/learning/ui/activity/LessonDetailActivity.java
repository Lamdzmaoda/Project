package com.example.democode3.features.learning.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.democode3.R;
import com.example.democode3.core.network.RetrofitClient;
import com.example.democode3.features.ai.api.ChatApiService;
import com.example.democode3.features.ai.model.ChatRequest;
import com.example.democode3.features.ai.model.ChatResponse;
import com.example.democode3.features.learning.fake.FakeLessonDetailRepository;
import com.example.democode3.features.learning.model.LessonStep;
import com.example.democode3.features.learning.model.LessonUiState;
import com.example.democode3.features.learning.ui.component.LearningRewardDialog;
import com.example.democode3.features.learning.ui.handler.QuizResultHandler;
import com.example.democode3.features.learning.ui.renderer.CodeRendererAdapter;
import com.example.democode3.features.learning.ui.renderer.ProgressRenderer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonDetailActivity
        extends AppCompatActivity {

    // =================================================
    // VIEW
    // =================================================

    private TextView txtContent;

    private TextView txtStepProgress;

    private View progressView;

    private Button btnNext;

    // =================================================
    // QUIZ
    // =================================================

    private LinearLayout layoutQuiz;

    private LinearLayout layoutAnswerA;

    private LinearLayout layoutAnswerB;

    private TextView btnAnswerA;

    private TextView btnAnswerB;

    private View viewCircleA;

    private View viewCircleB;

    // =================================================
    // CODE
    // =================================================

    private LinearLayout layoutCode;

    private LinearLayout layoutCodeSlots;

    private LinearLayout layoutCodeWords;

    // =================================================
    // RESULT
    // =================================================

    private LinearLayout layoutResult;

    private LinearLayout layoutAi;

    private TextView txtResult;

    private TextView txtAiDescription;

    private TextView btnAskAi;

    // =================================================
    // STATE
    // =================================================

    private final LessonUiState uiState =
            new LessonUiState();

    // =================================================
    // DATA
    // =================================================

    private List<LessonStep> lessonSteps;

    // =================================================
    // HANDLER
    // =================================================

    private final QuizResultHandler quizResultHandler =
            new QuizResultHandler();

    // =================================================
    // ON CREATE
    // =================================================

    @Override
    protected void onCreate(
            @Nullable Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_lesson_detail
        );

        initView();

        loadFakeData();

        renderStep();
    }

    // =================================================
    // INIT
    // =================================================

    private void initView() {

        txtContent =
                findViewById(R.id.txtContent);

        txtStepProgress =
                findViewById(R.id.txtStepProgress);

        progressView =
                findViewById(R.id.progressView);

        btnNext =
                findViewById(R.id.btnNext);

        // QUIZ

        layoutQuiz =
                findViewById(R.id.layoutQuiz);

        layoutAnswerA =
                findViewById(R.id.layoutAnswerA);

        layoutAnswerB =
                findViewById(R.id.layoutAnswerB);

        btnAnswerA =
                findViewById(R.id.btnAnswerA);

        btnAnswerB =
                findViewById(R.id.btnAnswerB);

        viewCircleA =
                findViewById(R.id.viewCircleA);

        viewCircleB =
                findViewById(R.id.viewCircleB);

        // CODE

        layoutCode =
                findViewById(R.id.layoutCode);

        layoutCodeSlots =
                findViewById(R.id.layoutCodeSlots);

        layoutCodeWords =
                findViewById(R.id.layoutCodeWords);

        // RESULT

        layoutResult =
                findViewById(R.id.layoutResult);

        layoutAi =
                findViewById(R.id.layoutAi);

        txtResult =
                findViewById(R.id.txtResult);

        txtAiDescription =
                findViewById(R.id.txtAiDescription);

        btnAskAi =
                findViewById(R.id.btnAskAi);
    }

    // =================================================
    // LOAD DATA
    // =================================================

    private void loadFakeData() {

        // =====================================
        // GET LESSON ID
        // =====================================

        long lessonId =
                getIntent().getLongExtra(
                        "lessonId",
                        1
                );

        // =====================================
        // REPOSITORY
        // =====================================

        FakeLessonDetailRepository repository =
                new FakeLessonDetailRepository();

        // =====================================
        // LOAD STEPS
        // =====================================

        lessonSteps =
                repository.getLessonSteps(
                        lessonId
                );

        // =====================================
        // UI STATE
        // =====================================

        uiState.lessonSteps =
                lessonSteps;

        uiState.totalSteps =
                lessonSteps.size();
    }

    // =================================================
    // RENDER STEP
    // =================================================

    private void renderStep() {

        resetUi();

        LessonStep step =
                lessonSteps.get(
                        uiState.currentStepIndex
                );

        ProgressRenderer progressRenderer =
                new ProgressRenderer(
                        txtStepProgress,
                        progressView
                );

        progressRenderer.render(

                uiState.currentStepIndex + 1,

                uiState.totalSteps
        );

        switch (step.type) {

            case "TEXT":

                renderText(step);

                break;

            case "QUIZ":

                renderQuiz(step);

                break;

            case "CODE":

                renderCode(step);

                break;

            case "AI_HINT":

                renderAi(step);

                break;
        }
    }

    // =================================================
    // TEXT
    // =================================================

    private void renderText(
            LessonStep step
    ) {

        txtContent.setText(
                step.data.content
        );

        btnNext.setVisibility(
                View.VISIBLE
        );

        btnNext.setText(
                "TIẾP TỤC"
        );

        btnNext.setEnabled(true);

        btnNext.setAlpha(1f);

        btnNext.setBackgroundResource(
                R.drawable.bg_primary_button
        );

        btnNext.setOnClickListener(v -> {

            nextStep();
        });
    }

    // =================================================
    // QUIZ
    // =================================================

    private void renderQuiz(
            LessonStep step
    ) {

        layoutQuiz.setVisibility(
                View.VISIBLE
        );

        txtContent.setText(
                step.data.question
        );

        // ANSWERS

        btnAnswerA.setText(

                step.data.options
                        .get(0)
                        .text
        );

        btnAnswerB.setText(

                step.data.options
                        .get(1)
                        .text
        );

        // RESET

        uiState.selectedIndex = -1;

        layoutAnswerA.setBackgroundResource(
                R.drawable.bg_quiz_option
        );

        layoutAnswerB.setBackgroundResource(
                R.drawable.bg_quiz_option
        );

        viewCircleA.setBackgroundResource(
                R.drawable.bg_radio_unselected
        );

        viewCircleB.setBackgroundResource(
                R.drawable.bg_radio_unselected
        );

        layoutResult.setVisibility(
                View.GONE
        );

        layoutAi.setVisibility(
                View.GONE
        );

        // BUTTON

        btnNext.setVisibility(
                View.VISIBLE
        );

        btnNext.setText(
                "KIỂM TRA"
        );

        btnNext.setEnabled(true);

        btnNext.setAlpha(1f);

        btnNext.setBackgroundResource(
                R.drawable.bg_primary_button
        );

        // SELECT A

        layoutAnswerA.setOnClickListener(v -> {

            uiState.selectedIndex = 0;

            layoutAnswerA.setBackgroundResource(
                    R.drawable.bg_quiz_option_selected
            );

            layoutAnswerB.setBackgroundResource(
                    R.drawable.bg_quiz_option
            );

            viewCircleA.setBackgroundResource(
                    R.drawable.bg_radio_selected
            );

            viewCircleB.setBackgroundResource(
                    R.drawable.bg_radio_unselected
            );
        });

        // SELECT B

        layoutAnswerB.setOnClickListener(v -> {

            uiState.selectedIndex = 1;

            layoutAnswerB.setBackgroundResource(
                    R.drawable.bg_quiz_option_selected
            );

            layoutAnswerA.setBackgroundResource(
                    R.drawable.bg_quiz_option
            );

            viewCircleB.setBackgroundResource(
                    R.drawable.bg_radio_selected
            );

            viewCircleA.setBackgroundResource(
                    R.drawable.bg_radio_unselected
            );
        });

        // CHECK

        btnNext.setOnClickListener(v -> {

            checkAnswer(step);
        });

        // =================================================
        // AI
        // =================================================

        btnAskAi.setOnClickListener(v -> {

            txtAiDescription.setText(
                    "Đang hỏi AI..."
            );

            ChatApiService apiService =

                    RetrofitClient
                            .getInstance(
                                    LessonDetailActivity.this
                            )
                            .create(
                                    ChatApiService.class
                            );

            ChatRequest request =
                    new ChatRequest(

                            "Giải thích bài học này",

                            step.id,

                            null
                    );

            apiService.askAi(request)

                    .enqueue(

                            new Callback<ChatResponse>() {

                                @Override
                                public void onResponse(

                                        Call<ChatResponse> call,

                                        Response<ChatResponse> response
                                ) {

                                    if (

                                            response.body() != null
                                                    &&
                                                    response.body().result != null
                                    ) {

                                        txtAiDescription.setText(

                                                response.body()
                                                        .result
                                                        .aiExplanation
                                        );

                                    } else {

                                        txtAiDescription.setText(
                                                "AI chưa phản hồi 😭"
                                        );
                                    }
                                }

                                @Override
                                public void onFailure(

                                        Call<ChatResponse> call,

                                        Throwable t
                                ) {

                                    txtAiDescription.setText(

                                            "Lỗi AI: "
                                                    + t.getMessage()
                                    );

                                    Toast.makeText(

                                            LessonDetailActivity.this,

                                            t.getMessage(),

                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                    );
        });
    }

    // =================================================
    // CHECK ANSWER
    // =================================================

    private void checkAnswer(
            LessonStep step
    ) {

        if (uiState.selectedIndex == -1) {

            return;
        }

        boolean correct =
                String.valueOf(
                        uiState.selectedIndex
                ).equals(
                        step.data.correctValue
                );

        if (correct) {

            quizResultHandler.handleCorrect(

                    uiState.selectedIndex,

                    layoutAnswerA,

                    layoutAnswerB,

                    layoutResult,

                    layoutAi,

                    txtResult,

                    btnNext,

                    this::nextStep
            );

        } else {

            quizResultHandler.handleWrong(

                    uiState.selectedIndex,

                    layoutAnswerA,

                    layoutAnswerB,

                    layoutResult,

                    layoutAi,

                    txtResult,

                    btnNext,

                    () -> {

                        renderQuiz(step);
                    }
            );
        }
    }

    // =================================================
    // CODE
    // =================================================

    private void renderCode(
            LessonStep step
    ) {

        layoutCode.setVisibility(
                View.VISIBLE
        );

        btnNext.setVisibility(
                View.VISIBLE
        );

        btnNext.setEnabled(true);

        btnNext.setAlpha(1f);

        btnNext.setText(
                "KIỂM TRA"
        );

        btnNext.setBackgroundResource(
                R.drawable.bg_primary_button
        );

        CodeRendererAdapter adapter =
                new CodeRendererAdapter(

                        this,

                        layoutCode,

                        layoutCodeSlots,

                        layoutCodeWords,

                        txtContent
                );

        adapter.render(step);

        btnNext.setOnClickListener(v -> {

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

            btnNext.setText(
                    "TIẾP TỤC"
            );

            btnNext.setOnClickListener(v2 -> {

                nextStep();
            });
        });
    }

    // =================================================
    // AI
    // =================================================

    private void renderAi(
            LessonStep step
    ) {

        txtContent.setText(
                step.data.content
        );

        btnNext.setVisibility(
                View.VISIBLE
        );

        btnNext.setEnabled(true);

        btnNext.setAlpha(1f);

        btnNext.setText(
                "TIẾP TỤC"
        );

        btnNext.setBackgroundResource(
                R.drawable.bg_primary_button
        );

        btnNext.setOnClickListener(v -> {

            nextStep();
        });
    }

    // =================================================
    // NEXT
    // =================================================

    private void nextStep() {

        uiState.currentStepIndex++;

        if (

                uiState.currentStepIndex
                        >= lessonSteps.size()
        ) {

            showRewardDialog();

            return;
        }

        renderStep();
    }

    // =================================================
    // REWARD
    // =================================================

    private void showRewardDialog() {

        LearningRewardDialog dialog =
                new LearningRewardDialog(
                        50,
                        5
                );

        dialog.show(
                getSupportFragmentManager(),
                "reward_dialog"
        );
    }

    // =================================================
    // RESET UI
    // =================================================

    private void resetUi() {

        layoutQuiz.setVisibility(
                View.GONE
        );

        layoutCode.setVisibility(
                View.GONE
        );

        layoutResult.setVisibility(
                View.GONE
        );

        layoutAi.setVisibility(
                View.GONE
        );
    }
}