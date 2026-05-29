package com.example.democode3.features.learning.fake;

import com.example.democode3.features.learning.model.LessonStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeLessonDetailRepository {

    // =====================================
    // GET LESSON STEPS
    // =====================================

    public List<LessonStep> getLessonSteps(
            long lessonId
    ) {

        return getPythonBeginnerLesson();
    }

    // =====================================
    // PYTHON BEGINNER LESSON
    // =====================================

    private List<LessonStep> getPythonBeginnerLesson() {

        List<LessonStep> steps =
                new ArrayList<>();

        // =====================================
        // STEP 1 - TEXT
        // =====================================

        LessonStep step1 =
                new LessonStep();

        step1.id = "1";

        step1.type = "TEXT";

        step1.title = "Python là gì";

        step1.orderIndex = 1;

        step1.status = "ACTIVE";

        step1.xp = 10;

        step1.data =
                new LessonStep.StepData();

        step1.data.content =
                "Python là một ngôn ngữ lập trình dễ học và cực kỳ phổ biến trong AI, Web và Game.";

        steps.add(step1);

        // =====================================
        // STEP 2 - QUIZ
        // =====================================

        LessonStep step2 =
                new LessonStep();

        step2.id = "2";

        step2.type = "QUIZ";

        step2.title = "Quiz Python";

        step2.orderIndex = 2;

        step2.status = "ACTIVE";

        step2.xp = 15;

        step2.data =
                new LessonStep.StepData();

        step2.data.question =
                "Python là gì?";

        step2.data.options =
                Arrays.asList(

                        new LessonStep.Option(
                                "0",
                                "Ngôn ngữ lập trình"
                        ),

                        new LessonStep.Option(
                                "1",
                                "Trò chơi"
                        )
                );

        step2.data.correctValue =
                "0";

        step2.data.explanation =
                "Python là ngôn ngữ lập trình";

        steps.add(step2);

        // =====================================
        // STEP 3 - TEXT
        // =====================================

        LessonStep step3 =
                new LessonStep();

        step3.id = "3";

        step3.type = "TEXT";

        step3.title = "Lệnh print";

        step3.orderIndex = 3;

        step3.status = "ACTIVE";

        step3.xp = 10;

        step3.data =
                new LessonStep.StepData();

        step3.data.content =
                "Trong Python, print() dùng để hiển thị dữ liệu ra màn hình.";

        steps.add(step3);

        // =====================================
        // STEP 4 - CODE
        // =====================================

        LessonStep step4 =
                new LessonStep();

        step4.id = "4";

        step4.type = "CODE";

        step4.title = "Code Hello";

        step4.orderIndex = 4;

        step4.status = "ACTIVE";

        step4.xp = 25;

        step4.data =
                new LessonStep.StepData();

        step4.data.content =
                "Hãy hoàn thành câu lệnh để in Hello World";

        step4.data.template =
                "print([0])";

        step4.data.answers =
                Arrays.asList(
                        "\"Hello World\""
                );

        steps.add(step4);

        // =====================================
        // STEP 5 - QUIZ
        // =====================================

        LessonStep step5 =
                new LessonStep();

        step5.id = "5";

        step5.type = "QUIZ";

        step5.title = "Quiz Print";

        step5.orderIndex = 5;

        step5.status = "ACTIVE";

        step5.xp = 20;

        step5.data =
                new LessonStep.StepData();

        step5.data.question =
                "Hàm nào dùng để in dữ liệu?";

        step5.data.options =
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

        step5.data.correctValue =
                "0";

        step5.data.explanation =
                "Python dùng print()";

        steps.add(step5);

        // =====================================
        // STEP 6 - TEXT
        // =====================================

        LessonStep step6 =
                new LessonStep();

        step6.id = "6";

        step6.type = "TEXT";

        step6.title = "Biến";

        step6.orderIndex = 6;

        step6.status = "ACTIVE";

        step6.xp = 10;

        step6.data =
                new LessonStep.StepData();

        step6.data.content =
                "Biến dùng để lưu trữ dữ liệu trong chương trình.";

        steps.add(step6);

        // =====================================
        // STEP 7 - CODE
        // =====================================

        LessonStep step7 =
                new LessonStep();

        step7.id = "7";

        step7.type = "CODE";

        step7.title = "Code Variable";

        step7.orderIndex = 7;

        step7.status = "ACTIVE";

        step7.xp = 30;

        step7.data =
                new LessonStep.StepData();

        step7.data.content =
                "Hoàn thành đoạn code tạo biến name";

        step7.data.template =
                "[0] = \"Dante\"";

        step7.data.answers =
                Arrays.asList(
                        "name"
                );

        steps.add(step7);

        // =====================================
        // STEP 8 - QUIZ
        // =====================================

        LessonStep step8 =
                new LessonStep();

        step8.id = "8";

        step8.type = "QUIZ";

        step8.title = "Quiz Variable";

        step8.orderIndex = 8;

        step8.status = "ACTIVE";

        step8.xp = 20;

        step8.data =
                new LessonStep.StepData();

        step8.data.question =
                "Python có cần từ khóa var không?";

        step8.data.options =
                Arrays.asList(

                        new LessonStep.Option(
                                "0",
                                "Không"
                        ),

                        new LessonStep.Option(
                                "1",
                                "Có"
                        )
                );

        step8.data.correctValue =
                "0";

        step8.data.explanation =
                "Python không cần var";

        steps.add(step8);

        // =====================================
        // STEP 9 - TEXT
        // =====================================

        LessonStep step9 =
                new LessonStep();

        step9.id = "9";

        step9.type = "TEXT";

        step9.title = "Hoàn thành";

        step9.orderIndex = 9;

        step9.status = "ACTIVE";

        step9.xp = 15;

        step9.data =
                new LessonStep.StepData();

        step9.data.content =
                "Chúc mừng \n\nBạn đã hoàn thành bài học Python cơ bản đầu tiên.";

        steps.add(step9);

        return steps;
    }
}