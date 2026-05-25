/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Chapter;
import com.example.identity_servive.entity.learning.Language;
import com.example.identity_servive.entity.learning.Lesson;
import com.example.identity_servive.entity.progress.Enrollment;
import com.example.identity_servive.entity.progress.UserChapterProgress;
import com.example.identity_servive.entity.progress.UserLessonProgress;
import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.repository.Progress.EnrollmentRepository;
import com.example.identity_servive.repository.Progress.SubmissionRepository;
import com.example.identity_servive.repository.Progress.UserChapterProgressRepository;
import com.example.identity_servive.repository.Progress.UserLessonProgressRepository;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.learning.ChapterRepository;
import com.example.identity_servive.repository.learning.LanguageRepository;
import com.example.identity_servive.repository.learning.LessonRepository;
import com.example.identity_servive.service.learning.LearningProgressService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class LearningProgressServiceTest {

  @Autowired private LearningProgressService learningProgressService;
  @MockitoBean private ChapterRepository chapterRepository;
  @MockitoBean private UserChapterProgressRepository userChapterProgressRepository;
  @MockitoBean private LessonRepository lessonRepository;
  @MockitoBean private UserLessonProgressRepository userLessonProgressRepository;
  @MockitoBean private UserRepository userRepository;
  @MockitoBean private EnrollmentRepository enrollmentRepository;
  @MockitoBean private LanguageRepository languageRepository;
  @MockitoBean private SubmissionRepository submissionRepository;

  private User user;
  private Language language;
  private Chapter chapter;
  private Lesson lesson;

  @BeforeEach
  void initData() {
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("testuser", "password", null));
    user =
        User.builder()
            .id("user-1")
            .username("testuser")
            .email("test@test.com")
            .totalXp(0L)
            .streak(0)
            .longestStreak(0)
            .build();
    language = Language.builder().name("java").description("Java").build();
    chapter =
        Chapter.builder()
            .id("chap-1")
            .title("Chapter 1")
            .orderIndex(1)
            .language(language)
            .totalXp(100L)
            .build();
    lesson = Lesson.builder().id("lesson-1").title("Lesson 1").xp(50L).chapter(chapter).build();
  }

  @Test
  void getCurrentUser_success() {
    when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
    var result = learningProgressService.getCurrentUser();
    assertThat(result).isNotNull();
    assertThat(result.getUsername()).isEqualTo("testuser");
  }

  @Test
  void getCurrentUser_unauthenticated_fail() {
    SecurityContextHolder.clearContext();
    assertThrows(AppException.class, () -> learningProgressService.getCurrentUser());
  }

  @Test
  void completeLesson_updatesXpAndStreak() {
    when(userLessonProgressRepository.findByUserAndLesson(any(), any()))
        .thenReturn(Optional.empty());
    when(userLessonProgressRepository.countByUserAndLesson_Chapter_LanguageAndCompletedStatus(
            any(), any(), any()))
        .thenReturn(1);
    when(lessonRepository.countByChapterLanguageAndStatus(any(), any())).thenReturn(2);
    when(lessonRepository.countByChapterIdAndStatus(any(), any())).thenReturn(1);
    when(userLessonProgressRepository.countByUserAndLesson_ChapterAndCompletedStatus(
            any(), any(), any()))
        .thenReturn(1);
    when(enrollmentRepository.findByUserAndLanguage(any(), any()))
        .thenReturn(Optional.of(Enrollment.builder().currentXp(0L).build()));
    when(chapterRepository.findFirstByLanguageAndStatusAndOrderIndexGreaterThanOrderByOrderIndexAsc(
            any(), any(), anyInt()))
        .thenReturn(Optional.empty());

    learningProgressService.completeLesson(user, lesson, 50L);

    assertThat(user.getTotalXp()).isEqualTo(50L);
    assertThat(user.getStreak()).isEqualTo(1);
    assertThat(user.getLastActivityDate()).isEqualTo(LocalDate.now());
    verify(userLessonProgressRepository).save(any(UserLessonProgress.class));
  }

  @Test
  void completeLesson_alreadyCompleted_skips() {
    var existing =
        UserLessonProgress.builder()
            .completedStatus(IsCompleted.TRUE)
            .lockedStatus(IsLocked.FALSE_LOCKED)
            .build();
    when(userLessonProgressRepository.findByUserAndLesson(any(), any()))
        .thenReturn(Optional.of(existing));

    learningProgressService.updateLessonToCompleted(user, lesson);

    assertThat(existing.getCompletedStatus()).isEqualTo(IsCompleted.TRUE);
  }

  @Test
  void initializeLearningProgressForLanguage_success() {
    when(chapterRepository.findFirstByLanguageAndStatusOrderByOrderIndexAsc(any(), any()))
        .thenReturn(Optional.of(chapter));
    when(userChapterProgressRepository.findByUserAndChapter(any(), any()))
        .thenReturn(Optional.empty());
    when(lessonRepository.findAllByChapterIdAndStatusOrderByOrderIndexAsc(any(), any()))
        .thenReturn(List.of(lesson));
    when(userLessonProgressRepository.findByUserAndLesson(any(), any()))
        .thenReturn(Optional.empty());

    learningProgressService.initializeLearningProgressForLanguage(language, user);

    verify(userChapterProgressRepository).save(any(UserChapterProgress.class));
    verify(userLessonProgressRepository).save(any(UserLessonProgress.class));
  }

  @Test
  void restProgress_success() {
    when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
    when(languageRepository.findByNameAndStatus(any(), any())).thenReturn(Optional.of(language));
    when(enrollmentRepository.findByUserAndLanguage(any(), any()))
        .thenReturn(
            Optional.of(Enrollment.builder().currentXp(100L).progressPercentage(50).build()));

    learningProgressService.resetProgress("java");

    verify(submissionRepository).deleteAllByUserAndLesson_Chapter_Language(any(), any());
    verify(userChapterProgressRepository).deleteAllByUserAndChapter_Language(any(), any());
    verify(userLessonProgressRepository).deleteAllByUserAndLesson_Chapter_Language(any(), any());
    verify(enrollmentRepository).save(any(Enrollment.class));
  }
}
