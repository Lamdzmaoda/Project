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

public class ChapterEditorActivity extends AppCompatActivity {

    RecyclerView recycler;
    Button btnAdd;

    List<Chapter> chapters = new ArrayList<>();
    BaseAdapter<Chapter> adapter;

    String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_editor);

        // ✅ FIX ID ĐÚNG XML
        recycler = findViewById(R.id.recycler);
        btnAdd = findViewById(R.id.btnAdd);

        courseId = getIntent().getStringExtra("COURSE_ID");

        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BaseAdapter<>(chapters, new BaseAdapter.Listener<Chapter>() {

            @Override
            public void onClick(Chapter c) {
                showChapterOptions(c);
            }

            @Override
            public void onEdit(Chapter c) {
                Toast.makeText(ChapterEditorActivity.this,
                        "TODO Edit", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDelete(Chapter c) {
                Toast.makeText(ChapterEditorActivity.this,
                        "TODO Delete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public String getTitle(Chapter c) {
                return c.title;
            }
        });

        recycler.setAdapter(adapter);

        loadChapters();

        btnAdd.setOnClickListener(v -> showAddDialog());
    }

    // =========================
    private void loadChapters() {

        String token = getSharedPreferences("APP", MODE_PRIVATE)
                .getString("TOKEN", "");

        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.getChapters(courseId, "Bearer " + token)
                .enqueue(new Callback<ApiResponse<List<Chapter>>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<List<Chapter>>> call,
                                           Response<ApiResponse<List<Chapter>>> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().result != null) {

                            chapters.clear();
                            chapters.addAll(response.body().result);

                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Chapter>>> call, Throwable t) {
                        Toast.makeText(ChapterEditorActivity.this,
                                "Lỗi mạng!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // =========================
    private void showChapterOptions(Chapter chapter) {

        String[] options = {"📂 Vào Lesson", "✏️ Sửa", "❌ Xóa"};

        new AlertDialog.Builder(this)
                .setTitle(chapter.title)
                .setItems(options, (d, w) -> {

                    if (w == 0) {
                        startActivity(new Intent(this,
                                LessonEditorActivity.class)
                                .putExtra("CHAPTER_ID", chapter.id));
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
        edt.setHint("Tên chương");

        new AlertDialog.Builder(this)
                .setTitle("Thêm Chapter")
                .setView(edt)
                .setPositiveButton("Tạo", (d,w)->{

                    String name = edt.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(this,"Nhập tên!",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    createChapter(name);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // =========================
    private void createChapter(String name) {

        try {
            JSONObject json = new JSONObject();

            json.put("languageName", courseId);
            json.put("title", name);
            json.put("orderIndex", chapters.size() + 1);
            json.put("lockedStatus", "FALSE_LOCKED");

            String token = getSharedPreferences("APP", MODE_PRIVATE)
                    .getString("TOKEN", "");

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"),
                    json.toString()
            );

            RetrofitClient.getClient().create(ApiService.class)
                    .createChapter("Bearer " + token, body)
                    .enqueue(new Callback<Object>() {

                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {

                            if (response.isSuccessful()) {
                                Toast.makeText(ChapterEditorActivity.this,
                                        "Tạo thành công", Toast.LENGTH_SHORT).show();

                                loadChapters();
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