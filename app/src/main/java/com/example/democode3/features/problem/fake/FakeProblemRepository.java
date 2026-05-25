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

        Problem p =
                new Problem();

        p.id = "1";

        p.title =
                "Check Even";

        p.description =
                "Viết hàm kiểm tra số chẵn";

        p.difficulty =
                "EASY";

        p.functionName =
                "checkEven";

        p.hint =
                "Dùng % 2 😭🔥";

        p.starterCode =
                "def checkEven(n):\n    pass";

        p.expectedAnswer =
                "return n % 2 == 0";

        p.conditions =
                new ArrayList<>();

        ProblemCondition c =
                new ProblemCondition();

        c.input = "2";

        c.expectedOutput = "true";

        p.conditions.add(c);

        problems.add(p);
    }

    public static List<Problem> getProblems() {

        return problems;
    }

    public static void addProblem(
            Problem p
    ) {

        problems.add(p);
    }

    public static void deleteProblem(
            Problem p
    ) {

        problems.remove(p);
    }
}