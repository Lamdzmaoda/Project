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
import com.example.appcodetest.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.*;

public class CourseEditorActivity extends AppCompatActivity {

    RecyclerView recyclerCourse;
    Button btnAdd;

    // Danh sách course (language)
    List<Language> courses = new ArrayList<>();

    // Adapter dùng chung
    BaseAdapter<Language> adapter;

    ApiService api;

    // Bearer token gọi API
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);

        recyclerCourse = findViewById(R.id.recyclerCourse);
        btnAdd = findViewById(R.id.btnAddCourse);

        recyclerCourse.setLayoutManager(new LinearLayoutManager(this));

        // 🔥 Lấy token đã lưu
        String rawToken = Prefs.getToken(this);

        // 🔥 Kiểm tra token
        // nếu token hết hạn thì quay về Login
        if (rawToken == null || rawToken.isEmpty()) {
            Toast.makeText(this, "Token hết hạn!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        token = "Bearer " + rawToken;

        // Khởi tạo API
        api = RetrofitClient.getClient().create(ApiService.class);

        adapter = new BaseAdapter<>(courses, new BaseAdapter.Listener<Language>() {

            @Override
            public void onClick(Language l) {

                // Click course → vào danh sách chapter
                startActivity(new Intent(CourseEditorActivity.this,
                        ChapterEditorActivity.class)
                        .putExtra("COURSE_ID", l.name));
            }

            @Override
            public void onEdit(Language l) {
                // Sửa course
                showEditDialog(l);
            }

            @Override
            public void onDelete(Language l) {
                // Xóa course
                deleteCourse(l.name);
            }

            @Override
            public String getTitle(Language l) {
                return l.name;
            }
        });

        recyclerCourse.setAdapter(adapter);

        // Load dữ liệu ban đầu
        loadCourses();

        // Nút thêm course
        btnAdd.setOnClickListener(v -> showCreateDialog());
    }

    // =========================
    // LOAD COURSE
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
                                "Lỗi load API",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // =========================
    // DIALOG CREATE
    // =========================

    private void showCreateDialog() {

        EditText edt = new EditText(this);
        edt.setHint("Tên language");

        new AlertDialog.Builder(this)
                .setTitle("Thêm Course")
                .setView(edt)
                .setPositiveButton("TẠO", (d, w) -> {

                    String name = edt.getText().toString().trim();

                    // Không cho tạo rỗng
                    if (name.isEmpty()) {
                        Toast.makeText(this,
                                "Nhập tên!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    createCourse(name);

                })
                .setNegativeButton("HUỶ", null)
                .show();
    }

    // =========================
    // CREATE COURSE
    // =========================

    private void createCourse(String name) {

        try {
            JSONObject json = new JSONObject();

            // 🔥 Backend yêu cầu đầy đủ field
            json.put("name", name);
            json.put("description", "Mô tả cho " + name);
            json.put("icon", "https://example.com/default-icon.png");
            json.put("level", 1);

            // Backend đang dùng kiểu datetime
            json.put("durationDays", "2026-12-31T23:59:59");

            // Backend yêu cầu chapters
            json.put("chapters", new JSONArray());

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"),
                    json.toString()
            );

            api.createLanguage(token, body)
                    .enqueue(new Callback<Object>() {

                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {

                            Toast.makeText(CourseEditorActivity.this,
                                    "Tạo thành công 🚀",
                                    Toast.LENGTH_SHORT).show();

                            loadCourses();
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Toast.makeText(CourseEditorActivity.this,
                                    "Lỗi mạng",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // DELETE COURSE
    // =========================

    private void deleteCourse(String name) {

        try {
            // Encode để tránh lỗi tên có dấu cách
            String encoded = URLEncoder.encode(name, "UTF-8");

            api.deleteLanguage(token, encoded)
                    .enqueue(new Callback<Object>() {

                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {

                            Toast.makeText(CourseEditorActivity.this,
                                    "Đã xóa",
                                    Toast.LENGTH_SHORT).show();

                            loadCourses();
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Toast.makeText(CourseEditorActivity.this,
                                    "Lỗi xóa",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // EDIT COURSE
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

                        // sửa tên language
                        json.put("name", edt.getText().toString());

                        RequestBody body = RequestBody.create(
                                MediaType.parse("application/json"),
                                json.toString()
                        );

                        api.updateLanguage(token, l.name, body)
                                .enqueue(new Callback<Object>() {

                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        loadCourses();
                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {
                                    }
                                });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}