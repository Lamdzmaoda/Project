package com.example.appcodetest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;

public class AdminActivity extends AppCompatActivity {

    LinearLayout lesson1, lesson2, lesson3;
    LinearLayout btnHome, btnProfile;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // 🔥 MATCH XML 100%
        lesson1 = findViewById(R.id.lesson1);
        lesson2 = findViewById(R.id.lesson2);
        lesson3 = findViewById(R.id.lesson3);

        btnHome = findViewById(R.id.btnHome);
        btnProfile = findViewById(R.id.btnProfile);
        btnAdd = findViewById(R.id.btnAdd);

        // HOME
        btnHome.setOnClickListener(v ->
                Toast.makeText(this, "Admin Home", Toast.LENGTH_SHORT).show()
        );

        // PROFILE
        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class))
        );

        // 🔥 ADD NEW LESSON
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, LessonEditorActivity.class);
            startActivity(intent);
        });

// 🔥 EDIT LESSON 1
        lesson1.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, LessonEditorActivity.class);
            intent.putExtra("LESSON_ID", "lesson_1");
            startActivity(intent);
        });

// 🔥 EDIT LESSON 2
        lesson2.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, LessonEditorActivity.class);
            intent.putExtra("LESSON_ID", "lesson_2");
            startActivity(intent);
        });

// 🔥 EDIT LESSON 3
        lesson3.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, LessonEditorActivity.class);
            intent.putExtra("LESSON_ID", "lesson_3");
            startActivity(intent);
        });
    }
}