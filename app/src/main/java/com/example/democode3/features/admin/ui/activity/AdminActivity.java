package com.example.democode3.features.admin.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.democode3.MainActivity;
import com.example.democode3.R;
import com.example.democode3.features.admin.ui.activity.UserManagerActivity;

public class AdminActivity
        extends AppCompatActivity {

    // =====================================
    // VIEW
    // =====================================

    LinearLayout btnLanguage;

    LinearLayout btnUsers;

    LinearLayout btnPreview;

    // =====================================
    // CREATE
    // =====================================

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.fragment_dashboard
        );

        // =================================
        // INIT
        // =================================

        btnLanguage =
                findViewById(
                        R.id.btnLanguage
                );

        btnUsers =
                findViewById(
                        R.id.btnUsers
                );

        btnPreview =
                findViewById(
                        R.id.btnPreview
                );

        // =================================
        // COURSE
        // =================================

        btnLanguage.setOnClickListener(v -> {

            startActivity(

                    new Intent(

                            this,

                            CourseEditorActivity.class
                    )
            );
        });

        // =================================
        // USERS
        // =================================

        btnUsers.setOnClickListener(v -> {

            startActivity(

                    new Intent(

                            this,

                            UserManagerActivity.class
                    )
            );
        });

        // =================================
        // PREVIEW
        // =================================

        btnPreview.setOnClickListener(v -> {

            startActivity(

                    new Intent(

                            this,

                            MainActivity.class
                    )
            );
        });
    }
}