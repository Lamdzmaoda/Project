package com.example.democode3.features.admin.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;
import com.example.democode3.core.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class CourseEditorActivity
        extends AppCompatActivity {

    public static class CourseItem {

        public String title;

        public String difficulty;

        public CourseItem(
                String title,
                String difficulty
        ) {

            this.title = title;
            this.difficulty = difficulty;
        }
    }

    RecyclerView recyclerView;

    BaseAdapter<CourseItem> adapter;

    List<CourseItem> list =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_course_editor
        );

        recyclerView =
                findViewById(
                        R.id.recyclerView
                );

        recyclerView.setLayoutManager(

                new LinearLayoutManager(this)
        );

        // DATA

        list.add(
                new CourseItem(
                        "Python",
                        "BEGINNER"
                )
        );

        list.add(
                new CourseItem(
                        "Java",
                        "EASY"
                )
        );

        list.add(
                new CourseItem(
                        "JavaScript",
                        "MEDIUM"
                )
        );

        adapter =
                new BaseAdapter<>(

                        list,

                        new BaseAdapter.Listener<CourseItem>() {

                            @Override
                            public void onClick(
                                    CourseItem item
                            ) {

                                Intent intent =
                                        new Intent(
                                                CourseEditorActivity.this,
                                                ChapterEditorActivity.class
                                        );

                                intent.putExtra(
                                        "language",
                                        item.title
                                );

                                startActivity(intent);
                            }

                            @Override
                            public void onEdit(
                                    CourseItem item
                            ) {

                                EditText edt =
                                        new EditText(
                                                CourseEditorActivity.this
                                        );

                                edt.setText(
                                        item.title
                                );

                                new AlertDialog.Builder(
                                        CourseEditorActivity.this
                                )

                                        .setTitle(
                                                "Sửa Course"
                                        )

                                        .setView(edt)

                                        .setPositiveButton(

                                                "Lưu",

                                                (d, w) -> {

                                                    item.title =
                                                            edt.getText()
                                                                    .toString();

                                                    adapter.notifyDataSetChanged();
                                                }
                                        )

                                        .setNegativeButton(
                                                "Hủy",
                                                null
                                        )

                                        .show();
                            }

                            @Override
                            public void onDelete(
                                    CourseItem item
                            ) {

                                list.remove(item);

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public String getTitle(
                                    CourseItem item
                            ) {

                                return item.title;
                            }

                            @Override
                            public String getSubTitle(
                                    CourseItem item
                            ) {

                                return "Programming Language";
                            }


                        }
                );

        recyclerView.setAdapter(adapter);
    }
}