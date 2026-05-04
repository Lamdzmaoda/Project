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
import com.example.appcodetest.utils.Prefs;

import java.util.*;

import retrofit2.*;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerLessons;

    TextView txtAvatar, txtStreak, txtEnergy, txtChapter, txtLanguage;
    Button btnNextChapter;

    LinearLayout btnHome, btnPractice, btnRank, btnPro, btnProfile;

    List<Language> languages = new ArrayList<>();
    List<Chapter> chapters = new ArrayList<>();
    List<Lesson> lessonList = new ArrayList<>();

    LessonAdapter adapter;

    String currentLanguage = null;
    int currentChapterIndex = 0;
    String currentChapterId = null;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // =========================
        // TOKEN
        // =========================

        String rawToken = Prefs.getToken(this);

        if (rawToken == null || rawToken.isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        token = "Bearer " + rawToken;

        // =========================
        // VIEW
        // =========================

        recyclerLessons = findViewById(R.id.recyclerLessons);
        recyclerLessons.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new LessonAdapter(this, lessonList);
        recyclerLessons.setAdapter(adapter);

        txtAvatar = findViewById(R.id.txtAvatar);
        txtStreak = findViewById(R.id.txtStreak);
        txtEnergy = findViewById(R.id.txtEnergy);

        txtChapter = findViewById(R.id.txtChapter);
        txtLanguage = findViewById(R.id.txtLanguage);

        btnNextChapter = findViewById(R.id.btnNextChapter);

        btnHome = findViewById(R.id.btnHome);
        btnPractice = findViewById(R.id.btnPractice);
        btnRank = findViewById(R.id.btnRank);
        btnPro = findViewById(R.id.btnPro);
        btnProfile = findViewById(R.id.btnProfile);

        txtLanguage.setOnClickListener(v -> showLanguagePopup());
        txtChapter.setOnClickListener(v -> showChapterPopup());

        btnNextChapter.setOnClickListener(v -> goNextChapter());

        setupBottomNav();

        loadUser();
        loadLanguages();
    }

    // =========================
    // USER
    // =========================

    private void loadUser() {

        ApiService api =
                RetrofitClient.getClient().create(ApiService.class);

        api.getMyInfo(token)
                .enqueue(new Callback<ApiResponse<UserResponse>>() {

                    @Override
                    public void onResponse(
                            Call<ApiResponse<UserResponse>> call,
                            Response<ApiResponse<UserResponse>> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().result != null) {

                            UserResponse user = response.body().result;

                            if (user.username != null
                                    && !user.username.isEmpty()) {

                                txtAvatar.setText(
                                        user.username.substring(0, 1)
                                );
                            }

                            txtStreak.setText("5");
                            txtEnergy.setText("3");
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ApiResponse<UserResponse>> call,
                            Throwable t
                    ) {
                    }
                });

        txtAvatar.setOnClickListener(v ->
                startActivity(new Intent(
                        this,
                        ProfileActivity.class
                ))
        );
    }

    // =========================
    // LANGUAGE
    // =========================

    private void loadLanguages() {

        RetrofitClient.getClient()
                .create(ApiService.class)
                .getLanguages(token)
                .enqueue(new Callback<ApiResponse<List<Language>>>() {

                    @Override
                    public void onResponse(
                            Call<ApiResponse<List<Language>>> call,
                            Response<ApiResponse<List<Language>>> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().result != null) {

                            languages.clear();
                            languages.addAll(response.body().result);

                            if (languages.isEmpty()) return;

                            String savedLanguage =
                                    Prefs.get(
                                            MainActivity.this,
                                            "CURRENT_LANG"
                                    );

                            boolean found = false;

                            if (savedLanguage != null) {
                                for (Language l : languages) {
                                    if (savedLanguage.equals(l.name)) {
                                        found = true;
                                        break;
                                    }
                                }
                            }

                            if (found) {
                                currentLanguage = savedLanguage;
                            } else {
                                currentLanguage = languages.get(0).name;
                            }

                            txtLanguage.setText(currentLanguage);

                            // reset sạch chapter + lesson cũ
                            chapters.clear();
                            lessonList.clear();
                            adapter.notifyDataSetChanged();

                            currentChapterId = null;
                            currentChapterIndex = 0;

                            loadChapters();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ApiResponse<List<Language>>> call,
                            Throwable t
                    ) {
                    }
                });
    }

    // =========================
    // CHAPTER
    // =========================

    private void loadChapters() {

        if (currentLanguage == null || currentLanguage.isEmpty()) return;

        RetrofitClient.getClient()
                .create(ApiService.class)
                .getChapters(currentLanguage, token)
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

                            chapters.addAll(response.body().result);

                            Collections.sort(
                                    chapters,
                                    (a, b) -> Integer.compare(
                                            a.orderIndex,
                                            b.orderIndex
                                    )
                            );
                        }

                        if (chapters.isEmpty()) {

                            txtChapter.setText("Không có chapter");

                            lessonList.clear();
                            adapter.notifyDataSetChanged();

                            currentChapterId = null;
                            currentChapterIndex = 0;
                            return;
                        }

                        Chapter first = chapters.get(0);

                        currentChapterIndex = 0;
                        currentChapterId = first.id;

                        txtChapter.setText(first.title);

                        // reset lesson trước khi load mới
                        lessonList.clear();
                        adapter.notifyDataSetChanged();

                        loadLessons(first.id);
                    }

                    @Override
                    public void onFailure(
                            Call<ApiResponse<List<Chapter>>> call,
                            Throwable t
                    ) {
                    }
                });
    }

    // =========================
    // LESSON
    // =========================

    private void loadLessons(String chapterId) {

        // khóa chapter hiện tại
        currentChapterId = chapterId;

        // clear ngay lập tức chống dính UI
        lessonList.clear();
        adapter.notifyDataSetChanged();

        RetrofitClient.getClient()
                .create(ApiService.class)
                .getLessons(chapterId, token)
                .enqueue(new Callback<ApiResponse<List<Lesson>>>() {

                    @Override
                    public void onResponse(
                            Call<ApiResponse<List<Lesson>>> call,
                            Response<ApiResponse<List<Lesson>>> response
                    ) {

                        // chống response cũ ghi đè response mới
                        if (!chapterId.equals(currentChapterId)) {
                            return;
                        }

                        lessonList.clear();

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().result != null) {

                            lessonList.addAll(response.body().result);

                            Collections.sort(
                                    lessonList,
                                    (a, b) -> Integer.compare(
                                            a.orderIndex,
                                            b.orderIndex
                                    )
                            );
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(
                            Call<ApiResponse<List<Lesson>>> call,
                            Throwable t
                    ) {
                    }
                });
    }

    // =========================
    // POPUP LANGUAGE
    // =========================

    private void showLanguagePopup() {

        if (languages.isEmpty()) return;

        String[] items = new String[languages.size()];

        for (int i = 0; i < languages.size(); i++) {
            items[i] = languages.get(i).name;
        }

        new AlertDialog.Builder(this)
                .setTitle("Chọn Language")
                .setItems(items, (dialog, which) -> {

                    currentLanguage = items[which];
                    txtLanguage.setText(currentLanguage);

                    Prefs.save(
                            this,
                            "CURRENT_LANG",
                            currentLanguage
                    );

                    // reset sạch
                    chapters.clear();
                    lessonList.clear();
                    adapter.notifyDataSetChanged();

                    currentChapterId = null;
                    currentChapterIndex = 0;

                    txtChapter.setText("Loading...");

                    loadChapters();
                })
                .show();
    }

    // =========================
    // POPUP CHAPTER
    // =========================

    private void showChapterPopup() {

        if (chapters.isEmpty()) return;

        String[] items = new String[chapters.size()];

        for (int i = 0; i < chapters.size(); i++) {
            items[i] = chapters.get(i).title;
        }

        new AlertDialog.Builder(this)
                .setTitle("Chọn Chapter")
                .setItems(items, (dialog, which) -> {

                    currentChapterIndex = which;

                    Chapter selected = chapters.get(which);

                    currentChapterId = selected.id;
                    txtChapter.setText(selected.title);

                    loadLessons(selected.id);
                })
                .show();
    }

    // =========================
    // NEXT CHAPTER
    // =========================

    private void goNextChapter() {

        if (chapters.isEmpty()) return;

        currentChapterIndex++;

        if (currentChapterIndex >= chapters.size()) {
            currentChapterIndex = 0;
        }

        Chapter next = chapters.get(currentChapterIndex);

        currentChapterId = next.id;
        txtChapter.setText(next.title);

        loadLessons(next.id);
    }

    // =========================
    // NAVIGATION
    // =========================

    private void setupBottomNav() {

        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(
                        this,
                        ProfileActivity.class
                ))
        );

        btnPractice.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Chưa có 😅",
                        Toast.LENGTH_SHORT
                ).show()
        );

        btnRank.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "BXH chưa có 😅",
                        Toast.LENGTH_SHORT
                ).show()
        );

        btnPro.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "PRO chưa mở 😅",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }
}