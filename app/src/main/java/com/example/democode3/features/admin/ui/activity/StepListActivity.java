package com.example.democode3.features.admin.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;
import com.example.democode3.core.base.BaseAdapter;
import com.example.democode3.features.admin.fake.FakeStepRepository;
import com.example.democode3.features.learning.model.LessonStep;

import java.util.List;

public class StepListActivity
        extends AppCompatActivity {

    RecyclerView recyclerView;

    BaseAdapter<LessonStep> adapter;

    List<LessonStep> steps;

    String lessonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_step_list
        );

        recyclerView =
                findViewById(
                        R.id.recyclerStep
                );

        lessonId =
                getIntent()
                        .getStringExtra(
                                "LESSON_ID"
                        );

        recyclerView.setLayoutManager(

                new LinearLayoutManager(this)
        );

        steps =
                FakeStepRepository.getSteps();

        adapter =
                new BaseAdapter<>(
                        steps,
                        new BaseAdapter.Listener<LessonStep>() {

                            @Override
                            public void onClick(
                                    LessonStep item
                            ) {

                                openEditor(item);
                            }

                            @Override
                            public void onEdit(
                                    LessonStep item
                            ) {

                                openEditor(item);
                            }

                            @Override
                            public void onDelete(
                                    LessonStep item
                            ) {

                                FakeStepRepository
                                        .deleteStep(item);

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public String getTitle(
                                    LessonStep item
                            ) {

                                return item.title;
                            }

                            @Override
                            public String getSubTitle(
                                    LessonStep item
                            ) {

                                return item.type;
                            }


                        }
                );

        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnAddStep)
                .setOnClickListener(v -> {

                    openEditor(null);
                });
    }

    @Override
    protected void onResume() {

        super.onResume();

        adapter.notifyDataSetChanged();
    }

    private void openEditor(
            LessonStep step
    ) {

        Intent i =
                new Intent(
                        this,
                        StepEditorActivity.class
                );

        i.putExtra(
                "LESSON_ID",
                lessonId
        );

        if (step != null) {

            i.putExtra(
                    "STEP_ID",
                    step.id
            );

            i.putExtra(
                    "TITLE",
                    step.title
            );

            i.putExtra(
                    "TYPE",
                    step.type
            );

            if (step.data != null) {

                i.putExtra(
                        "CONTENT",
                        step.data.content
                );

                i.putExtra(
                        "QUESTION",
                        step.data.question
                );

                i.putExtra(
                        "CORRECT",
                        step.data.correctValue
                );

                i.putExtra(
                        "EXPLANATION",
                        step.data.explanation
                );

                i.putExtra(
                        "TEMPLATE",
                        step.data.template
                );
            }
        }

        startActivity(i);
    }
}