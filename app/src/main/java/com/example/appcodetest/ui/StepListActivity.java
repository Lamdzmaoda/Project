package com.example.appcodetest.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.appcodetest.R;
import com.example.appcodetest.api.*;
import com.example.appcodetest.model.*;
import com.example.appcodetest.utils.Prefs;

import java.util.*;

import retrofit2.*;

public class StepListActivity extends AppCompatActivity {

    RecyclerView recycler;
    Button btnAdd;

    // danh sách step của lesson
    List<LessonStep> steps = new ArrayList<>();

    BaseAdapter<LessonStep> adapter;

    String lessonId;
    String token;

    // =========================
    // 🔥 launcher nhận kết quả từ Editor
    // dùng để khi thêm/sửa xong sẽ tự reload list
    // =========================

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        // nếu editor trả về thành công
                        if (result.getResultCode() == RESULT_OK) {
                            loadSteps();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        // =========================
        // ÁNH XẠ VIEW
        // =========================

        recycler = findViewById(R.id.recyclerStep);
        btnAdd = findViewById(R.id.btnAddStep);

        // lấy lesson id từ màn trước
        lessonId = getIntent().getStringExtra("LESSON_ID");

        // =========================
        // CHECK TOKEN
        // =========================

        String raw = Prefs.getToken(this);

        // tránh crash nếu token null
        if (raw == null || raw.isEmpty()) {

            Toast.makeText(this,
                    "Chưa đăng nhập!",
                    Toast.LENGTH_SHORT).show();

            finish();
            return;
        }

        token = "Bearer " + raw;

        // RecyclerView hiển thị dạng list dọc
        recycler.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // =========================
        // ADAPTER STEP
        // =========================

        adapter = new BaseAdapter<>(
                steps,
                new BaseAdapter.Listener<LessonStep>() {

                    // click item -> mở popup lựa chọn
                    @Override
                    public void onClick(LessonStep s) {
                        showOptions(s);
                    }

                    // click nút sửa
                    @Override
                    public void onEdit(LessonStep s) {
                        openEditor(s);
                    }

                    // click nút xóa
                    @Override
                    public void onDelete(LessonStep s) {
                        deleteStep(s.id);
                    }

                    // tiêu đề hiển thị
                    @Override
                    public String getTitle(LessonStep s) {
                        return s.title + " (" + s.type + ")";
                    }
                }
        );

        recycler.setAdapter(adapter);

        // load dữ liệu lần đầu
        loadSteps();

        // nút thêm step mới
        btnAdd.setOnClickListener(v ->
                openEditor(null)
        );
    }

    // =========================
    // LOAD DANH SÁCH STEP
    // =========================

    private void loadSteps() {

        RetrofitClient.getClient()
                .create(ApiService.class)
                .getSteps(lessonId, token)
                .enqueue(new Callback<ApiResponse<List<LessonStep>>>() {

                    @Override
                    public void onResponse(
                            Call<ApiResponse<List<LessonStep>>> call,
                            Response<ApiResponse<List<LessonStep>>> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().result != null) {

                            // clear list cũ
                            steps.clear();

                            // add list mới từ API
                            steps.addAll(
                                    response.body().result
                            );

                            // cập nhật giao diện
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ApiResponse<List<LessonStep>>> call,
                            Throwable t
                    ) {
                    }
                });
    }

    // =========================
    // POPUP CHỌN SỬA / XÓA
    // =========================

    private void showOptions(LessonStep s) {

        String[] ops = {
                "✏️ Sửa",
                "❌ Xóa"
        };

        new AlertDialog.Builder(this)
                .setTitle(s.title)
                .setItems(ops, (d, w) -> {

                    // đóng dialog trước để tránh lag
                    d.dismiss();

                    if (w == 0) {
                        openEditor(s);
                    } else {
                        deleteStep(s.id);
                    }
                })
                .show();
    }

    // =========================
    // MỞ STEP EDITOR
    // =========================

    private void openEditor(LessonStep step) {

        Intent i =
                new Intent(
                        this,
                        StepEditorActivity.class
                );

        // lesson hiện tại
        i.putExtra("LESSON_ID", lessonId);

        // nếu là edit thì truyền thêm dữ liệu
        if (step != null) {

            i.putExtra("STEP_ID", step.id);
            i.putExtra("TITLE", step.title);
            i.putExtra("TYPE", step.type);
        }

        // dùng launcher thay vì startActivity
        launcher.launch(i);
    }

    // =========================
    // DELETE STEP
    // =========================

    private void deleteStep(String id) {

        RetrofitClient.getClient()
                .create(ApiService.class)
                .deleteStep(token, id)
                .enqueue(new Callback<Object>() {

                    @Override
                    public void onResponse(
                            Call<Object> call,
                            Response<Object> response
                    ) {

                        // xóa xong thì reload lại list
                        loadSteps();
                    }

                    @Override
                    public void onFailure(
                            Call<Object> call,
                            Throwable t
                    ) {
                    }
                });
    }
}