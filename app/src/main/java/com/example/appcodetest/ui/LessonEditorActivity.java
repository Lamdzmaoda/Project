package com.example.appcodetest.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.appcodetest.R;
import com.example.appcodetest.api.*;
import com.example.appcodetest.model.*;
import com.example.appcodetest.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.*;

public class LessonEditorActivity extends AppCompatActivity {

    RecyclerView recycler;
    Button btnAdd;

    List<Lesson> lessons = new ArrayList<>();
    BaseAdapter<Lesson> adapter;

    // backend cần chapter.id thật
    String chapterId;

    String token;

    // 🔥 dùng launcher để Step quay về tự reload
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_editor);

        recycler = findViewById(R.id.recycler);
        btnAdd = findViewById(R.id.btnAdd);

        // =========================
        // lấy đúng CHAPTER_ID
        // =========================

        chapterId = getIntent().getStringExtra("CHAPTER_ID");

        Log.d("CHAPTER_ID_DEBUG", "chapterId = " + chapterId);

        if (chapterId == null || chapterId.trim().isEmpty()) {
            Toast.makeText(this,
                    "Không tìm thấy Chapter ID!",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // =========================
        // TOKEN
        // =========================

        String rawToken = Prefs.getToken(this);

        if (rawToken == null || rawToken.isEmpty()) {
            Toast.makeText(this,
                    "Token hết hạn!",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        token = "Bearer " + rawToken;

        // =========================
        // launcher reload khi quay từ Step
        // =========================

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadLessons();
                    }
                }
        );

        // =========================
        // RECYCLER
        // =========================

        recycler.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new BaseAdapter<>(
                lessons,
                new BaseAdapter.Listener<Lesson>() {

                    @Override
                    public void onClick(Lesson lesson) {
                        showLessonOptions(lesson);
                    }

                    @Override
                    public void onEdit(Lesson lesson) {
                        showEditDialog(lesson);
                    }

                    @Override
                    public void onDelete(Lesson lesson) {
                        deleteLesson(lesson.id);
                    }

                    @Override
                    public String getTitle(Lesson lesson) {
                        // debug tránh duplicate lesson
                        return lesson.title + " | ID: " + lesson.id;
                    }
                }
        );

        recycler.setAdapter(adapter);

        loadLessons();

        btnAdd.setOnClickListener(v ->
                showAddDialog()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLessons();
    }

    // =========================
    // LOAD LESSONS
    // =========================

    private void loadLessons() {

        Log.d("LOAD_LESSON_DEBUG",
                "load with chapterId = " + chapterId);

        RetrofitClient.getClient()
                .create(ApiService.class)
                .getLessons(chapterId, token)
                .enqueue(new Callback<ApiResponse<List<Lesson>>>() {

                    @Override
                    public void onResponse(
                            Call<ApiResponse<List<Lesson>>> call,
                            Response<ApiResponse<List<Lesson>>> response
                    ) {

                        lessons.clear();

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().result != null) {

                            lessons.addAll(response.body().result);

                            Collections.sort(
                                    lessons,
                                    (a, b) -> Integer.compare(
                                            a.orderIndex,
                                            b.orderIndex
                                    )
                            );
                        }

                        adapter.notifyDataSetChanged();

                        Log.d("LESSON_COUNT",
                                "size = " + lessons.size());
                    }

                    @Override
                    public void onFailure(
                            Call<ApiResponse<List<Lesson>>> call,
                            Throwable t
                    ) {

                        Toast.makeText(
                                LessonEditorActivity.this,
                                "Lỗi load lesson: " + t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();

                        Log.e("LOAD_LESSON_ERROR",
                                String.valueOf(t.getMessage()));
                    }
                });
    }

    // =========================
    // OPTIONS
    // =========================

    private void showLessonOptions(Lesson lesson) {

        String[] options = {
                "📂 Quản lý Step",
                "✏️ Sửa",
                "❌ Xóa"
        };

        new AlertDialog.Builder(this)
                .setTitle(lesson.title)
                .setItems(options, (d, w) -> {

                    if (w == 0) {

                        Intent intent = new Intent(
                                this,
                                StepListActivity.class
                        );

                        intent.putExtra(
                                "LESSON_ID",
                                lesson.id
                        );

                        // 🔥 launcher thay vì startActivity
                        launcher.launch(intent);

                    } else if (w == 1) {

                        showEditDialog(lesson);

                    } else {

                        deleteLesson(lesson.id);
                    }
                })
                .show();
    }

    // =========================
    // CREATE LESSON
    // =========================

    private void createLesson(String name) {

        try {

            if (name == null || name.trim().isEmpty()) {
                Toast.makeText(this,
                        "Nhập tên lesson!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject json = new JSONObject();

            json.put("chapterId", chapterId);
            json.put("title", name.trim());

            json.put("orderIndex",
                    lessons.size() + 1);

            json.put("xp", 50.0);
            json.put("progress", 0.0);

            json.put("lockedStatus",
                    "FALSE_LOCKED");

            json.put("completedStatus",
                    "FALSE");

            json.put("contentMarkdown", "");

            json.put("steps",
                    new JSONArray());

            Log.d("CREATE_LESSON_JSON",
                    json.toString());

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"),
                    json.toString()
            );

            RetrofitClient.getClient()
                    .create(ApiService.class)
                    .createLesson(token, body)
                    .enqueue(new Callback<Object>() {

                        @Override
                        public void onResponse(
                                Call<Object> call,
                                Response<Object> response
                        ) {

                            if (response.isSuccessful()) {

                                Toast.makeText(
                                        LessonEditorActivity.this,
                                        "Tạo lesson thành công 🚀",
                                        Toast.LENGTH_SHORT
                                ).show();

                                // 🔥 reload sạch chuẩn nhất
                                setResult(RESULT_OK);
                                loadLessons();

                            } else {

                                Toast.makeText(
                                        LessonEditorActivity.this,
                                        "Backend từ chối tạo lesson",
                                        Toast.LENGTH_LONG
                                ).show();

                                try {
                                    if (response.errorBody() != null) {

                                        String err =
                                                response.errorBody()
                                                        .string();

                                        Log.e(
                                                "CREATE_LESSON_ERROR",
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
                                    LessonEditorActivity.this,
                                    "Lỗi mạng: " + t.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();

                            Log.e(
                                    "CREATE_LESSON_FAILURE",
                                    String.valueOf(t.getMessage())
                            );
                        }
                    });

        } catch (Exception e) {

            e.printStackTrace();

            Toast.makeText(
                    this,
                    "Lỗi tạo lesson",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // =========================
    // EDIT
    // =========================

    private void showEditDialog(Lesson lesson) {

        EditText edt = new EditText(this);
        edt.setText(lesson.title);

        new AlertDialog.Builder(this)
                .setTitle("Sửa Lesson")
                .setView(edt)
                .setPositiveButton("Lưu", (d, w) -> {

                    try {

                        String newTitle =
                                edt.getText()
                                        .toString()
                                        .trim();

                        if (newTitle.isEmpty()) {
                            Toast.makeText(
                                    this,
                                    "Tên lesson không được rỗng!",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        JSONObject json =
                                new JSONObject();

                        json.put("title", newTitle);

                        RequestBody body =
                                RequestBody.create(
                                        MediaType.parse("application/json"),
                                        json.toString()
                                );

                        RetrofitClient.getClient()
                                .create(ApiService.class)
                                .updateLesson(
                                        token,
                                        lesson.id,
                                        body
                                )
                                .enqueue(new Callback<Object>() {

                                    @Override
                                    public void onResponse(
                                            Call<Object> call,
                                            Response<Object> response
                                    ) {

                                        loadLessons();
                                    }

                                    @Override
                                    public void onFailure(
                                            Call<Object> call,
                                            Throwable t
                                    ) {

                                        Toast.makeText(
                                                LessonEditorActivity.this,
                                                "Lỗi update lesson",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    // =========================
    // DELETE
    // =========================

    private void deleteLesson(String id) {

        RetrofitClient.getClient()
                .create(ApiService.class)
                .deleteLesson(token, id)
                .enqueue(new Callback<Object>() {

                    @Override
                    public void onResponse(
                            Call<Object> call,
                            Response<Object> response
                    ) {

                        Toast.makeText(
                                LessonEditorActivity.this,
                                "Đã xóa lesson",
                                Toast.LENGTH_SHORT
                        ).show();

                        loadLessons();
                    }

                    @Override
                    public void onFailure(
                            Call<Object> call,
                            Throwable t
                    ) {

                        Toast.makeText(
                                LessonEditorActivity.this,
                                "Lỗi xóa lesson",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    // =========================
    // ADD DIALOG
    // =========================

    private void showAddDialog() {

        EditText edt = new EditText(this);
        edt.setHint("Tên bài học");

        new AlertDialog.Builder(this)
                .setTitle("Thêm Lesson")
                .setView(edt)
                .setPositiveButton("Tạo", (d, w) -> {

                    String name =
                            edt.getText()
                                    .toString()
                                    .trim();

                    if (name.isEmpty()) {
                        Toast.makeText(
                                this,
                                "Nhập tên lesson!",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    createLesson(name);
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}