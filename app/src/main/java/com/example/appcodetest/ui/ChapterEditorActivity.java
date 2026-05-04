package com.example.appcodetest.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

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

public class ChapterEditorActivity extends AppCompatActivity {

    RecyclerView recycler;
    Button btnAdd;

    // Danh sách chapter hiện tại
    List<Chapter> chapters = new ArrayList<>();

    // Adapter dùng chung
    BaseAdapter<Chapter> adapter;

    // COURSE_ID thực chất là languageName
    String courseId;

    // Bearer token gọi API
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_editor);

        recycler = findViewById(R.id.recycler);
        btnAdd = findViewById(R.id.btnAdd);

        // lấy languageName từ màn trước
        courseId = getIntent().getStringExtra("COURSE_ID");

        Log.d("COURSE_ID_DEBUG", "languageName = " + courseId);

        // =========================
        // CHECK TOKEN
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

        // RecyclerView danh sách dọc
        recycler.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new BaseAdapter<>(chapters,
                new BaseAdapter.Listener<Chapter>() {

                    @Override
                    public void onClick(Chapter c) {
                        showChapterOptions(c);
                    }

                    @Override
                    public void onEdit(Chapter c) {
                        showEditDialog(c);
                    }

                    @Override
                    public void onDelete(Chapter c) {
                        deleteChapter(c.id);
                    }

                    @Override
                    public String getTitle(Chapter c) {
                        // debug ID tránh duplicate chapter
                        return c.title + " | ID: " + c.id;
                    }
                });

        recycler.setAdapter(adapter);

        loadChapters();

        btnAdd.setOnClickListener(v ->
                showAddDialog()
        );
    }

    // =========================
    // LOAD CHAPTER
    // =========================

    private void loadChapters() {

        Log.d("LOAD_CHAPTER_DEBUG",
                "load chapter with languageName = " + courseId);

        RetrofitClient.getClient()
                .create(ApiService.class)
                .getChapters(courseId, token)
                .enqueue(new Callback<ApiResponse<List<Chapter>>>() {

                    @Override
                    public void onResponse(
                            Call<ApiResponse<List<Chapter>>> call,
                            Response<ApiResponse<List<Chapter>>> response
                    ) {

                        chapters.clear();

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().result != null) {

                            chapters.addAll(
                                    response.body().result
                            );

                            // sort theo orderIndex
                            Collections.sort(chapters, (a, b) ->
                                    Integer.compare(
                                            a.orderIndex,
                                            b.orderIndex
                                    )
                            );
                        }

                        adapter.notifyDataSetChanged();

                        Log.d("CHAPTER_COUNT",
                                "size = " + chapters.size());
                    }

                    @Override
                    public void onFailure(
                            Call<ApiResponse<List<Chapter>>> call,
                            Throwable t
                    ) {

                        Toast.makeText(
                                ChapterEditorActivity.this,
                                "Lỗi mạng!",
                                Toast.LENGTH_SHORT
                        ).show();

                        Log.e("LOAD_CHAPTER_ERROR",
                                String.valueOf(t.getMessage()));
                    }
                });
    }

    // =========================
    // MENU OPTIONS
    // =========================

    private void showChapterOptions(Chapter chapter) {

        String[] options = {
                "📂 Vào Lesson",
                "✏️ Sửa",
                "❌ Xóa"
        };

        new AlertDialog.Builder(this)
                .setTitle(chapter.title)
                .setItems(options, (d, w) -> {

                    if (w == 0) {

                        Log.d("OPEN_LESSON_DEBUG",
                                "chapterId = " + chapter.id);

                        startActivity(
                                new Intent(
                                        this,
                                        LessonEditorActivity.class
                                ).putExtra(
                                        "CHAPTER_ID",
                                        chapter.id
                                )
                        );

                    } else if (w == 1) {

                        showEditDialog(chapter);

                    } else {

                        deleteChapter(chapter.id);
                    }
                })
                .show();
    }

    // =========================
    // CREATE CHAPTER
    // =========================

    private void createChapter(String name) {

        try {

            if (name == null || name.trim().isEmpty()) {

                Toast.makeText(
                        this,
                        "Nhập tên chapter!",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            JSONObject json = new JSONObject();

            // backend yêu cầu đủ field
            json.put("languageName", courseId);
            json.put("title", name.trim());

            // chapter mới nằm cuối
            json.put("orderIndex",
                    chapters.size() + 1);

            // enum chuẩn backend
            json.put("lockedStatus",
                    "FALSE_LOCKED");

            // backend yêu cầu field lessons
            json.put("lessons",
                    new JSONArray());

            Log.d(
                    "CREATE_CHAPTER_JSON",
                    json.toString()
            );

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"),
                    json.toString()
            );

            RetrofitClient.getClient()
                    .create(ApiService.class)
                    .createChapter(token, body)
                    .enqueue(new Callback<Object>() {

                        @Override
                        public void onResponse(
                                Call<Object> call,
                                Response<Object> response
                        ) {

                            if (response.isSuccessful()) {

                                Toast.makeText(
                                        ChapterEditorActivity.this,
                                        "Tạo chapter thành công 🚀",
                                        Toast.LENGTH_SHORT
                                ).show();

                                // force reload sạch
                                finish();
                                startActivity(getIntent());

                            } else {

                                Toast.makeText(
                                        ChapterEditorActivity.this,
                                        "Backend từ chối tạo chapter",
                                        Toast.LENGTH_LONG
                                ).show();

                                try {
                                    if (response.errorBody() != null) {

                                        String err =
                                                response.errorBody()
                                                        .string();

                                        Log.e(
                                                "CREATE_CHAPTER_ERROR",
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
                                    ChapterEditorActivity.this,
                                    "Lỗi mạng!",
                                    Toast.LENGTH_SHORT
                            ).show();

                            Log.e("CREATE_CHAPTER_FAILURE",
                                    String.valueOf(t.getMessage()));

                            t.printStackTrace();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // EDIT CHAPTER
    // =========================

    private void showEditDialog(Chapter c) {

        EditText edt = new EditText(this);
        edt.setText(c.title);

        new AlertDialog.Builder(this)
                .setTitle("Sửa Chapter")
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
                                    "Tên chapter không được rỗng!",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        JSONObject json =
                                new JSONObject();

                        json.put("title", newTitle);

                        RequestBody body =
                                RequestBody.create(
                                        MediaType.parse(
                                                "application/json"
                                        ),
                                        json.toString()
                                );

                        RetrofitClient.getClient()
                                .create(ApiService.class)
                                .updateChapter(
                                        token,
                                        c.id,
                                        body
                                )
                                .enqueue(new Callback<Object>() {

                                    @Override
                                    public void onResponse(
                                            Call<Object> call,
                                            Response<Object> response
                                    ) {

                                        loadChapters();
                                    }

                                    @Override
                                    public void onFailure(
                                            Call<Object> call,
                                            Throwable t
                                    ) {
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
    // DELETE CHAPTER
    // =========================

    private void deleteChapter(String id) {

        RetrofitClient.getClient()
                .create(ApiService.class)
                .deleteChapter(token, id)
                .enqueue(new Callback<Object>() {

                    @Override
                    public void onResponse(
                            Call<Object> call,
                            Response<Object> response
                    ) {

                        Toast.makeText(
                                ChapterEditorActivity.this,
                                "Đã xóa chapter",
                                Toast.LENGTH_SHORT
                        ).show();

                        loadChapters();
                    }

                    @Override
                    public void onFailure(
                            Call<Object> call,
                            Throwable t
                    ) {

                        Toast.makeText(
                                ChapterEditorActivity.this,
                                "Lỗi xóa chapter",
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
        edt.setHint("Tên chương");

        new AlertDialog.Builder(this)
                .setTitle("Thêm Chapter")
                .setView(edt)
                .setPositiveButton("Tạo", (d, w) -> {

                    String name =
                            edt.getText()
                                    .toString()
                                    .trim();

                    // tránh tạo rỗng / chapter rác
                    if (name.isEmpty()) {

                        Toast.makeText(
                                this,
                                "Nhập tên chapter!",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    createChapter(name);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}