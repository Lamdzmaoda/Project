package com.example.appcodetest.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;
import com.example.appcodetest.api.RetrofitClient;
import com.example.appcodetest.api.ApiService;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.*;

public class StepEditorActivity extends AppCompatActivity {

    Spinner spinnerType;
    EditText edtContent, edtOption1, edtOption2, edtOption3;
    RadioGroup radioCorrect;
    Button btnSave;

    String lessonId;
    int currentOrder = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_editor);

        spinnerType = findViewById(R.id.spinnerType);
        edtContent = findViewById(R.id.edtContent);
        edtOption1 = findViewById(R.id.edtOption1);
        edtOption2 = findViewById(R.id.edtOption2);
        edtOption3 = findViewById(R.id.edtOption3);
        radioCorrect = findViewById(R.id.radioCorrect);
        btnSave = findViewById(R.id.btnSave);

        lessonId = getIntent().getStringExtra("LESSON_ID");

        setupSpinner();
        handleUI();

        btnSave.setOnClickListener(v -> createStep());
    }

    // =========================
    // 🎨 Spinner (FIX TYPE BACKEND)
    // =========================
    private void setupSpinner() {
        String[] types = {
                "INFO",
                "QUIZ",
                "CODE",
                "FILL_CODE"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                types
        );

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerType.setAdapter(adapter);
    }

    // =========================
    // 👀 Ẩn/hiện input theo type
    // =========================
    private void handleUI() {
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                String type = spinnerType.getSelectedItem().toString();
                boolean isQuiz = type.equals("QUIZ");

                edtOption1.setVisibility(isQuiz ? View.VISIBLE : View.GONE);
                edtOption2.setVisibility(isQuiz ? View.VISIBLE : View.GONE);
                edtOption3.setVisibility(isQuiz ? View.VISIBLE : View.GONE);
                radioCorrect.setVisibility(isQuiz ? View.VISIBLE : View.GONE);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // =========================
    // 🚀 CREATE STEP (FIX CHUẨN BACKEND)
    // =========================
    private void createStep() {

        String content = edtContent.getText().toString().trim();

        if (content.isEmpty()) {
            edtContent.setError("Nhập nội dung!");
            return;
        }

        try {

            btnSave.setEnabled(false);

            JSONObject json = new JSONObject();

            String type = spinnerType.getSelectedItem().toString();

            json.put("lessonId", lessonId);
            json.put("title", "Step " + currentOrder);
            json.put("type", type); // 🔥 FIX
            json.put("mode", "LEARN");
            json.put("orderIndex", currentOrder);
            json.put("xp", 30.0);

            JSONObject data = new JSONObject();

            // =========================
            // QUIZ
            // =========================
            if (type.equals("QUIZ")) {

                String op1 = edtOption1.getText().toString().trim();
                String op2 = edtOption2.getText().toString().trim();
                String op3 = edtOption3.getText().toString().trim();

                if (op1.isEmpty() || op2.isEmpty() || op3.isEmpty()) {
                    Toast.makeText(this, "Nhập đủ đáp án!", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                    return;
                }

                JSONArray options = new JSONArray();
                options.put(op1);
                options.put(op2);
                options.put(op3);

                int checkedId = radioCorrect.getCheckedRadioButtonId();

                if (checkedId == -1) {
                    Toast.makeText(this, "Chọn đáp án đúng!", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                    return;
                }

                int correct = 0;
                if (checkedId == R.id.radio1) correct = 0;
                else if (checkedId == R.id.radio2) correct = 1;
                else if (checkedId == R.id.radio3) correct = 2;

                data.put("question", content);
                data.put("options", options);
                data.put("correctValue", correct);
                data.put("explanation", "Giải thích cho câu hỏi");

            }

            // =========================
            // INFO / CODE / FILL_CODE
            // =========================
            else {
                data.put("question", content);
            }

            json.put("data", data);

            callApi(json);

        } catch (Exception e) {
            e.printStackTrace();
            btnSave.setEnabled(true);
        }
    }

    // =========================
    // 🌐 CALL API
    // =========================
    private void callApi(JSONObject json) {

        String token = getSharedPreferences("APP", MODE_PRIVATE)
                .getString("TOKEN", "");

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                json.toString()
        );

        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.createStep("Bearer " + token, body)
                .enqueue(new Callback<Object>() {

                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        btnSave.setEnabled(true);

                        if (response.isSuccessful()) {

                            Toast.makeText(StepEditorActivity.this,
                                    "Tạo step OK 🚀",
                                    Toast.LENGTH_SHORT).show();

                            currentOrder++;

                            resetForm();

                        } else {
                            Toast.makeText(StepEditorActivity.this,
                                    "API lỗi: " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                        btnSave.setEnabled(true);

                        Toast.makeText(StepEditorActivity.this,
                                "Lỗi mạng!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // =========================
    // 🔄 RESET FORM
    // =========================
    private void resetForm() {
        edtContent.setText("");
        edtOption1.setText("");
        edtOption2.setText("");
        edtOption3.setText("");
        radioCorrect.clearCheck();
    }
}