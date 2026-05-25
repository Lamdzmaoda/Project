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

public class LessonEditorActivity
        extends AppCompatActivity {

    public static class LessonItem {

        public String title;

        public String xp;

        public LessonItem(
                String title,
                String xp
        ) {

            this.title = title;
            this.xp = xp;
        }
    }

    RecyclerView recyclerView;

    BaseAdapter<LessonItem> adapter;

    List<LessonItem> list =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_lesson_editor
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
                new LessonItem(
                        "Python Print",
                        "50 XP"
                )
        );

        list.add(
                new LessonItem(
                        "Python Variable",
                        "80 XP"
                )
        );

        adapter =
                new BaseAdapter<>(

                        list,

                        new BaseAdapter.Listener<LessonItem>() {

                            @Override
                            public void onClick(
                                    LessonItem item
                            ) {

                                Intent intent =
                                        new Intent(
                                                LessonEditorActivity.this,
                                                StepListActivity.class
                                        );

                                intent.putExtra(
                                        "lesson",
                                        item.title
                                );

                                startActivity(intent);
                            }

                            @Override
                            public void onEdit(
                                    LessonItem item
                            ) {

                                EditText edt =
                                        new EditText(
                                                LessonEditorActivity.this
                                        );

                                edt.setText(
                                        item.title
                                );

                                new AlertDialog.Builder(
                                        LessonEditorActivity.this
                                )

                                        .setTitle(
                                                "Sửa Lesson"
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
                                    LessonItem item
                            ) {

                                list.remove(item);

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public String getTitle(
                                    LessonItem item
                            ) {

                                return item.title;
                            }

                            @Override
                            public String getSubTitle(
                                    LessonItem item
                            ) {

                                return "Lesson";
                            }


                        }
                );

        recyclerView.setAdapter(adapter);
    }
}