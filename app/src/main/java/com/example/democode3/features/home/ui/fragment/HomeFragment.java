package com.example.democode3.features.home.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.democode3.R;
import com.example.democode3.features.home.fake.FakeHomeRepository;
import com.example.democode3.features.home.fake.FakeProgressManager;
import com.example.democode3.features.home.model.Chapter;
import com.example.democode3.features.home.model.Language;
import com.example.democode3.features.home.model.Lesson;
import com.example.democode3.features.home.model.UserProgress;
import com.example.democode3.features.home.ui.component.ChapterPickerSheet;
import com.example.democode3.features.home.ui.component.LanguagePickerSheet;
import com.example.democode3.features.learning.ui.activity.LessonDetailActivity;
import com.example.democode3.core.session.LearningSessionManager;

import java.util.List;

public class HomeFragment extends Fragment {

    // =====================================================
    // VIEW
    // =====================================================

    private TextView txtLanguage;

    private TextView txtStreak;

    private TextView txtXp;

    private TextView txtChapter;

    private TextView txtProgress;

    private LinearLayout layoutLessonContainer;
    private View currentLessonView;

    LinearLayout btnNextChapter;

    // =====================================================
    // REPOSITORY
    // =====================================================

    private FakeHomeRepository repository;

    // =====================================================
    // DATA
    // =====================================================

    private List<Language> languages;

    private List<Chapter> chapters;

    private List<Lesson> lessons;

    private UserProgress userProgress;

    private int currentLanguageIndex = 0;

    private int currentChapterIndex = 0;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_home,
                container,
                false
        );

        initViews(view);

        initRepository();

        loadData();

        setupClick();

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        // =============================================
        // UPDATE XP
        // =============================================

        txtXp.setText(
                String.valueOf(
                        FakeProgressManager.xp
                )
        );

        // =============================================
        // UPDATE STREAK
        // =============================================

        txtStreak.setText(
                String.valueOf(
                        FakeProgressManager.streak
                )
        );

        // =============================================
        // UPDATE PROGRESS
        // =============================================

        txtProgress.setText(
                FakeProgressManager.chapterProgress
                        + "%"
        );
    }

    // =====================================================
    // INIT VIEW
    // =====================================================

    private void initViews(View view) {

        txtLanguage =
                view.findViewById(R.id.txtLanguage);

        txtStreak =
                view.findViewById(R.id.txtStreak);

        txtXp =
                view.findViewById(R.id.txtXp);

        txtChapter =
                view.findViewById(R.id.txtChapter);

        txtProgress =
                view.findViewById(R.id.txtProgress);

        layoutLessonContainer =
                view.findViewById(R.id.layoutLessonContainer);

        btnNextChapter =
                view.findViewById(R.id.btnNextChapter);
    }

    // =====================================================
    // REPOSITORY
    // =====================================================

    private void initRepository() {

        repository =
                new FakeHomeRepository();
    }

    // =====================================================
    // LOAD DATA
    // =====================================================

    private void loadData() {

        // =============================================
        // USER PROGRESS
        // =============================================

        userProgress =
                repository.getUserProgress();

        // =============================================
        // LANGUAGES
        // =============================================

        languages =
                repository.getLanguages();

        long currentLanguageId =
                LearningSessionManager
                        .getLanguageId(
                                requireContext()
                        );

        Language currentLanguage =
                languages.get(0);

        // =============================================
        // FIND CURRENT LANGUAGE
        // =============================================

        for (Language language : languages) {

            if (language.id
                    == currentLanguageId) {

                currentLanguage = language;

                currentLanguageIndex =
                        languages.indexOf(language);

                break;
            }
        }

        txtLanguage.setText(
                currentLanguage.name
        );

        // =============================================
        // XP
        // =============================================

        txtStreak.setText(
                String.valueOf(
                        FakeProgressManager.streak
                )
        );

        txtXp.setText(
                String.valueOf(
                        FakeProgressManager.xp
                )
        );

        // =============================================
        // CHAPTERS
        // =============================================

        chapters =
                repository.getChapters(
                        currentLanguage.id
                );

        // =============================================
        // LOAD SAVED CHAPTER
        // =============================================

        long currentChapterId =
                LearningSessionManager
                        .getChapterId(
                                requireContext()
                        );

        Chapter currentChapter =
                chapters.get(0);

        // =============================================
        // FIND CURRENT CHAPTER
        // =============================================

        for (Chapter chapter : chapters) {

            if (chapter.id
                    == currentChapterId) {

                currentChapter = chapter;

                currentChapterIndex =
                        chapters.indexOf(chapter);

                break;
            }
        }

        // =============================================
        // CHAPTER UI
        // =============================================

        txtChapter.setText(
                currentChapter.title
        );

        txtProgress.setText(
                FakeProgressManager.chapterProgress
                        + "%"
        );

        // =============================================
        // LESSONS
        // =============================================

        lessons =
                repository.getLessons(
                        currentChapter.id
                );

        renderLessons();

        // =========================================
// AUTO SCROLL CURRENT LESSON
// =========================================

        if (currentLessonView != null) {

            currentLessonView.post(() -> {

                currentLessonView.requestFocus();

                currentLessonView.getParent()
                        .requestLayout();
            });
        }
    }

    // =====================================================
    // UPDATE CHAPTER
    // =====================================================

    private void updateChapter(
            Chapter chapter
    ) {

        // =====================================
        // SAVE CURRENT CHAPTER
        // =====================================

        LearningSessionManager
                .saveChapterId(
                        requireContext(),
                        chapter.id
                );

        txtChapter.setText(
                chapter.title
        );

        int progress =
                10 + (currentChapterIndex * 25);

        FakeProgressManager.chapterProgress =
                progress;

        txtProgress.setText(
                progress + "%"
        );

        lessons =
                repository.getLessons(
                        chapter.id
                );

        renderLessons();
    }

    // =====================================================
    // RENDER LESSON
    // =====================================================

    private void renderLessons() {

        layoutLessonContainer.removeAllViews();

        for (int i = 0; i < lessons.size(); i++) {

            Lesson lesson = lessons.get(i);

            View itemView =
                    getLayoutInflater().inflate(
                            R.layout.item_lesson_path,
                            layoutLessonContainer,
                            false
                    );

            LinearLayout layoutCard =
                    itemView.findViewById(R.id.layoutCard);

            TextView txtTitle =
                    itemView.findViewById(R.id.txtTitle);

            TextView txtStatus =
                    itemView.findViewById(R.id.txtStatus);

            TextView txtSubtitle =
                    itemView.findViewById(R.id.txtSubtitle);

            TextView txtXp =
                    itemView.findViewById(R.id.txtXp);

            TextView txtLessonIcon =
                    itemView.findViewById(R.id.txtLessonIcon);

            TextView btnStart =
                    itemView.findViewById(R.id.btnStart);

            View viewDot =
                    itemView.findViewById(R.id.viewDot);

            View viewLine =
                    itemView.findViewById(R.id.viewLine);

            // =====================================
            // TITLE
            // =====================================

            txtTitle.setText(
                    lesson.orderIndex + ". " + lesson.title
            );

            txtSubtitle.setText(
                    lesson.estimatedMinute + " phút"
            );

            txtXp.setText("+50 XP");

            // =====================================
            // CURRENT LESSON
            // =====================================

            if (lesson.id
                    == FakeProgressManager.currentLessonId) {

                layoutCard.setBackgroundResource(
                        R.drawable.bg_current_lesson
                );

                layoutCard.setAlpha(1f);

                txtStatus.setText("Đang học");

                txtStatus.setTextColor(0xFF3B82F6);

                txtLessonIcon.setText("▶");

                txtLessonIcon.setTextSize(18);

                btnStart.setVisibility(View.VISIBLE);

                btnStart.setText("Tiếp tục");

                viewDot.setAlpha(1f);

                currentLessonView = itemView;
            }

            // =====================================
            // COMPLETED
            // =====================================

            else if (
                    lesson.id
                            < FakeProgressManager.currentLessonId
            ) {

                layoutCard.setBackgroundResource(
                        R.drawable.bg_completed_lesson
                );

                layoutCard.setAlpha(0.92f);

                txtStatus.setText("Hoàn thành");

                txtStatus.setTextColor(0xFF22C55E);

                txtLessonIcon.setText("✓");

                txtLessonIcon.setTextSize(22);

                btnStart.setVisibility(View.VISIBLE);

                btnStart.setText("Xem lại");

                viewDot.setAlpha(0.9f);
            }

            // =====================================
            // LOCKED
            // =====================================

            else {

                layoutCard.setBackgroundResource(
                        R.drawable.bg_card_primary
                );

                layoutCard.setAlpha(0.45f);

                txtStatus.setText("Mở khóa sau");

                txtStatus.setTextColor(0xFF94A3B8);

                txtLessonIcon.setText("🔒");

                txtLessonIcon.setTextSize(16);

                btnStart.setVisibility(View.GONE);

                txtXp.setVisibility(View.GONE);

                viewDot.setAlpha(0.35f);
            }

            // =====================================
            // LAST ITEM
            // =====================================

            if (i == lessons.size() - 1) {

                viewLine.setVisibility(View.GONE);
            }

            // =====================================
            // CLICK
            // =====================================

            layoutCard.setOnClickListener(v -> {

                if (
                        lesson.id
                                > FakeProgressManager.currentLessonId
                ) {

                    return;
                }

                LearningSessionManager.saveLessonId(
                        requireContext(),
                        lesson.id
                );

                Intent intent =
                        new Intent(
                                requireContext(),
                                LessonDetailActivity.class
                        );

                intent.putExtra(
                        "lessonId",
                        lesson.id
                );

                startActivity(intent);
            });

            layoutLessonContainer.addView(itemView);
        }
    }
    // =====================================================
    // CLICK
    // =====================================================

    private void setupClick() {

        // =============================================
        // LANGUAGE PICKER
        // =============================================

        txtLanguage.setOnClickListener(v -> {

            LanguagePickerSheet sheet =
                    new LanguagePickerSheet(

                            languages,

                            language -> {

                                LearningSessionManager
                                        .saveLanguageId(
                                                requireContext(),
                                                language.id
                                        );

                                txtLanguage.setText(
                                        language.name
                                );

                                currentChapterIndex = 0;

                                chapters =
                                        repository.getChapters(
                                                language.id
                                        );

                                Chapter currentChapter =
                                        chapters.get(0);

                                updateChapter(currentChapter);
                            }
                    );

            sheet.show(
                    getParentFragmentManager(),
                    "LanguagePicker"
            );
        });

        // =============================================
        // CHAPTER PICKER
        // =============================================

        txtChapter.setOnClickListener(v -> {

            ChapterPickerSheet sheet =
                    new ChapterPickerSheet(

                            chapters,

                            chapter -> {

                                currentChapterIndex =
                                        chapters.indexOf(chapter);

                                updateChapter(chapter);
                            }
                    );

            sheet.show(
                    getParentFragmentManager(),
                    "ChapterPicker"
            );
        });

        // =============================================
        // PROGRESS CLICK
        // =============================================

        txtProgress.setOnClickListener(v -> {

            txtChapter.performClick();
        });

        // =============================================
        // NEXT CHAPTER
        // =============================================

        btnNextChapter.setOnClickListener(v -> {

            currentChapterIndex++;

            if (currentChapterIndex >= chapters.size()) {

                currentChapterIndex = 0;
            }

            Chapter nextChapter =
                    chapters.get(currentChapterIndex);

            updateChapter(nextChapter);
        });
    }
}