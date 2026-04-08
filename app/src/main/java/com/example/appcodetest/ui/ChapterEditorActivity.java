package com.example.appcodetest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;

import java.util.ArrayList;
import java.util.List;

public class ChapterEditorActivity extends AppCompatActivity {

    ListView listChapter;
    Button btnAdd;

    List<String> chapters = new ArrayList<>();
    String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_editor);

        listChapter = findViewById(R.id.listChapter);
        btnAdd = findViewById(R.id.btnAddChapter);

        courseId = getIntent().getStringExtra("COURSE_ID");

        chapters.add("Chương 1");
        chapters.add("Chương 2");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chapters);

        listChapter.setAdapter(adapter);

        listChapter.setOnItemClickListener((p,v,pos,id)->{
            Intent i = new Intent(this, LessonEditorActivity.class);
            i.putExtra("CHAPTER_ID", chapters.get(pos));
            startActivity(i);
        });

        btnAdd.setOnClickListener(v ->
                Toast.makeText(this,"TODO API ADD CHAPTER",Toast.LENGTH_SHORT).show());
    }
}