package com.example.democode3.features.problem.fake;

import com.example.democode3.features.problem.model.Problem;
import com.example.democode3.features.problem.model.ProblemCondition;

import java.util.ArrayList;
import java.util.List;

public class FakeProblemRepository {

    private static final List<Problem>
            problems =
            new ArrayList<>();

    static {

        // =====================================
        // PROBLEM 1
        // =====================================

        Problem p1 =
                new Problem();

        p1.id = "1";

        p1.title =
                "Check Even";

        p1.description =
                "Viết hàm kiểm tra số chẵn.";

        p1.difficulty =
                "EASY";

        p1.functionName =
                "checkEven";

        p1.hint =
                "Dùng % 2 😭🔥";

        p1.starterCode =
                "def checkEven(n):\n    pass";

        p1.expectedAnswer =
                "return n % 2 == 0";

        p1.conditions =
                new ArrayList<>();

        ProblemCondition c1 =
                new ProblemCondition();

        c1.input = "2";

        c1.expectedOutput =
                "true";

        p1.conditions.add(c1);

        problems.add(p1);

        // =====================================
        // PROBLEM 2
        // =====================================

        Problem p2 =
                new Problem();

        p2.id = "2";

        p2.title =
                "Hello Python";

        p2.description =
                "In ra Hello World.";

        p2.difficulty =
                "BEGINNER";

        p2.functionName =
                "printHello";

        p2.hint =
                "Dùng print() 😭🔥";

        p2.starterCode =
                "print()";

        p2.expectedAnswer =
                "\"Hello World\"";

        p2.conditions =
                new ArrayList<>();

        ProblemCondition c2 =
                new ProblemCondition();

        c2.input =
                "none";

        c2.expectedOutput =
                "Hello World";

        p2.conditions.add(c2);

        problems.add(p2);

        // =====================================
        // PROBLEM 3
        // =====================================

        Problem p3 =
                new Problem();

        p3.id = "3";

        p3.title =
                "Delivery Info";

        p3.description =
                "Hiển thị delivery info bằng print().";

        p3.difficulty =
                "BEGINNER";

        p3.functionName =
                "deliveryInfo";

        p3.hint =
                "print(\"delivery info\") 😭🔥";

        p3.starterCode =
                "print()";

        p3.expectedAnswer =
                "\"delivery info\"";

        p3.conditions =
                new ArrayList<>();

        ProblemCondition c3 =
                new ProblemCondition();

        c3.input =
                "none";

        c3.expectedOutput =
                "delivery info";

        p3.conditions.add(c3);

        problems.add(p3);
    }

    // =====================================
    // GET
    // =====================================

    public static List<Problem> getProblems() {

        return problems;
    }

    // =====================================
    // ADD
    // =====================================

    public static void addProblem(
            Problem p
    ) {

        problems.add(p);
    }

    // =====================================
    // DELETE
    // =====================================

    public static void deleteProblem(
            Problem p
    ) {

        problems.remove(p);
    }
}