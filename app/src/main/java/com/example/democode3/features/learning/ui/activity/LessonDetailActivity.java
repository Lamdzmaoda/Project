package com.example.democode3.features.learning.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.democode3.R;
import com.example.democode3.features.learning.model.LessonStep;
import com.example.democode3.features.learning.model.LessonUiState;
import com.example.democode3.features.learning.ui.controller.QuizController;
import com.example.democode3.features.learning.ui.handler.QuizResultHandler;
import com.example.democode3.features.learning.ui.navigation.LessonNavigator;
import com.example.democode3.features.learning.ui.registry.StepRegistry;
import com.example.democode3.features.learning.ui.renderer.AiHintRendererAdapter;
import com.example.democode3.features.learning.ui.renderer.CodeRendererAdapter;
import com.example.democode3.features.learning.ui.renderer.ProgressRenderer;
import com.example.democode3.features.learning.ui.renderer.QuizRendererAdapter;
import com.example.democode3.features.learning.ui.renderer.TextRendererAdapter;
import com.example.democode3.features.learning.ui.viewmodel.LessonViewModel;

public class LessonDetailActivity
        extends AppCompatActivity {

    // =====================================================
    // VIEW
    // =====================================================

    private TextView txtContent;

    private TextView txtStepProgress;

    private View progressView;

    private Button btnNext;

    // =====================================================
    // FOOTER
    // =====================================================

    private LinearLayout layoutNormalFooter;

    private LinearLayout layoutCodeFooter;

    // =====================================================
    // QUIZ
    // =====================================================

    private LinearLayout layoutQuiz;

    private Button btnAnswerA;

    private Button btnAnswerB;

    // =====================================================
    // CODE
    // =====================================================

    private LinearLayout layoutCode;

    private TextView txtCodeQuestion;

    private LinearLayout layoutCodeSlots;

    private LinearLayout layoutCodeWords;

    private Button btnCheckCode;

    private Button btnResetCode;

    private Button btnClearCode;

    // =====================================================
    // DATA
    // =====================================================

    private long lessonId;

    // =====================================================
    // VIEWMODEL
    // =====================================================

    private LessonViewModel viewModel;

    private StepRegistry stepRegistry;

    private ProgressRenderer progressRenderer;

    private QuizResultHandler quizResultHandler;

    private QuizController quizController;

    private LessonNavigator lessonNavigator;

    // =====================================================
    // UI STATE
    // =====================================================

    private final LessonUiState uiState =
            new LessonUiState();

    // =====================================================
    // RENDER STATE
    // =====================================================

    private int currentRenderedStepIndex = -1;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_lesson_detail
        );

        viewModel =
                new ViewModelProvider(this)
                        .get(LessonViewModel.class);

        initViews();

        stepRegistry =
                new StepRegistry();

        progressRenderer =
                new ProgressRenderer(

                        txtStepProgress,

                        progressView
                );

        quizResultHandler =
                new QuizResultHandler();

        quizController =
                new QuizController(

                        viewModel,

                        uiState,

                        btnAnswerA,

                        btnAnswerB,

                        btnNext
                );

        getIntentData();

        lessonNavigator =
                new LessonNavigator(

                        viewModel,

                        lessonId,

                        getSupportFragmentManager()
                );

        // =============================================
        // TEXT RENDERER
        // =============================================

        stepRegistry.register(
                "TEXT",

                new TextRendererAdapter(

                        txtContent,

                        btnNext
                )
        );

        // =============================================
        // QUIZ RENDERER
        // =============================================

        stepRegistry.register(

                "QUIZ",

                new QuizRendererAdapter(

                        layoutQuiz,

                        txtContent,

                        btnAnswerA,

                        btnAnswerB
                )
        );

        // =============================================
        // CODE RENDERER
        // =============================================

        stepRegistry.register(

                "CODE",

                new CodeRendererAdapter(

                        this,

                        uiState,

                        layoutCode,

                        txtContent,

                        txtCodeQuestion,

                        layoutCodeSlots,

                        layoutCodeWords,

                        btnCheckCode,

                        btnResetCode,

                        this::goToNextStep
                )
        );

        // =============================================
        // AI HINT RENDERER
        // =============================================

        stepRegistry.register(

                "AI_HINT",

                new AiHintRendererAdapter(

                        txtContent,

                        btnNext
                )
        );

        observeState();

        loadLessonSteps();

        setupClick();
    }

    // =====================================================
    // INIT VIEW
    // =====================================================

    private void initViews() {

        txtContent =
                findViewById(
                        R.id.txtContent
                );

        txtStepProgress =
                findViewById(
                        R.id.txtStepProgress
                );

        progressView =
                findViewById(
                        R.id.progressView
                );

        btnNext =
                findViewById(
                        R.id.btnNext
                );

        // =============================================
        // FOOTER
        // =============================================

        layoutNormalFooter =
                findViewById(
                        R.id.layoutNormalFooter
                );

        layoutCodeFooter =
                findViewById(
                        R.id.layoutCodeFooter
                );

        // =============================================
        // QUIZ
        // =============================================

        layoutQuiz =
                findViewById(
                        R.id.layoutQuiz
                );

        btnAnswerA =
                findViewById(
                        R.id.btnAnswerA
                );

        btnAnswerB =
                findViewById(
                        R.id.btnAnswerB
                );

        // =============================================
        // CODE
        // =============================================

        layoutCode =
                findViewById(
                        R.id.layoutCode
                );

        txtCodeQuestion =
                findViewById(
                        R.id.txtCodeQuestion
                );

        layoutCodeSlots =
                findViewById(
                        R.id.layoutCodeSlots
                );

        layoutCodeWords =
                findViewById(
                        R.id.layoutCodeWords
                );

        btnCheckCode =
                findViewById(
                        R.id.btnCheckCode
                );

        btnResetCode =
                findViewById(
                        R.id.btnResetCode
                );

        btnResetCode.setOnClickListener(v -> {

            renderCurrentStep();
        });

        btnClearCode =
                findViewById(
                        R.id.btnClearCode
                );
    }

    // =====================================================
    // INTENT
    // =====================================================

    private void getIntentData() {

        lessonId =
                getIntent().getLongExtra(
                        "lessonId",
                        1
                );
    }

    // =====================================================
    // LOAD STEP
    // =====================================================

    private void loadLessonSteps() {

        viewModel.loadLesson(
                lessonId
        );
    }

    // =====================================================
    // OBSERVE STATE
    // =====================================================

    private void observeState() {

        viewModel.getUiState().observe(

                this,

                state -> {

                    if (state.currentStepIndex
                            != currentRenderedStepIndex) {

                        currentRenderedStepIndex =
                                state.currentStepIndex;

                        renderCurrentStep();
                    }
                }
        );
    }

    // =====================================================
    // RENDER
    // =====================================================

    private void renderCurrentStep() {

        LessonStep step =
                viewModel.getCurrentStep();

        if (step == null) {

            return;
        }

        // =============================================
        // RESET
        // =============================================

        uiState.selectedIndex = -1;

        uiState.answered = false;

        uiState.isCorrect = false;

        layoutQuiz.setVisibility(
                View.GONE
        );

        layoutCode.setVisibility(
                View.GONE
        );

        txtContent.setVisibility(
                View.VISIBLE
        );

        // =============================================
        // FOOTER MODE
        // =============================================

        if (step.type.equals("CODE")) {

            layoutNormalFooter.setVisibility(
                    View.GONE
            );

            layoutCodeFooter.setVisibility(
                    View.VISIBLE
            );

        } else {

            layoutNormalFooter.setVisibility(
                    View.VISIBLE
            );

            layoutCodeFooter.setVisibility(
                    View.GONE
            );
        }

        // =============================================
        // RESET NORMAL BUTTON
        // =============================================

        btnNext.setEnabled(false);

        btnNext.setAlpha(0.4f);

        btnNext.setText("KIỂM TRA");

        // =============================================
        // RENDER BY REGISTRY
        // =============================================

        stepRegistry.render(step);

        // =============================================
        // PROGRESS
        // =============================================

        LessonUiState state =
                viewModel.getUiState().getValue();

        if (state != null) {

            progressRenderer.render(

                    state.currentStepIndex + 1,

                    state.lessonSteps.size()
            );
        }
    }

    // =====================================================
    // CLICK
    // =====================================================

    private void setupClick() {

        btnNext.setOnClickListener(v -> {

            LessonStep step =
                    viewModel.getCurrentStep();

            if (step == null) {

                return;
            }

            // =========================================
            // QUIZ FLOW
            // =========================================

            if (step.type.equals("QUIZ")) {

                if (!uiState.answered) {

                    checkQuizAnswer();

                    return;
                }

                if (uiState.isCorrect) {

                    goToNextStep();

                    return;
                }

                resetQuizState();

                return;
            }

            // =========================================
            // NORMAL FLOW
            // =========================================

            goToNextStep();
        });

        // =============================================
        // ANSWER A
        // =============================================

        btnAnswerA.setOnClickListener(v -> {

            quizController.selectAnswer(0);
        });

        // =============================================
        // ANSWER B
        // =============================================

        btnAnswerB.setOnClickListener(v -> {

            quizController.selectAnswer(1);
        });

        // =============================================
        // CLEAR CODE
        // =============================================

        btnClearCode.setOnClickListener(v -> {

            if (uiState.selectedCodeWords.isEmpty()) {

                return;
            }

            // =========================================
            // REMOVE LAST
            // =========================================

            uiState.selectedCodeWords.remove(

                    uiState.selectedCodeWords.size() - 1
            );

            // =========================================
            // RERENDER CURRENT STEP
            // =========================================

            LessonStep step =
                    viewModel.getCurrentStep();

            if (step != null) {

                stepRegistry.render(step);
            }
        });
    }

    // =====================================================
    // NEXT STEP
    // =====================================================

    private void goToNextStep() {

        boolean hasNext =
                lessonNavigator.goToNextStep();

        // =========================================
        // COMPLETE LESSON
        // =========================================

        if (!hasNext) {

            return;
        }

        // =========================================
        // NEXT STEP
        // =========================================

        renderCurrentStep();
    }

    // =====================================================
    // CHECK QUIZ
    // =====================================================

    private void checkQuizAnswer() {

        int selectedIndex =
                uiState.selectedIndex;

        boolean correct =
                viewModel.checkQuizAnswer();

        LessonStep step =
                viewModel.getCurrentStep();

        if (step == null) {

            return;
        }

        // =============================================
        // CORRECT
        // =============================================

        if (correct) {

            quizResultHandler.handleCorrect(

                    selectedIndex,

                    btnAnswerA,

                    btnAnswerB,

                    step,

                    getSupportFragmentManager(),

                    this::goToNextStep,

                    btnNext
            );
        }

        // =============================================
        // WRONG
        // =============================================

        else {

            quizResultHandler.handleWrong(

                    selectedIndex,

                    btnAnswerA,

                    btnAnswerB,

                    step,

                    getSupportFragmentManager(),

                    this::resetQuizState,

                    btnNext
            );
        }
    }

    // =====================================================
    // RESET QUIZ
    // =====================================================

    private void resetQuizState() {

        viewModel.resetQuizState();

        btnAnswerA.setBackgroundColor(
                0xFF4C4AA1
        );

        btnAnswerB.setBackgroundColor(
                0xFF4C4AA1
        );
    }
}