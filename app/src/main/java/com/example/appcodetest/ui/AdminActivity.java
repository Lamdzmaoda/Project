package com.example.appcodetest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;

public class AdminActivity extends AppCompatActivity {

    Button btnLanguage, btnProfile, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnLanguage = findViewById(R.id.btnLanguage);
        btnProfile = findViewById(R.id.btnProfile);
        btnHome = findViewById(R.id.btnHome);

        // 📚 MỞ LANGUAGE (CourseEditor)
        btnLanguage.setOnClickListener(v ->
                startActivity(new Intent(this, CourseEditorActivity.class))
        );

        // 👤 PROFILE
        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class))
        );

        // 🏠 HOME
        btnHome.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class))
        );
    }
}