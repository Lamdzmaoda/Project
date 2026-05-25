/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Chapter;
import com.example.identity_servive.entity.learning.Language;
import com.example.identity_servive.entity.learning.Lesson;
import com.example.identity_servive.entity.learning.Problem;
import com.example.identity_servive.entity.learning.Step;
import com.example.identity_servive.entity.progress.Enrollment;
import com.example.identity_servive.enums.ContentStatus;
import com.example.identity_servive.enums.IsLocked;
import com.example.identity_servive.enums.LessonType;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.mapper.CourseMapper;
import com.example.identity_servive.repository.Progress.EnrollmentRepository;
import com.example.identity_servive.repository.Progress.UserChapterProgressRepository;
import com.example.identity_servive.repository.Progress.UserLessonProgressRepository;
import com.example.identity_servive.repository.learning.ChapterRepository;
import com.example.identity_servive.repository.learning.LanguageRepository;
import com.example.identity_servive.repository.learning.LessonRepository;
import com.example.identity_servive.repository.learning.ProblemRepository;
import com.example.identity_servive.repository.learning.StepRepository;
import com.example.identity_servive.service.learning.CourseService;
import com.example.identity_servive.service.learning.LearningProgressService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
@WithMockUser(roles = "USER")
public class CourseServiceTest {

  @Autowired private CourseService courseService;
  @MockitoBean private LessonRepository lessonRepository;
  @MockitoBean private ChapterRepository chapterRepository;
  @MockitoBean private StepRepository stepRepository;
  @MockitoBean private LanguageRepository languageRepository;
  @MockitoBean private EnrollmentRepository enrollmentRepository;
  @MockitoBean private CourseMapper courseMapper;
  @MockitoBean private UserLessonProgressRepository userLessonProgressRepository;
  @MockitoBean private UserChapterProgressRepository userChapterProgressRepository;
  @MockitoBean private LearningProgressService learningProgressService;
  @MockitoBean private ProblemRepository problemRepository;

  private User user;
  private Language language;
  private Chapter chapter;
  private Lesson lesson;
  private Step step;
  private Problem problem;

  @BeforeEach
  void initData() {
    user = User.builder().id("user-1").username("testuser").email("test@test.com").build();
    language =
        Language.builder().name("java").description("Java").chapters(new HashSet<>()).build();
    chapter =
        Chapter.builder()
            .id("chap-1")
            .title("Chapter 1")
            .orderIndex(1)
            .language(language)
            .lessons(new HashSet<>())
            .build();
    lesson =
        Lesson.builder()
            .id("lesson-1")
            .title("Lesson 1")
            .xp(50L)
            .chapter(chapter)
            .lessonType(LessonType.LEARN)
            .steps(new HashSet<>())
            .problems(new HashSet<>())
            .build();
    step =
        Step.builder()
            .id("step-1")
            .title("Step 1")
            .orderIndex(1)
            .status(ContentStatus.ACTIVE)
            .build();
    problem =
        Problem.builder().id("prob-1").title("Problem 1").status(ContentStatus.ACTIVE).build();
  }

  @Test
  void getStepById_success() {
    when(stepRepository.findByIdAndStatus(any(), any())).thenReturn(Optional.of(step));
    when(courseMapper.toStepResponse(any()))
        .thenReturn(
            com.example
                .identity_servive
                .dto
                .response
                .learningResponse
                .StepResponse
                .builder()
                .id("step-1")
                .title("Step 1")
                .build());

    var result = courseService.getStepById("step-1");
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("step-1");
  }

  @Test
  void getStepById_notFound_fail() {
    when(stepRepository.findByIdAndStatus(any(), any())).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> courseService.getStepById("invalid"));
  }

  @Test
  void getProblemById_success() {
    when(problemRepository.findByIdAndStatus(any(), any())).thenReturn(Optional.of(problem));
    when(courseMapper.toProblemResponse(any()))
        .thenReturn(
            com.example
                .identity_servive
                .dto
                .response
                .learningResponse
                .ProblemResponse
                .builder()
                .id("prob-1")
                .title("Problem 1")
                .build());

    var result = courseService.getProblemById("prob-1");
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("prob-1");
  }

  @Test
  void getProblemById_notFound_fail() {
    when(problemRepository.findByIdAndStatus(any(), any())).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> courseService.getProblemById("invalid"));
  }

  @Test
  void getLessonById_success() {
    when(lessonRepository.findByIdAndStatus(any(), any())).thenReturn(Optional.of(lesson));
    when(courseMapper.toLessonResponse(any()))
        .thenReturn(
            com.example
                .identity_servive
                .dto
                .response
                .learningResponse
                .LessonResponse
                .builder()
                .id("lesson-1")
                .title("Lesson 1")
                .build());

    var result = courseService.getLessonById("lesson-1");
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("lesson-1");
  }

  @Test
  void getChapterById_success() {
    when(chapterRepository.findByIdAndStatus(any(), any())).thenReturn(Optional.of(chapter));
    when(courseMapper.toChapterResponse(any()))
        .thenReturn(
            com.example
                .identity_servive
                .dto
                .response
                .learningResponse
                .ChapterResponse
                .builder()
                .id("chap-1")
                .title("Chapter 1")
                .build());

    var result = courseService.getChapterById("chap-1");
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("chap-1");
  }

  @Test
  void getLanguageById_success() {
    when(languageRepository.findByNameAndStatus(any(), any())).thenReturn(Optional.of(language));
    when(courseMapper.toLanguageResponse(any()))
        .thenReturn(
            com.example
                .identity_servive
                .dto
                .response
                .learningResponse
                .LanguageResponse
                .builder()
                .name("java")
                .description("Java")
                .build());

    var result = courseService.getLanguageById("java");
    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("java");
  }

  @Test
  void getStepByLesson_success() {
    when(stepRepository.findAllByLessonIdAndStatusOrderByOrderIndexAsc(any(), any()))
        .thenReturn(List.of(step));
    when(courseMapper.toStepResponse(any()))
        .thenReturn(
            com.example
                .identity_servive
                .dto
                .response
                .learningResponse
                .StepResponse
                .builder()
                .id("step-1")
                .title("Step 1")
                .build());

    var result = courseService.getStepByLesson("lesson-1");
    assertThat(result).hasSize(1);
    assertThat(result.getFirst().getId()).isEqualTo("step-1");
  }

  @Test
  void getProblemByLesson_success() {
    when(problemRepository.findAllByLessonIdAndStatusOrderByOrderIndexAsc(any(), any()))
        .thenReturn(List.of(problem));
    when(courseMapper.toProblemResponse(any()))
        .thenReturn(
            com.example
                .identity_servive
                .dto
                .response
                .learningResponse
                .ProblemResponse
                .builder()
                .id("prob-1")
                .title("Problem 1")
                .build());

    var result = courseService.getProblemByLesson("lesson-1");
    assertThat(result).hasSize(1);
    assertThat(result.getFirst().getId()).isEqualTo("prob-1");
  }

  @Test
  void getLessonByChapter_success() {
    when(lessonRepository.findAllByChapterIdAndStatusOrderByOrderIndexAsc(any(), any()))
        .thenReturn(List.of(lesson));
    when(courseMapper.toLessonResponse(any()))
        .thenReturn(
            com.example
                .identity_servive
                .dto
                .response
                .learningResponse
                .LessonResponse
                .builder()
                .id("lesson-1")
                .title("Lesson 1")
                .build());

    var result = courseService.getLessonByChapter("chap-1");
    assertThat(result).hasSize(1);
    assertThat(result.getFirst().getId()).isEqualTo("lesson-1");
  }

  @Test
  void getChapterByLanguage_success() {
    when(chapterRepository.findAllByLanguageNameAndStatusOrderByOrderIndexAsc(any(), any()))
        .thenReturn(List.of(chapter));
    when(courseMapper.toChapterResponse(any()))
        .thenReturn(
            com.example
                .identity_servive
                .dto
                .response
                .learningResponse
                .ChapterResponse
                .builder()
                .id("chap-1")
                .title("Chapter 1")
                .build());

    var result = courseService.getChapterByLanguage("java");
    assertThat(result).hasSize(1);
    assertThat(result.getFirst().getId()).isEqualTo("chap-1");
  }

  @Test
  void getMyLearning_success() {
    when(learningProgressService.getCurrentUser()).thenReturn(user);
    when(chapterRepository.findAllByLanguageNameAndStatusOrderByOrderIndexAsc(any(), any()))
        .thenReturn(List.of(chapter));
    when(userChapterProgressRepository.findByUserAndChapter(any(), any()))
        .thenReturn(Optional.empty());
    when(lessonRepository.findAllByChapterIdAndStatusOrderByOrderIndexAsc(any(), any()))
        .thenReturn(List.of(lesson));
    when(userLessonProgressRepository.findByUserAndLesson(any(), any()))
        .thenReturn(Optional.empty());
    when(courseMapper.toStepResponse(any()))
        .thenReturn(
            com.example
                .identity_servive
                .dto
                .response
                .learningResponse
                .StepResponse
                .builder()
                .id("step-1")
                .build());

    var result = courseService.getMyLearning("java");
    assertThat(result).hasSize(1);
    assertThat(result.getFirst().getId()).isEqualTo("chap-1");
    assertThat(result.getFirst().getLockedStatus()).isEqualTo(IsLocked.TRUE_LOCKED);
  }

  @Test
  void getLearningProgress_success() {
    when(learningProgressService.getCurrentUser()).thenReturn(user);
    when(languageRepository.findByNameAndStatus(any(), any())).thenReturn(Optional.of(language));
    when(enrollmentRepository.findByUserAndLanguage(any(), any()))
        .thenReturn(Optional.of(Enrollment.builder().currentXp(50L).build()));
    when(lessonRepository.countByChapterLanguageAndStatus(any(), any())).thenReturn(5);
    when(userLessonProgressRepository.countByUserAndLesson_Chapter_LanguageAndCompletedStatus(
            any(), any(), any()))
        .thenReturn(2);
    when(userChapterProgressRepository.countByUserAndChapter_LanguageAndCompletedStatus(
            any(), any(), any()))
        .thenReturn(1);

    var result = courseService.getLearningProgress("java");
    assertThat(result).isNotNull();
    assertThat(result.getCurrentXp()).isEqualTo(50);
    assertThat(result.getCompletedLessons()).isEqualTo(2);
    assertThat(result.getTotalLessons()).isEqualTo(5);
  }
}
