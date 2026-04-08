package com.example.appcodetest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;

import java.util.ArrayList;
import java.util.List;

public class CourseEditorActivity extends AppCompatActivity {

    ListView listCourse;
    Button btnAdd;

    List<String> courses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);

        listCourse = findViewById(R.id.listCourse);
        btnAdd = findViewById(R.id.btnAddCourse);

        courses.add("Python");
        courses.add("Java");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);

        listCourse.setAdapter(adapter);

        listCourse.setOnItemClickListener((p,v,pos,id)->{
            Intent i = new Intent(this, ChapterEditorActivity.class);
            i.putExtra("COURSE_ID", courses.get(pos));
            startActivity(i);
        });

        btnAdd.setOnClickListener(v ->
                Toast.makeText(this,"TODO API ADD COURSE",Toast.LENGTH_SHORT).show());
    }
}