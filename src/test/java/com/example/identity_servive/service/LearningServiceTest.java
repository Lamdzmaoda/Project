/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.identity_servive.dto.request.learningRequest.LessonBatchRequest;
import com.example.identity_servive.dto.request.learningRequest.VerifyRequest;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Lesson;
import com.example.identity_servive.entity.learning.Step;
import com.example.identity_servive.entity.progress.UserLessonProgress;
import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
import com.example.identity_servive.enums.Type;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.repository.Progress.UserLessonProgressRepository;
import com.example.identity_servive.repository.learning.LessonRepository;
import com.example.identity_servive.service.learning.LearningProgressService;
import com.example.identity_servive.service.learning.LearningService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class LearningServiceTest {

  @Autowired private LearningService learningService;
  @MockitoBean private LessonRepository lessonRepository;
  @MockitoBean private UserLessonProgressRepository userLessonProgressRepository;
  @MockitoBean private LearningProgressService learningProgressService;

  private User user;
  private Lesson lesson;
  private Step step1;
  private Step step2;

  @BeforeEach
  void initData() {
    user = User.builder().id("user-1").username("testuser").email("test@test.com").build();
    step1 =
        Step.builder()
            .id("step-1")
            .title("Step 1")
            .type(Type.QUIZ)
            .data(Map.of("correctValue", "answer1"))
            .build();
    step2 =
        Step.builder()
            .id("step-2")
            .title("Step 2")
            .type(Type.QUIZ)
            .data(Map.of("correctValue", "answer2"))
            .build();
    lesson =
        Lesson.builder()
            .id("lesson-1")
            .title("Lesson 1")
            .xp(50L)
            .steps(Set.of(step1, step2))
            .build();
  }

  @Test
  void verifyLesson_success() {
    when(learningProgressService.getCurrentUser()).thenReturn(user);
    var progress =
        UserLessonProgress.builder()
            .lockedStatus(IsLocked.FALSE_LOCKED)
            .completedStatus(IsCompleted.FALSE)
            .build();
    when(lessonRepository.findByIdAndStatus(any(), any())).thenReturn(Optional.of(lesson));
    when(userLessonProgressRepository.findByUserAndLesson(any(), any()))
        .thenReturn(Optional.of(progress));

    var request =
        LessonBatchRequest.builder()
            .lessonId("lesson-1")
            .requestSteps(
                List.of(
                    VerifyRequest.builder().stepId("step-1").answer("answer1").build(),
                    VerifyRequest.builder().stepId("step-2").answer("answer2").build()))
            .build();

    var result = learningService.verifyLesson(request);
    assertThat(result).isNotNull();
    assertThat(result.getTotalXpGained()).isEqualTo(100L);
    assertThat(result.isNewlyCompleted()).isTrue();
    assertThat(result.getVerifyResponses()).hasSize(2);
    assertThat(result.getVerifyResponses().get(0).isCorrect()).isTrue();
  }

  @Test
  void verifyLesson_lessonNotFound_fail() {
    when(learningProgressService.getCurrentUser()).thenReturn(user);
    when(lessonRepository.findByIdAndStatus(any(), any())).thenReturn(Optional.empty());

    var request = LessonBatchRequest.builder().lessonId("invalid").requestSteps(List.of()).build();

    assertThrows(AppException.class, () -> learningService.verifyLesson(request));
  }

  @Test
  void verifyLesson_stepMismatch_fail() {
    when(learningProgressService.getCurrentUser()).thenReturn(user);
    when(lessonRepository.findByIdAndStatus(any(), any())).thenReturn(Optional.of(lesson));

    var request =
        LessonBatchRequest.builder()
            .lessonId("lesson-1")
            .requestSteps(
                List.of(VerifyRequest.builder().stepId("step-1").answer("answer1").build()))
            .build();

    assertThrows(AppException.class, () -> learningService.verifyLesson(request));
  }

  @Test
  void verifyLesson_duplicateStepId_fail() {
    when(learningProgressService.getCurrentUser()).thenReturn(user);
    when(lessonRepository.findByIdAndStatus(any(), any())).thenReturn(Optional.of(lesson));

    var request =
        LessonBatchRequest.builder()
            .lessonId("lesson-1")
            .requestSteps(
                List.of(
                    VerifyRequest.builder().stepId("step-1").answer("answer1").build(),
                    VerifyRequest.builder().stepId("step-1").answer("answer2").build()))
            .build();

    assertThrows(AppException.class, () -> learningService.verifyLesson(request));
  }

  @Test
  void verifyLesson_wrongAnswer_fail() {
    when(learningProgressService.getCurrentUser()).thenReturn(user);
    when(lessonRepository.findByIdAndStatus(any(), any())).thenReturn(Optional.of(lesson));
    when(userLessonProgressRepository.findByUserAndLesson(any(), any()))
        .thenReturn(Optional.empty());

    var request =
        LessonBatchRequest.builder()
            .lessonId("lesson-1")
            .requestSteps(
                List.of(
                    VerifyRequest.builder().stepId("step-1").answer("wrong").build(),
                    VerifyRequest.builder().stepId("step-2").answer("answer2").build()))
            .build();

    assertThrows(AppException.class, () -> learningService.verifyLesson(request));
  }

  @Test
  void verifyLesson_lockedLesson_fail() {
    var progress =
        UserLessonProgress.builder()
            .lockedStatus(IsLocked.TRUE_LOCKED)
            .completedStatus(IsCompleted.FALSE)
            .build();

    when(learningProgressService.getCurrentUser()).thenReturn(user);
    when(lessonRepository.findByIdAndStatus(any(), any())).thenReturn(Optional.of(lesson));
    when(userLessonProgressRepository.findByUserAndLesson(any(), any()))
        .thenReturn(Optional.of(progress));

    var request =
        LessonBatchRequest.builder()
            .lessonId("lesson-1")
            .requestSteps(
                List.of(
                    VerifyRequest.builder().stepId("step-1").answer("answer1").build(),
                    VerifyRequest.builder().stepId("step-2").answer("answer2").build()))
            .build();

    assertThrows(AppException.class, () -> learningService.verifyLesson(request));
  }

  @Test
  void verifyLesson_alreadyCompleted_returnsZeroXp() {
    var progress =
        UserLessonProgress.builder()
            .lockedStatus(IsLocked.FALSE_LOCKED)
            .completedStatus(IsCompleted.TRUE)
            .build();

    when(learningProgressService.getCurrentUser()).thenReturn(user);
    when(lessonRepository.findByIdAndStatus(any(), any())).thenReturn(Optional.of(lesson));
    when(userLessonProgressRepository.findByUserAndLesson(any(), any()))
        .thenReturn(Optional.of(progress));

    var request =
        LessonBatchRequest.builder()
            .lessonId("lesson-1")
            .requestSteps(
                List.of(
                    VerifyRequest.builder().stepId("step-1").answer("answer1").build(),
                    VerifyRequest.builder().stepId("step-2").answer("answer2").build()))
            .build();

    var result = learningService.verifyLesson(request);
    assertThat(result.getTotalXpGained()).isZero();
    assertThat(result.isNewlyCompleted()).isFalse();
  }
}
