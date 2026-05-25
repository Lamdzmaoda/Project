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

public class ChapterEditorActivity
        extends AppCompatActivity {

    public static class ChapterItem {

        public String title;

        public String order;

        public ChapterItem(
                String title,
                String order
        ) {

            this.title = title;
            this.order = order;
        }
    }

    RecyclerView recyclerView;

    BaseAdapter<ChapterItem> adapter;

    List<ChapterItem> list =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_chapter_editor
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
                new ChapterItem(
                        "Introduction",
                        "#1"
                )
        );

        list.add(
                new ChapterItem(
                        "Variable",
                        "#2"
                )
        );

        adapter =
                new BaseAdapter<>(

                        list,

                        new BaseAdapter.Listener<ChapterItem>() {

                            @Override
                            public void onClick(
                                    ChapterItem item
                            ) {

                                Intent intent =
                                        new Intent(
                                                ChapterEditorActivity.this,
                                                LessonEditorActivity.class
                                        );

                                intent.putExtra(
                                        "chapter",
                                        item.title
                                );

                                startActivity(intent);
                            }

                            @Override
                            public void onEdit(
                                    ChapterItem item
                            ) {

                                EditText edt =
                                        new EditText(
                                                ChapterEditorActivity.this
                                        );

                                edt.setText(
                                        item.title
                                );

                                new AlertDialog.Builder(
                                        ChapterEditorActivity.this
                                )

                                        .setTitle(
                                                "Sửa Chapter"
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
                                    ChapterItem item
                            ) {

                                list.remove(item);

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public String getTitle(
                                    ChapterItem item
                            ) {

                                return item.title;
                            }

                            @Override
                            public String getSubTitle(
                                    ChapterItem item
                            ) {

                                return "Chapter " + item.order;
                            }
                        }
                );

        recyclerView.setAdapter(adapter);
    }
}