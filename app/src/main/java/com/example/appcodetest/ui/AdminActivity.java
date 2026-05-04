package com.example.appcodetest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;

public class AdminActivity extends AppCompatActivity {

    LinearLayout btnLanguage, btnProfile, btnHome, btnPreview, btnUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnLanguage = findViewById(R.id.btnLanguage);
        btnProfile = findViewById(R.id.btnProfile);
        btnHome = findViewById(R.id.btnHome);
        btnPreview = findViewById(R.id.btnPreview);
        btnUsers = findViewById(R.id.btnUsers); // 🔥 thêm

        btnLanguage.setOnClickListener(v ->
                startActivity(new Intent(this, CourseEditorActivity.class))
        );

        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class))
        );

        btnHome.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class))
        );

        btnPreview.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class))
        );

        // 🔥 MỞ USER MANAGER
        btnUsers.setOnClickListener(v ->
                startActivity(new Intent(this, UserManagerActivity.class))
        );
    }
}