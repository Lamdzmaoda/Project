/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.identity_servive.dto.request.ai.CodeRequest;
import com.example.identity_servive.dto.response.ai.CodeResponse;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Lesson;
import com.example.identity_servive.entity.learning.Problem;
import com.example.identity_servive.enums.Status;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.repository.Progress.SubmissionRepository;
import com.example.identity_servive.repository.learning.LessonRepository;
import com.example.identity_servive.repository.learning.ProblemRepository;
import com.example.identity_servive.service.learning.LearningProgressService;
import com.example.identity_servive.service.learning.PracticeService;
import com.example.identity_servive.service.system.Judge0APIService;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class PracticeServiceTest {

  @Autowired private PracticeService practiceService;
  @MockitoBean private ProblemRepository problemRepository;
  @MockitoBean private Judge0APIService judge0APIService;
  @MockitoBean private LearningProgressService learningProgressService;
  @MockitoBean private SubmissionRepository submissionRepository;
  @MockitoBean private LessonRepository lessonRepository;

  private User user;
  private Problem problem;
  private Lesson lesson;

  @BeforeEach
  void initData() {
    user = User.builder().id("user-1").username("testuser").email("test@test.com").build();
    lesson =
        Lesson.builder()
            .id("lesson-1")
            .title("Lesson 1")
            .xp(100L)
            .problems(new HashSet<>())
            .build();
    problem =
        Problem.builder()
            .id("problem-1")
            .title("Problem 1")
            .lesson(lesson)
            .expectedOutput("5")
            .build();
    lesson.getProblems().add(problem);
  }

  @Test
  void summit_success() {
    CodeResponse execResult = CodeResponse.builder().output("5").status(Status.SUCCESS).build();
    when(problemRepository.findById(any())).thenReturn(Optional.of(problem));
    when(judge0APIService.executePythonCode(any(CodeRequest.class))).thenReturn(execResult);
    when(learningProgressService.getCurrentUser()).thenReturn(user);

    var result = practiceService.submit("problem-1", "print(5)");
    assertThat(result).isNotNull();
    assertThat(result.getPassed()).isTrue();
  }

  @Test
  void summit_problemNotFound_fail() {
    when(problemRepository.findById(any())).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> practiceService.submit("invalid", "code"));
  }

  @Test
  void complete_notAllPassed_fail() {
    when(lessonRepository.findById(any())).thenReturn(Optional.of(lesson));
    when(learningProgressService.getCurrentUser()).thenReturn(user);
    when(submissionRepository.findFirstByUserAndProblemOrderByCreatedAtDesc(any(), any()))
        .thenReturn(Optional.empty());

    var result = practiceService.complete("lesson-1");
    assertThat(result.isPassed()).isFalse();
  }
}
