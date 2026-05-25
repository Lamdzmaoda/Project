package com.example.democode3.features.admin.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.democode3.R;

public class ProblemEditorActivity
        extends AppCompatActivity {

    EditText edtTitle;

    EditText edtDescription;

    EditText edtDifficulty;

    EditText edtFunction;

    EditText edtHint;

    EditText edtStarterCode;

    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_problem_editor
        );

        edtTitle =
                findViewById(R.id.edtTitle);

        edtDescription =
                findViewById(R.id.edtDescription);

        edtDifficulty =
                findViewById(R.id.edtDifficulty);

        edtFunction =
                findViewById(R.id.edtFunction);

        edtHint =
                findViewById(R.id.edtHint);

        edtStarterCode =
                findViewById(R.id.edtStarterCode);

        btnSave =
                findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {

            Toast.makeText(

                    this,

                    "SAVE PROBLEM 😭🔥",

                    Toast.LENGTH_SHORT

            ).show();
        });
    }
}