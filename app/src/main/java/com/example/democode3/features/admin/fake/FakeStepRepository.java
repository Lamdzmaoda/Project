package com.example.democode3.features.admin.fake;

import com.example.democode3.features.learning.model.LessonStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeStepRepository {

    private static final List<LessonStep>
            steps =
            new ArrayList<>();

    static {

        // TEXT

        LessonStep text =
                new LessonStep();

        text.id = "1";

        text.title =
                "Python Print";

        text.type =
                "TEXT";

        text.orderIndex = 1;

        text.status =
                "ACTIVE";

        text.xp = 10;

        text.data =
                new LessonStep.StepData();

        text.data.content =
                "print() dùng để hiển thị";

        steps.add(text);

        // QUIZ

        LessonStep quiz =
                new LessonStep();

        quiz.id = "2";

        quiz.title =
                "Quiz Print";

        quiz.type =
                "QUIZ";

        quiz.orderIndex = 2;

        quiz.status =
                "ACTIVE";

        quiz.xp = 20;

        quiz.data =
                new LessonStep.StepData();

        quiz.data.question =
                "Hàm nào dùng để in?";

        quiz.data.options =
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

        quiz.data.correctValue =
                "0";

        quiz.data.explanation =
                "Đúng rồi 😭🔥";

        steps.add(quiz);

        // CODE

        LessonStep code =
                new LessonStep();

        code.id = "3";

        code.title =
                "Code Hello";

        code.type =
                "CODE";

        code.orderIndex = 3;

        code.status =
                "ACTIVE";

        code.xp = 30;

        code.data =
                new LessonStep.StepData();

        code.data.content =
                "Hoàn thành print";

        code.data.template =
                "print([0])";

        code.data.answers =
                new ArrayList<>();

        code.data.answers.add(
                "\"Hello\""
        );

        steps.add(code);
    }

    public static List<LessonStep> getSteps() {

        return steps;
    }

    public static void deleteStep(
            LessonStep step
    ) {

        steps.remove(step);
    }

    public static void addStep(
            LessonStep step
    ) {

        steps.add(step);
    }
}