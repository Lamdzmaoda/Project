package com.example.identity_servive.service.learning;

import com.example.identity_servive.dto.request.learningRequest.LessonBatchRequest;
import com.example.identity_servive.dto.request.learningRequest.VerifyRequest;
import com.example.identity_servive.dto.response.learningResponse.LessonBatchResponse;
import com.example.identity_servive.dto.response.learningResponse.StepVerificationResult;
import com.example.identity_servive.dto.response.learningResponse.VerifyResponse;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Lesson;
import com.example.identity_servive.entity.learning.Step;
import com.example.identity_servive.entity.progress.UserLessonProgress;
import com.example.identity_servive.enums.ContentStatus;
import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.repository.Progress.UserLessonProgressRepository;
import com.example.identity_servive.repository.learning.LessonRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ĐÂY LÀ "TRÁI TIM" CỦA HỆ THỐNG HỌC TẬP
 * Nhiệm vụ: Chấm điểm bài tập, cộng XP và tự động mở bài mới khi hoàn thành.
 */
@Slf4j // Cho phép dùng lệnh log.info() để ghi nhật ký hoạt động
@Service // Đánh dấu lớp này là một Service để Spring quản lý
@RequiredArgsConstructor // Tự động tạo constructor cho các biến 'final' (Dependency Injection)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Mặc định các biến là private final
public class LearningService {
    // --- KHAI BÁO CÁC "KHO DỮ LIỆU" (REPOSITORIES) ---
    LessonRepository lessonRepository;
    UserLessonProgressRepository userLessonProgressRepository;
    LearningProgressService learningProgressService;

    @Transactional
    public LessonBatchResponse verifyLesson(LessonBatchRequest request) {
        // 1. Xác định xem ai là người đang nộp bài
        User user = learningProgressService.getCurrentUser();
        // 2. Tìm bài học (Lesson) trong database, không thấy thì báo lỗi
        Lesson lesson = lessonRepository.findByIdAndStatus(request.getLessonId(), ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        // 3. Lấy danh sách steps từ lesson (đã có sẵn nhờ quan hệ JPA)
        Set<Step> lessonSteps = lesson.getSteps().stream().filter(step -> ContentStatus.ACTIVE.equals(step.getStatus())).collect(Collectors.toSet());
        // KIỂM TRA THIẾU 1: Số lượng step gửi lên phải khớp với số lượng step của bài học
        if (request.getRequestSteps().size() != lessonSteps.size()) {
            throw new AppException(ErrorCode.LESSON_INCOMPLETE, "Số lượng câu trả lời không khớp với bài học.");
        }

        Optional<UserLessonProgress> progressOpt = userLessonProgressRepository.findByUserAndLesson(user, lesson);
        boolean isAlreadyComplete = progressOpt
                .map(p -> IsCompleted.TRUE.equals(p.getCompletedStatus())).orElse(false);
        if (progressOpt.map(p -> p.getLockedStatus()
                .equals(IsLocked.TRUE_LOCKED)).orElse(true)) {
            throw new AppException(ErrorCode.LESSON_LOCKED, "Bài học đã bị khóa.");
        }
        // 4. Biến danh sách thành Map để tra cứu nhanh
        Map<String, Step> stepMap = lessonSteps.stream().collect(Collectors.toMap(Step::getId, step -> step));
        List<VerifyResponse> verifyResponses = new ArrayList<>();
        long totalXpGained = 0; // Biến dùng để cộng dồn điểm XP

        // KIỂM TRA THIẾU 2: Tránh việc gửi trùng ID Step để gian lận
        Set<String> processedStepIds = new HashSet<>();
        long progressPercentage = 0;
        // 5. DUYỆT QUA CÁC CÂU TRẢ LỜI CỦA USER GỬI LÊN
        for (VerifyRequest answerReq : request.getRequestSteps()) {
            Step step = stepMap.get(answerReq.getStepId()); // Tìm thông tin Step gốc trong MapStepVerificationResult
            if (processedStepIds.contains(answerReq.getStepId())) {
                throw new AppException(ErrorCode.INVALID_KEY, "Phát hiện ID Step bị trùng lặp trong yêu cầu.");
            }
            if (step == null) throw new AppException(ErrorCode.ID_NOT_EXISTED, "Step không thuộc bài học này.");

            StepVerificationResult result = checkAnswer(step, answerReq.getAnswer());
            if (!result.isCorrect()) throw new AppException(ErrorCode.INVALID_ANSWER, result.getLogs());
            // Nếu đúng, tích lũy XP của câu đó vào tổng điểm bài học

            long earnedXp = 0;
            if (!isAlreadyComplete) {
                earnedXp = lesson.getXp();
                totalXpGained += earnedXp;
            }
            verifyResponses.add(VerifyResponse.builder()
                    .isCorrect(true)
                    .earnedXp(earnedXp)
                    .correctAnswer(result.getExpectedValue() != null ? result.getExpectedValue().toString() : "")
                    .userOutput(result.getLogs())
                    .build());
            processedStepIds.add(step.getId());
        }
        boolean isNewlyCompleted = false;
        // 6. NẾU ĐÃ ĐI ĐẾN ĐÂY: Có nghĩa là đúng 100% câu hỏi trong bài
        // -> Đánh dấu hoàn thành cho từng Step (Hiện tích xanh)
        if (!isAlreadyComplete) {
            learningProgressService.completeLesson(user, lesson , totalXpGained);
            // -> Đánh dấu hoàn thành cho cả bài Lesson (Hiện tích xanh trên danh sách bài)
            // 7. Cập nhật tổng XP vào tài khoản của người học và lưu lại

            isNewlyCompleted = true;
        }
        return LessonBatchResponse.builder()
                .totalXpGained(totalXpGained)
                .verifyResponses(verifyResponses)
                .progressPercentage(progressPercentage)
                .isNewlyCompleted(isNewlyCompleted)
                .build();
    }
    private StepVerificationResult checkAnswer(Step step, Object userAnswer) {
        try {
            // Đọc cột 'data' (chuỗi JSON) từ DB ra thành một Map để lấy đáp án đúng
            Map<String, Object> data = step.getData();
            String answerStr = (userAnswer != null) ? userAnswer.toString() : "";
            return switch (step.getType()) {
                case QUIZ,CODE-> {
                    Object correctValue = data.get("correctValue"); // Lấy đáp án đúng trong cấu hình bài tập
                    yield StepVerificationResult.builder()
                            .isCorrect(correctValue != null && userAnswer != null &&
                                    normalizeAnswer(correctValue.toString()).equals(normalizeAnswer(userAnswer.toString())))
                            .expectedValue(correctValue)
                            .build(); // Lấy đáp án đúng trong cấu hình bài tập
                }
                default -> // Các loại INFO hoặc QUESTION đơn giản (chỉ cần có trả lời là đúng)
                        StepVerificationResult.builder()
                                .isCorrect(userAnswer != null && !answerStr.isEmpty())
                                .build();
            };
        }catch (Exception e) {
            log.error("Lỗi khi chấm điểm Step ID {}: {}", step.getId(), e.getMessage());
            return StepVerificationResult.builder()
                    .isCorrect(false)
                    .logs("Lỗi hệ thống khi kiểm tra đáp án.")
                    .build();
        }
    }
    private String normalizeAnswer(String raw){
        if (raw == null) return "";
        return raw.trim().toLowerCase();
    }
}
