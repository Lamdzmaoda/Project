package com.example.appcodetest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;

public class MainActivity extends AppCompatActivity {

    LinearLayout btnHome, btnProfile, lesson1, lesson2, lesson3;
    TextView iconHome, textHome, iconProfile, textProfile;

    // 🔥 FIX: dùng đúng ID giống FakeData
    private final String LESSON_1_ID = "lesson_1";
    private final String LESSON_2_ID = "lesson_2";
    private final String LESSON_3_ID = "lesson_2"; // tạm dùng lại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHome = findViewById(R.id.btnHome);
        btnProfile = findViewById(R.id.btnProfile);

        iconHome = findViewById(R.id.iconHome);
        textHome = findViewById(R.id.textHome);
        iconProfile = findViewById(R.id.iconProfile);
        textProfile = findViewById(R.id.textProfile);

        lesson1 = findViewById(R.id.lesson1);
        lesson2 = findViewById(R.id.lesson2);
        lesson3 = findViewById(R.id.lesson3);

        lesson1.setOnClickListener(v -> openLesson(LESSON_1_ID, "LEARN"));
        lesson2.setOnClickListener(v -> openLesson(LESSON_2_ID, "LEARN"));
        lesson3.setOnClickListener(v -> openLesson(LESSON_3_ID, "PRACTICE"));

        setActiveTab(true);

        btnHome.setOnClickListener(v -> {
            setActiveTab(true);
            Toast.makeText(this, "Bạn đang ở Home", Toast.LENGTH_SHORT).show();
        });

        btnProfile.setOnClickListener(v -> {
            setActiveTab(false);
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });
    }

    private void openLesson(String lessonId, String mode) {

        Intent intent;

        if ("LEARN".equals(mode)) {
            intent = new Intent(this, LessonLearnActivity.class);
        } else {
            intent = new Intent(this, LessonPracticeActivity.class);
        }

        intent.putExtra("LESSON_ID", lessonId);
        startActivity(intent);
    }

    private void setActiveTab(boolean isHome) {
        if (isHome) {
            iconHome.setTextColor(0xFFFFD700);
            textHome.setTextColor(0xFFFFD700);

            iconProfile.setTextColor(0xFF888888);
            textProfile.setTextColor(0xFF888888);
        } else {
            iconHome.setTextColor(0xFF888888);
            textHome.setTextColor(0xFF888888);

            iconProfile.setTextColor(0xFFFFD700);
            textProfile.setTextColor(0xFFFFD700);
        }
    }
}