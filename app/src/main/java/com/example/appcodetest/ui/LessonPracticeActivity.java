package com.example.appcodetest.ui;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;

public class LessonPracticeActivity extends AppCompatActivity {

    TextView txtTitle, txtGuide, txtOutput;
    EditText edtCode;
    Button btnRun, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_practice);

        txtTitle = findViewById(R.id.txtTitle);
        txtGuide = findViewById(R.id.txtGuide);
        txtOutput = findViewById(R.id.txtOutput);
        edtCode = findViewById(R.id.edtCode);
        btnRun = findViewById(R.id.btnRun);
        btnNext = findViewById(R.id.btnNext);

        // Demo data (sau này lấy từ API)
        txtTitle.setText("In delivery info");
        txtGuide.setText("Viết print(\"delivery info\")");

        btnRun.setOnClickListener(v -> runCode());
    }

    private void runCode() {

        String code = edtCode.getText().toString().trim();

        // giả lập backend check
        if (code.equals("print(\"delivery info\")")) {
            txtOutput.setText("delivery info");
            Toast.makeText(this, "Đúng!", Toast.LENGTH_SHORT).show();
        } else {
            txtOutput.setText("Sai output");
            Toast.makeText(this, "Sai rồi!", Toast.LENGTH_SHORT).show();
        }
    }
}