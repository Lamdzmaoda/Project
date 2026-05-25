package com.example.democode3.features.learning.fake;

import com.example.democode3.features.learning.model.LessonStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeLessonDetailRepository {

    public List<LessonStep> getLessonSteps(
            long lessonId
    ) {

        List<LessonStep> steps =
                new ArrayList<>();

        // =================================================
        // STEP 1 - TEXT
        // =================================================

        LessonStep textStep =
                new LessonStep();

        textStep.id =
                "1";

        textStep.type =
                "TEXT";

        textStep.title =
                "Python Print";

        textStep.orderIndex = 1;

        textStep.status =
                "ACTIVE";

        textStep.xp = 10;

        textStep.data =
                new LessonStep.StepData();

        textStep.data.content =
                "Trong Python, print() dùng để hiển thị dữ liệu.";

        steps.add(textStep);

        // =================================================
        // STEP 2 - QUIZ
        // =================================================

        LessonStep quizStep =
                new LessonStep();

        quizStep.id =
                "2";

        quizStep.type =
                "QUIZ";

        quizStep.title =
                "Quiz Print";

        quizStep.orderIndex = 2;

        quizStep.status =
                "ACTIVE";

        quizStep.xp = 20;

        quizStep.data =
                new LessonStep.StepData();

        quizStep.data.question =
                "Hàm nào dùng để in ra màn hình?";

        quizStep.data.options =
                Arrays.asList(

                        new LessonStep.Option(
                                "0",
                                "print()"
                        ),

                        new LessonStep.Option(
                                "1",
                                "echo()"
                        )
                );

        quizStep.data.correctValue =
                "0";

        quizStep.data.explanation =
                "Đúng rồi 😭🔥";

        steps.add(quizStep);

        // =================================================
        // STEP 3 - CODE
        // =================================================

        LessonStep codeStep =
                new LessonStep();

        codeStep.id =
                "3";

        codeStep.type =
                "CODE";

        codeStep.title =
                "Code Print";

        codeStep.orderIndex = 3;

        codeStep.status =
                "ACTIVE";

        codeStep.xp = 30;

        codeStep.data =
                new LessonStep.StepData();

        codeStep.data.content =
                "Hãy hoàn thành print Hello";

        codeStep.data.template =
                "print([0])";

        codeStep.data.answers =
                Arrays.asList(
                        "\"Hello\""
                );

        steps.add(codeStep);

        // =================================================
        // STEP 4 - AI HINT
        // =================================================

        LessonStep aiStep =
                new LessonStep();

        aiStep.id =
                "4";

        aiStep.type =
                "AI_HINT";

        aiStep.title =
                "AI Explain Variable";

        aiStep.orderIndex = 4;

        aiStep.status =
                "ACTIVE";

        aiStep.xp = 15;

        aiStep.data =
                new LessonStep.StepData();

        aiStep.data.content =
                "🤖 AI: Variable giúp lưu dữ liệu tạm thời 😭🔥";

        steps.add(aiStep);

        return steps;
    }
}