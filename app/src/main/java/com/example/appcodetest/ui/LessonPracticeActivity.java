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

        // Ánh xạ view từ XML
        txtTitle = findViewById(R.id.txtTitle);
        txtGuide = findViewById(R.id.txtGuide);
        txtOutput = findViewById(R.id.txtOutput);
        edtCode = findViewById(R.id.edtCode);
        btnRun = findViewById(R.id.btnRun);
        btnNext = findViewById(R.id.btnNext);

        // 🔥 Demo tạm thời
        // Sau này nên lấy từ API Step type = CODE
        txtTitle.setText("In delivery info");
        txtGuide.setText("Viết print(\"delivery info\")");

        // Nút chạy code
        btnRun.setOnClickListener(v -> runCode());
    }

    // =========================
    // CHECK CODE
    // =========================

    private void runCode() {

        // Lấy code người dùng nhập
        String code = edtCode.getText().toString().trim();

        // 🔥 Giả lập backend check đáp án
        // Hiện tại chỉ demo local
        if (code.equals("print(\"delivery info\")")) {

            txtOutput.setText("delivery info");

            Toast.makeText(this,
                    "Đúng!",
                    Toast.LENGTH_SHORT).show();

            // sau này có thể mở btnNext ở đây
            // btnNext.setEnabled(true);

        } else {

            txtOutput.setText("Sai output");

            Toast.makeText(this,
                    "Sai rồi!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}