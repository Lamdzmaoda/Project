package com.example.appcodetest.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcodetest.R;
import com.example.appcodetest.api.ApiService;
import com.example.appcodetest.api.RetrofitClient;
import com.example.appcodetest.model.ApiResponse;
import com.example.appcodetest.model.Language;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.*;

public class CourseEditorActivity extends AppCompatActivity {

    RecyclerView recyclerCourse;
    Button btnAdd;

    List<Language> courses = new ArrayList<>();
    BaseAdapter<Language> adapter;

    ApiService api;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);

        recyclerCourse = findViewById(R.id.recyclerCourse);
        btnAdd = findViewById(R.id.btnAddCourse);

        recyclerCourse.setLayoutManager(new LinearLayoutManager(this));

        token = "Bearer " + getSharedPreferences("APP", MODE_PRIVATE)
                .getString("TOKEN", "");

        api = RetrofitClient.getClient().create(ApiService.class);

        adapter = new BaseAdapter<>(courses, new BaseAdapter.Listener<Language>() {

            @Override
            public void onClick(Language l) {
                startActivity(new Intent(CourseEditorActivity.this,
                        ChapterEditorActivity.class)
                        .putExtra("COURSE_ID", l.name));
            }

            @Override
            public void onEdit(Language l) {
                showEditDialog(l);
            }

            @Override
            public void onDelete(Language l) {
                deleteCourse(l.name); // 🔥 FIX
            }

            @Override
            public String getTitle(Language l) {
                return l.name;
            }
        });

        recyclerCourse.setAdapter(adapter);

        loadCourses();

        btnAdd.setOnClickListener(v -> showCreateDialog());
    }

    // =========================
    // LOAD
    // =========================
    private void loadCourses() {

        api.getLanguages(token)
                .enqueue(new Callback<ApiResponse<List<Language>>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<List<Language>>> call,
                                           Response<ApiResponse<List<Language>>> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().result != null) {

                            courses.clear();
                            courses.addAll(response.body().result);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Language>>> call, Throwable t) {
                        Toast.makeText(CourseEditorActivity.this,
                                "Lỗi load API", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // =========================
    // CREATE
    // =========================
    private void showCreateDialog() {

        EditText edt = new EditText(this);
        edt.setHint("Tên language");

        new AlertDialog.Builder(this)
                .setTitle("Thêm Course")
                .setView(edt)
                .setPositiveButton("TẠO", (d, w) -> {

                    String name = edt.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(this, "Nhập tên!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    createCourse(name);

                })
                .setNegativeButton("HUỶ", null)
                .show();
    }

    private void createCourse(String name) {

        try {
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("description", "New Course");

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"),
                    json.toString()
            );

            api.createLanguage(token, body)
                    .enqueue(new Callback<Object>() {

                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Toast.makeText(CourseEditorActivity.this,
                                    "Tạo thành công 🚀", Toast.LENGTH_SHORT).show();
                            loadCourses();
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Toast.makeText(CourseEditorActivity.this,
                                    "Lỗi mạng", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // UPDATE
    // =========================
    private void showEditDialog(Language l) {

        EditText edt = new EditText(this);
        edt.setText(l.name);

        new AlertDialog.Builder(this)
                .setTitle("Sửa Language")
                .setView(edt)
                .setPositiveButton("Lưu", (d, w) -> {

                    try {
                        JSONObject json = new JSONObject();
                        json.put("name", edt.getText().toString());

                        RequestBody body = RequestBody.create(
                                MediaType.parse("application/json"),
                                json.toString()
                        );

                        api.updateLanguage(token, l.name, body); // 🔥 FIX

                        api.updateLanguage(token, l.name, body)
                                .enqueue(new Callback<Object>() {

                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        Toast.makeText(CourseEditorActivity.this,
                                                "Đã cập nhật", Toast.LENGTH_SHORT).show();
                                        loadCourses();
                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {
                                        Toast.makeText(CourseEditorActivity.this,
                                                "Lỗi update", Toast.LENGTH_SHORT).show();
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
    private void deleteCourse(String name) {

        api.deleteLanguage(token, name) // 🔥 FIX
                .enqueue(new Callback<Object>() {

                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Toast.makeText(CourseEditorActivity.this,
                                "Đã xóa", Toast.LENGTH_SHORT).show();
                        loadCourses();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(CourseEditorActivity.this,
                                "Lỗi xóa", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}