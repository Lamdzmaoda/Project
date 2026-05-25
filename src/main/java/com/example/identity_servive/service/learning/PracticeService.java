/* (C)2026 */
package com.example.identity_servive.service.learning;

import com.example.identity_servive.dto.request.ai.CodeRequest;
import com.example.identity_servive.dto.response.ai.CodeResponse;
import com.example.identity_servive.dto.response.learningResponse.PracticeSubmitResponse;
import com.example.identity_servive.dto.response.learningResponse.ProblemConditionResponse;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Lesson;
import com.example.identity_servive.entity.learning.Problem;
import com.example.identity_servive.entity.learning.ProblemCondition;
import com.example.identity_servive.entity.progress.Submission;
import com.example.identity_servive.enums.Status;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.repository.Progress.SubmissionRepository;
import com.example.identity_servive.repository.learning.LessonRepository;
import com.example.identity_servive.repository.learning.ProblemRepository;
import com.example.identity_servive.service.system.Judge0APIService;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j // Cho phép dùng lệnh log.info() để ghi nhật ký hoạt động
@Service // Đánh dấu lớp này là một Service để Spring quản lý
@RequiredArgsConstructor // Tự động tạo constructor cho các biến 'final' (Dependency Injection)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Mặc định các biến là private final
public class PracticeService {
  ProblemRepository problemRepository;
  Judge0APIService judge0APIService;
  LearningProgressService learningProgressService;
  SubmissionRepository submissionRepository;
  LessonRepository lessonRepository;

  @Transactional
  public PracticeSubmitResponse complete(String lessonId) {
    Lesson lesson =
        lessonRepository
            .findById(lessonId)
            .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
    User user = learningProgressService.getCurrentUser();

    // BOUNDARY CHECK: Lesson không có bài tập nào → không cho phép complete để tính XP
    // (Java's Stream.allMatch() trả về true với empty stream → user có thể lash XP free)
    if (lesson.getProblems() == null || lesson.getProblems().isEmpty()) {
      return PracticeSubmitResponse.builder()
          .passed(false)
          .xp(0)
          .message("Lesson này chưa có bài tập nào.")
          .build();
    }

    boolean allPassed =
        lesson.getProblems().stream()
            .allMatch(
                p ->
                    submissionRepository
                        .findFirstByUserAndProblemOrderByCreatedAtDesc(user, p)
                        .map(Submission::isPassed)
                        .orElse(false));
    if (!allPassed) {
      return PracticeSubmitResponse.builder()
          .passed(false)
          .xp(0)
          .message("Chưa hoàn thành tất cả bài tập trong lesson")
          .build();
    }
    learningProgressService.completeLesson(user, lesson, lesson.getXp());
    return PracticeSubmitResponse.builder()
        .passed(true)
        .message("Hoàn thành tất cả! Nhận " + lesson.getXp() + " XP.")
        .xp(lesson.getXp())
        .build();
  }

  public CodeResponse submit(String problemId, String code) {
    Problem problem =
        problemRepository
            .findById(problemId)
            .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

    CodeResponse result = judge0APIService.executePythonCode(new CodeRequest(code));

    result.setExpected(problem.getExpectedOutput());
    boolean outputPassed = false;
    if (result.getStatus() == Status.SUCCESS && problem.getExpectedOutput() != null) {
      String actual = result.getOutput() != null ? result.getOutput().strip() : "";
      outputPassed = actual.equals(problem.getExpectedOutput().strip());
      result.setPassed(outputPassed);
    }
    // 3. Scan conditions trong code user
    List<ProblemConditionResponse> conditionResults = new ArrayList<>();
    boolean allConditionsPassed = true;
    if (problem.getConditions() != null) {
      for (ProblemCondition cond : problem.getConditions()) {
        boolean condPassed = code.contains(cond.getExpectedCode());
        if (!condPassed) allConditionsPassed = false;
        conditionResults.add(
            ProblemConditionResponse.builder()
                .expectedCode(cond.getExpectedCode())
                .hint(cond.getHint())
                .orderIndex(cond.getOrderIndex())
                .passed(condPassed)
                .build());
      }
    }
    boolean overallPassed = outputPassed && allConditionsPassed;
    result.setPassed(overallPassed);
    User user = learningProgressService.getCurrentUser();
    Submission submission =
        Submission.builder()
            .user(user)
            .lesson(problem.getLesson())
            .problem(problem)
            .code(code)
            .output(result.getOutput())
            .passed(overallPassed)
            .build();
    submissionRepository.save(submission);

    // 6. Gắn conditions vào response
    result.setConditions(conditionResults);
    return result;
  }
}
