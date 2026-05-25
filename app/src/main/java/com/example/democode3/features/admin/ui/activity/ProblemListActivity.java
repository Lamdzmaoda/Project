package com.example.democode3.features.admin.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;
import com.example.democode3.core.base.BaseAdapter;
import com.example.democode3.features.problem.fake.FakeProblemRepository;
import com.example.democode3.features.problem.model.Problem;

import java.util.List;

public class ProblemListActivity
        extends AppCompatActivity {

    RecyclerView recyclerView;

    BaseAdapter<Problem> adapter;

    List<Problem> problems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_problem_list
        );

        recyclerView =
                findViewById(
                        R.id.recyclerProblem
                );

        recyclerView.setLayoutManager(

                new LinearLayoutManager(this)
        );

        problems =
                FakeProblemRepository
                        .getProblems();

        adapter =
                new BaseAdapter<>(
                        problems,
                        new BaseAdapter.Listener<Problem>() {

                            @Override
                            public void onClick(
                                    Problem item
                            ) {

                                openEditor(item);
                            }

                            @Override
                            public void onEdit(
                                    Problem item
                            ) {

                                openEditor(item);
                            }

                            @Override
                            public void onDelete(
                                    Problem item
                            ) {

                                FakeProblemRepository
                                        .deleteProblem(item);

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public String getTitle(
                                    Problem item
                            ) {

                                return item.title;
                            }

                            @Override
                            public String getSubTitle(
                                    Problem item
                            ) {

                                return item.description;
                            }


                        }
                );

        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnAddProblem)
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
            Problem p
    ) {

        Intent i =
                new Intent(
                        this,
                        ProblemEditorActivity.class
                );

        if (p != null) {

            i.putExtra(
                    "ID",
                    p.id
            );

            i.putExtra(
                    "TITLE",
                    p.title
            );

            i.putExtra(
                    "DESCRIPTION",
                    p.description
            );

            i.putExtra(
                    "DIFFICULTY",
                    p.difficulty
            );

            i.putExtra(
                    "FUNCTION",
                    p.functionName
            );

            i.putExtra(
                    "HINT",
                    p.hint
            );

            i.putExtra(
                    "CODE",
                    p.starterCode
            );
        }

        startActivity(i);
    }
}