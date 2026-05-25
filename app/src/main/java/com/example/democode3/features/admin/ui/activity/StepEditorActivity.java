package com.example.democode3.features.admin.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.democode3.R;
import com.example.democode3.features.admin.fake.FakeStepRepository;
import com.example.democode3.features.learning.model.LessonStep;

import java.util.ArrayList;

public class StepEditorActivity
        extends AppCompatActivity {

    EditText edtTitle;

    Spinner spinnerType;

    LinearLayout layoutContent;

    LinearLayout layoutQuiz;

    LinearLayout layoutCode;

    EditText edtContent;

    EditText edtQuestion;

    EditText edtCorrectValue;

    EditText edtExplanation;

    EditText edtTemplate;

    LinearLayout layoutOptions;

    LinearLayout layoutAnswers;

    Button btnAddOption;

    Button btnAddAnswer;

    Button btnSave;

    String stepId;

    String[] types = {

            "TEXT",

            "QUIZ",

            "CODE"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_step_editor
        );

        initViews();

        setupSpinner();

        loadEditData();

        btnAddOption.setOnClickListener(v -> {

            addOption("");
        });

        btnAddAnswer.setOnClickListener(v -> {

            addAnswer("");
        });

        btnSave.setOnClickListener(v -> {

            saveStep();
        });
    }

    private void initViews() {

        edtTitle =
                findViewById(R.id.edtTitle);

        spinnerType =
                findViewById(R.id.spinnerType);

        layoutContent =
                findViewById(R.id.layoutContent);

        layoutQuiz =
                findViewById(R.id.layoutQuiz);

        layoutCode =
                findViewById(R.id.layoutCode);

        edtContent =
                findViewById(R.id.edtContent);

        edtQuestion =
                findViewById(R.id.edtQuestion);

        edtCorrectValue =
                findViewById(R.id.edtCorrectValue);

        edtExplanation =
                findViewById(R.id.edtExplanation);

        edtTemplate =
                findViewById(R.id.edtTemplate);

        layoutOptions =
                findViewById(R.id.layoutOptions);

        layoutAnswers =
                findViewById(R.id.layoutAnswers);

        btnAddOption =
                findViewById(R.id.btnAddOption);

        btnAddAnswer =
                findViewById(R.id.btnAddAnswer);

        btnSave =
                findViewById(R.id.btnSave);
    }

    private void setupSpinner() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(

                        this,

                        android.R.layout.simple_spinner_dropdown_item,

                        types
                );

        spinnerType.setAdapter(adapter);

        spinnerType.setOnItemSelectedListener(

                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(

                            android.widget.AdapterView<?> parent,

                            View view,

                            int position,

                            long id
                    ) {

                        updateUI(
                                types[position]
                        );
                    }

                    @Override
                    public void onNothingSelected(

                            android.widget.AdapterView<?> parent
                    ) {
                    }
                }
        );
    }

    private void loadEditData() {

        stepId =
                getIntent()
                        .getStringExtra(
                                "STEP_ID"
                        );

        edtTitle.setText(

                getIntent()
                        .getStringExtra(
                                "TITLE"
                        )
        );

        edtContent.setText(

                getIntent()
                        .getStringExtra(
                                "CONTENT"
                        )
        );

        edtQuestion.setText(

                getIntent()
                        .getStringExtra(
                                "QUESTION"
                        )
        );

        edtCorrectValue.setText(

                getIntent()
                        .getStringExtra(
                                "CORRECT"
                        )
        );

        edtExplanation.setText(

                getIntent()
                        .getStringExtra(
                                "EXPLANATION"
                        )
        );

        edtTemplate.setText(

                getIntent()
                        .getStringExtra(
                                "TEMPLATE"
                        )
        );

        String type =
                getIntent()
                        .getStringExtra(
                                "TYPE"
                        );

        if (type != null) {

            for (
                    int i = 0;
                    i < types.length;
                    i++
            ) {

                if (types[i].equals(type)) {

                    spinnerType.setSelection(i);

                    break;
                }
            }
        }
    }

    private void updateUI(
            String type
    ) {

        layoutContent.setVisibility(
                View.GONE
        );

        layoutQuiz.setVisibility(
                View.GONE
        );

        layoutCode.setVisibility(
                View.GONE
        );

        if (type.equals("TEXT")) {

            layoutContent.setVisibility(
                    View.VISIBLE
            );

        } else if (type.equals("QUIZ")) {

            layoutQuiz.setVisibility(
                    View.VISIBLE
            );

        } else {

            layoutCode.setVisibility(
                    View.VISIBLE
            );
        }
    }

    private void addOption(
            String value
    ) {

        EditText edt =
                new EditText(this);

        edt.setText(value);

        edt.setHint("Option");

        edt.setBackgroundColor(
                0xFFFFFFFF
        );

        layoutOptions.addView(edt);
    }

    private void addAnswer(
            String value
    ) {

        EditText edt =
                new EditText(this);

        edt.setText(value);

        edt.setHint("Answer");

        edt.setBackgroundColor(
                0xFFFFFFFF
        );

        layoutAnswers.addView(edt);
    }

    private void saveStep() {

        String title =
                edtTitle.getText()
                        .toString();

        String type =
                spinnerType.getSelectedItem()
                        .toString();

        LessonStep step =
                new LessonStep();

        step.id =
                stepId == null
                        ? String.valueOf(
                        System.currentTimeMillis()
                )
                        : stepId;

        step.title =
                title;

        step.type =
                type;

        step.orderIndex =
                FakeStepRepository
                        .getSteps()
                        .size() + 1;

        step.status =
                "ACTIVE";

        step.xp = 10;

        step.data =
                new LessonStep.StepData();

        // TEXT

        if (type.equals("TEXT")) {

            step.data.content =
                    edtContent
                            .getText()
                            .toString();
        }

        // QUIZ

        else if (type.equals("QUIZ")) {

            step.data.question =
                    edtQuestion
                            .getText()
                            .toString();

            step.data.correctValue =
                    edtCorrectValue
                            .getText()
                            .toString();

            step.data.explanation =
                    edtExplanation
                            .getText()
                            .toString();
        }

        // CODE

        else {

            step.data.template =
                    edtTemplate
                            .getText()
                            .toString();

            step.data.answers =
                    new ArrayList<>();

            step.data.answers.add(
                    "\"Hello\""
            );
        }

        // UPDATE

        if (stepId != null) {

            for (LessonStep old
                    : FakeStepRepository
                    .getSteps()) {

                if (
                        old.id.equals(stepId)
                ) {

                    old.title =
                            step.title;

                    old.type =
                            step.type;

                    old.data =
                            step.data;
                }
            }

        } else {

            FakeStepRepository
                    .addStep(step);
        }

        Toast.makeText(

                this,

                "Đã lưu 😭🔥",

                Toast.LENGTH_SHORT

        ).show();

        finish();
    }
}