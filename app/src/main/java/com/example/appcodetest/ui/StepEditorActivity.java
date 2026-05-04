package com.example.appcodetest.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcodetest.R;
import com.example.appcodetest.api.*;
import com.example.appcodetest.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.*;

public class StepEditorActivity extends AppCompatActivity {

    Spinner spinnerType;
    EditText edtContent;

    RadioGroup radioCorrect;
    Button btnSave, btnAddOption;

    LinearLayout layoutOptions;

    // =========================
    // FILL CODE
    // =========================

    LinearLayout layoutFillCode, layoutFillInputs;

    Button btnAddFill;
    EditText edtTemplate;

    // QUIZ answer inputs
    List<EditText> optionInputs = new ArrayList<>();

    // FILL answer inputs
    List<EditText> fillInputs = new ArrayList<>();
    List<TextView> fillIndexes = new ArrayList<>();

    String lessonId;
    String token;

    // thứ tự step trong lesson
    int currentOrder = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_editor);

        // =========================
        // ÁNH XẠ VIEW
        // =========================

        spinnerType = findViewById(R.id.spinnerType);
        edtContent = findViewById(R.id.edtContent);

        radioCorrect = findViewById(R.id.radioCorrect);

        btnSave = findViewById(R.id.btnSave);
        btnAddOption = findViewById(R.id.btnAddOption);

        layoutOptions = findViewById(R.id.layoutOptions);

        layoutFillCode = findViewById(R.id.layoutFillCode);
        layoutFillInputs = findViewById(R.id.layoutFillInputs);

        btnAddFill = findViewById(R.id.btnAddFill);
        edtTemplate = findViewById(R.id.edtTemplate);

        // lấy lesson id từ màn trước
        lessonId = getIntent().getStringExtra("LESSON_ID");

        Log.d("LESSON_ID_DEBUG", "lessonId = " + lessonId);

        if (lessonId == null || lessonId.trim().isEmpty()) {
            Toast.makeText(this,
                    "Không tìm thấy Lesson ID!",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // =========================
        // CHECK TOKEN
        // =========================

        String raw = Prefs.getToken(this);

        if (raw == null || raw.isEmpty()) {

            Toast.makeText(this,
                    "Chưa đăng nhập!",
                    Toast.LENGTH_SHORT).show();

            finish();
            return;
        }

        token = "Bearer " + raw;

        // setup dữ liệu ban đầu
        setupSpinner();
        handleUI();

        // mặc định tạo sẵn 3 đáp án quiz
        addOptionField("");
        addOptionField("");
        addOptionField("");

        btnAddOption.setOnClickListener(v ->
                addOptionField("")
        );

        btnAddFill.setOnClickListener(v ->
                addFillInput("")
        );

        btnSave.setOnClickListener(v ->
                saveStep()
        );
    }

    // =========================
    // SETUP TYPE
    // =========================

    private void setupSpinner() {

        String[] types = {
                "INFO",
                "CODE",
                "QUESTION",
                "QUIZ",
                "FILL_INLINE",
                "FILL_CHOICE"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        types
                );

        spinnerType.setAdapter(adapter);
    }

    // =========================
    // ẨN / HIỆN UI
    // =========================

    private void handleUI() {

        spinnerType.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int pos,
                            long id
                    ) {

                        String type =
                                spinnerType.getSelectedItem()
                                        .toString();

                        boolean isQuiz =
                                type.equals("QUIZ");

                        boolean isFill =
                                type.equals("FILL_INLINE")
                                        || type.equals("FILL_CHOICE");

                        layoutOptions.setVisibility(
                                isQuiz ? View.VISIBLE : View.GONE
                        );

                        btnAddOption.setVisibility(
                                isQuiz ? View.VISIBLE : View.GONE
                        );

                        radioCorrect.setVisibility(
                                isQuiz ? View.VISIBLE : View.GONE
                        );

                        layoutFillCode.setVisibility(
                                isFill ? View.VISIBLE : View.GONE
                        );
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {
                    }
                });
    }

    // =========================
    // QUIZ
    // =========================

    private void addOptionField(String text) {

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);

        EditText edt = new EditText(this);
        edt.setHint("Đáp án " + (optionInputs.size() + 1));
        edt.setText(text);

        edt.setLayoutParams(
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        Button btnRemove = new Button(this);
        btnRemove.setText("❌");

        btnRemove.setOnClickListener(v -> {

            layoutOptions.removeView(row);
            optionInputs.remove(edt);

            refreshRadio();
        });

        row.addView(edt);
        row.addView(btnRemove);

        layoutOptions.addView(row);
        optionInputs.add(edt);

        refreshRadio();
    }

    private void refreshRadio() {

        radioCorrect.removeAllViews();

        for (int i = 0; i < optionInputs.size(); i++) {

            RadioButton rb = new RadioButton(this);
            rb.setText("Đáp án đúng: " + (i + 1));

            radioCorrect.addView(rb);
        }
    }

    // =========================
    // FILL
    // =========================

    private void addFillInput(String text) {

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);

        int index = fillInputs.size();

        TextView txtIndex = new TextView(this);
        txtIndex.setText(String.valueOf(index));
        txtIndex.setPadding(10, 10, 20, 10);

        EditText edt = new EditText(this);
        edt.setHint("Value");
        edt.setText(text);

        edt.setLayoutParams(
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        Button btnRemove = new Button(this);
        btnRemove.setText("❌");

        btnRemove.setOnClickListener(v -> {

            layoutFillInputs.removeView(row);

            fillInputs.remove(edt);
            fillIndexes.remove(txtIndex);

            refreshFillIndex();
        });

        row.addView(txtIndex);
        row.addView(edt);
        row.addView(btnRemove);

        layoutFillInputs.addView(row);

        fillInputs.add(edt);
        fillIndexes.add(txtIndex);
    }

    private void refreshFillIndex() {

        for (int i = 0; i < fillIndexes.size(); i++) {
            fillIndexes.get(i)
                    .setText(String.valueOf(i));
        }
    }

    // =========================
    // SAVE STEP
    // =========================

    private void saveStep() {

        try {

            JSONObject json = new JSONObject();

            String type =
                    spinnerType.getSelectedItem()
                            .toString();

            String content =
                    edtContent.getText()
                            .toString()
                            .trim();

            String templateRaw =
                    edtTemplate.getText()
                            .toString()
                            .trim();

            if (content.isEmpty()) {
                edtContent.setError("Nhập nội dung!");
                return;
            }

            // =========================
            // FIELD CHÍNH
            // =========================

            json.put("lessonId", lessonId);
            json.put("title", "Step " + currentOrder);

            json.put("type", type);
            json.put("mode", "LEARN");

            json.put("orderIndex", currentOrder);
            json.put("xp", 30);

            json.put("lockedStatus", "FALSE_LOCKED");
            json.put("completedStatus", "FALSE");

            json.put("requiredToUnlockNext", false);

            JSONObject data = new JSONObject();

            // =========================
            // QUIZ
            // =========================

            if (type.equals("QUIZ")) {

                JSONArray options = new JSONArray();

                for (EditText edt : optionInputs) {

                    String value =
                            edt.getText()
                                    .toString()
                                    .trim();

                    if (!value.isEmpty()) {
                        options.put(value);
                    }
                }

                if (options.length() < 2) {
                    Toast.makeText(this,
                            "Quiz cần ít nhất 2 đáp án!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int checkedId =
                        radioCorrect.getCheckedRadioButtonId();

                if (checkedId == -1) {

                    Toast.makeText(this,
                            "Chọn đáp án đúng!",
                            Toast.LENGTH_SHORT).show();

                    return;
                }

                int index =
                        radioCorrect.indexOfChild(
                                findViewById(checkedId)
                        );

                data.put("question", content);
                data.put("options", options);
                data.put("correctValue", index);
                data.put("explanation", "");
            }

            // =========================
            // FILL_INLINE / FILL_CHOICE
            // =========================

            else if (type.equals("FILL_INLINE")
                    || type.equals("FILL_CHOICE")) {

                if (templateRaw.isEmpty()) {
                    Toast.makeText(this,
                            "Nhập template trước!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONArray options = new JSONArray();
                JSONArray answers = new JSONArray();

                Set<Integer> usedIndexes =
                        new HashSet<>();

                Pattern pattern =
                        Pattern.compile("\\[(\\d+)\\]");

                Matcher matcher =
                        pattern.matcher(templateRaw);

                while (matcher.find()) {
                    usedIndexes.add(
                            Integer.parseInt(
                                    matcher.group(1)
                            )
                    );
                }

                for (int i = 0; i < fillInputs.size(); i++) {

                    String value =
                            fillInputs.get(i)
                                    .getText()
                                    .toString()
                                    .trim();

                    if (value.isEmpty()) continue;

                    options.put(value);

                    if (usedIndexes.contains(i)) {
                        answers.put(i);
                    }
                }

                if (answers.length() == 0) {

                    Toast.makeText(this,
                            "Template chưa có đáp án đúng!",
                            Toast.LENGTH_SHORT).show();

                    return;
                }

                data.put("template", templateRaw);
                data.put("options", options);
                data.put("answers", answers);
            }

            // =========================
            // INFO / CODE
            // =========================

            else if (type.equals("INFO")
                    || type.equals("CODE")) {

                data.put("content", content);
            }

            // =========================
            // QUESTION
            // =========================

            else {

                data.put("question", content);
            }

            json.put("data", data);

            Log.d("STEP_JSON",
                    json.toString());

            callApi(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // CALL API CREATE STEP
    // =========================

    private void callApi(JSONObject json) {

        RequestBody body =
                RequestBody.create(
                        MediaType.parse("application/json"),
                        json.toString()
                );

        ApiService api =
                RetrofitClient.getClient()
                        .create(ApiService.class);

        api.createStep(token, body)
                .enqueue(new Callback<Object>() {

                    @Override
                    public void onResponse(
                            Call<Object> call,
                            Response<Object> response
                    ) {

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    StepEditorActivity.this,
                                    "Tạo Step thành công 🚀",
                                    Toast.LENGTH_SHORT
                            ).show();

                            // 🔥 cực quan trọng:
                            // trả RESULT_OK để StepListActivity reload
                            setResult(RESULT_OK);

                            finish();

                        } else {

                            Toast.makeText(
                                    StepEditorActivity.this,
                                    "Backend từ chối tạo Step",
                                    Toast.LENGTH_LONG
                            ).show();

                            try {
                                if (response.errorBody() != null) {

                                    String err =
                                            response.errorBody()
                                                    .string();

                                    Log.e(
                                            "CREATE_STEP_ERROR",
                                            err
                                    );
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Object> call,
                            Throwable t
                    ) {

                        Toast.makeText(
                                StepEditorActivity.this,
                                "Lỗi mạng: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                        Log.e(
                                "API_ERROR",
                                String.valueOf(
                                        t.getMessage()
                                )
                        );
                    }
                });
    }
}