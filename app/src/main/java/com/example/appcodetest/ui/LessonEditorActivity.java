package com.example.appcodetest.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.appcodetest.R;
import com.example.appcodetest.api.*;
import com.example.appcodetest.model.*;

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

    String chapterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_editor);

        recycler = findViewById(R.id.recycler);
        btnAdd = findViewById(R.id.btnAdd);

        chapterId = getIntent().getStringExtra("CHAPTER_ID");

        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BaseAdapter<>(lessons, new BaseAdapter.Listener<Lesson>() {

            @Override
            public void onClick(Lesson l) {
                showLessonOptions(l);
            }

            @Override
            public void onEdit(Lesson l) {
                Toast.makeText(LessonEditorActivity.this,
                        "TODO Edit", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDelete(Lesson l) {
                lessons.remove(l);
                adapter.notifyDataSetChanged();
            }

            @Override
            public String getTitle(Lesson l) {
                return l.title;
            }
        });

        recycler.setAdapter(adapter);

        loadLessons();

        btnAdd.setOnClickListener(v -> showAddDialog());
    }

    // =========================
    private void loadLessons() {

        String token = getSharedPreferences("APP", MODE_PRIVATE)
                .getString("TOKEN", "");

        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.getLessons(chapterId, "Bearer " + token)
                .enqueue(new Callback<ApiResponse<List<Lesson>>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<List<Lesson>>> call,
                                           Response<ApiResponse<List<Lesson>>> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().result != null) {

                            lessons.clear();
                            lessons.addAll(response.body().result);

                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Lesson>>> call, Throwable t) {
                        Toast.makeText(LessonEditorActivity.this,
                                "Lỗi load lesson", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // =========================
    private void showLessonOptions(Lesson lesson) {

        String[] options = {"📂 Vào Step", "✏️ Sửa", "❌ Xóa"};

        new AlertDialog.Builder(this)
                .setTitle(lesson.title)
                .setItems(options, (d, w) -> {

                    if (w == 0) {
                        startActivity(new Intent(this,
                                StepEditorActivity.class)
                                .putExtra("LESSON_ID", lesson.id));
                    } else if (w == 1) {
                        Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    // =========================
    private void showAddDialog() {

        EditText edt = new EditText(this);
        edt.setHint("Tên bài học");

        new AlertDialog.Builder(this)
                .setTitle("Thêm Lesson")
                .setView(edt)
                .setPositiveButton("Tạo", (d,w)->{

                    String name = edt.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(this,"Nhập tên!",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    createLesson(name);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // =========================
    private void createLesson(String name) {

        try {
            JSONObject json = new JSONObject();

            json.put("chapterId", chapterId);
            json.put("title", name);
            json.put("orderIndex", lessons.size() + 1);
            json.put("xp", 50.0);
            json.put("progress", 0.0);
            json.put("lockedStatus", "FALSE_LOCKED");
            json.put("completedStatus", "FALSE");

            String token = getSharedPreferences("APP", MODE_PRIVATE)
                    .getString("TOKEN", "");

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"),
                    json.toString()
            );

            RetrofitClient.getClient().create(ApiService.class)
                    .createLesson("Bearer " + token, body)
                    .enqueue(new Callback<Object>() {

                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {

                            if (response.isSuccessful()) {
                                Toast.makeText(LessonEditorActivity.this,
                                        "Tạo thành công", Toast.LENGTH_SHORT).show();

                                loadLessons();
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {}
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}