package com.example.democode3.features.learning.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.democode3.features.learning.fake.FakeLessonDetailRepository;
import com.example.democode3.features.learning.model.LessonCompleteResult;
import com.example.democode3.features.learning.model.LessonStep;
import com.example.democode3.features.learning.model.LessonUiState;

import java.util.List;

public class LessonViewModel
        extends ViewModel {

    // =====================================================
    // UI STATE
    // =====================================================

    private final MutableLiveData<LessonUiState>
            uiState = new MutableLiveData<>(
            new LessonUiState()
    );

    // =====================================================
    // COMPLETE EVENT
    // =====================================================

    private final MutableLiveData<LessonCompleteResult>
            completeEvent =
            new MutableLiveData<>();

    // =====================================================
    // REPOSITORY
    // =====================================================

    private final FakeLessonDetailRepository repository =
            new FakeLessonDetailRepository();

    // =====================================================
    // CURRENT LESSON ID
    // =====================================================

    private long currentLessonId;

    // =====================================================
    // OBSERVE
    // =====================================================

    public LiveData<LessonUiState> getUiState() {

        return uiState;
    }

    public LiveData<LessonCompleteResult>
    getCompleteEvent() {

        return completeEvent;
    }

    // =====================================================
    // LOAD LESSON
    // =====================================================

    public void loadLesson(
            long lessonId
    ) {

        currentLessonId = lessonId;

        LessonUiState state =
                uiState.getValue();

        if (state == null) {
            return;
        }

        List<LessonStep> steps =
                repository.getLessonSteps(
                        lessonId
                );

        state.lessonSteps = steps;

        state.currentStepIndex = 0;

        uiState.setValue(state);
    }

    // =====================================================
    // CURRENT STEP
    // =====================================================

    public LessonStep getCurrentStep() {

        LessonUiState state =
                uiState.getValue();

        if (state == null
                || state.lessonSteps.isEmpty()) {

            return null;
        }

        // =========================================
        // OUT OF RANGE
        // =========================================

        if (state.currentStepIndex
                >= state.lessonSteps.size()) {

            return null;
        }

        return state.lessonSteps.get(
                state.currentStepIndex
        );
    }

    // =====================================================
    // NEXT STEP
    // =====================================================

    public boolean nextStep() {

        LessonUiState state =
                uiState.getValue();

        if (state == null) {

            return false;
        }

        // =========================================
        // NEXT
        // =========================================

        state.currentStepIndex++;

        // =========================================
        // HAS NEXT
        // =========================================

        boolean hasNext =
                state.currentStepIndex
                        < state.lessonSteps.size();

        // =========================================
        // UPDATE UI
        // =========================================

        uiState.setValue(state);

        return hasNext;
    }

    // =====================================================
    // SELECT ANSWER
    // =====================================================

    public void selectAnswer(
            int index
    ) {

        LessonUiState state =
                uiState.getValue();

        if (state == null) {
            return;
        }

        state.selectedIndex = index;

        uiState.setValue(state);
    }

    // =====================================================
    // CHECK QUIZ
    // =====================================================

    public boolean checkQuizAnswer() {

        LessonUiState state =
                uiState.getValue();

        if (state == null) {
            return false;
        }

        LessonStep step =
                getCurrentStep();

        if (step == null) {
            return false;
        }

        state.answered = true;

        state.isCorrect =
                state.selectedIndex
                        ==
                        Integer.parseInt(
                                step.data.correctValue
                        );

        uiState.setValue(state);

        return state.isCorrect;
    }

    // =====================================================
    // RESET QUIZ
    // =====================================================

    public void resetQuizState() {

        LessonUiState state =
                uiState.getValue();

        if (state == null) {
            return;
        }

        state.selectedIndex = -1;

        state.answered = false;

        state.isCorrect = false;

        uiState.setValue(state);
    }
}