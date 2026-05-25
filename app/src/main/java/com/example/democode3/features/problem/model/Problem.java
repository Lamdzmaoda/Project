package com.example.democode3.features.problem.model;

import java.util.List;

public class Problem {

    public String id;

    public String title;

    public String description;

    public String difficulty;

    public String starterCode;

    public String expectedAnswer;

    public String functionName;

    public String hint;

    public List<ProblemCondition> conditions;
}