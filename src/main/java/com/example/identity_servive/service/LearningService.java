package com.example.identity_servive.service;

import com.example.identity_servive.dto.request.CodeRequest;
import com.example.identity_servive.dto.request.LessonBatchRequest;
import com.example.identity_servive.dto.request.VerifyRequest;
import com.example.identity_servive.dto.response.CodeResponse;
import com.example.identity_servive.dto.response.LessonBatchResponse;
import com.example.identity_servive.dto.response.StepVerificationResult;
import com.example.identity_servive.dto.response.VerifyResponse;
import com.example.identity_servive.entity.*;
import com.example.identity_servive.enums.ContentStatus;
import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
import com.example.identity_servive.enums.Status;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    ChapterRepository chapterRepository;
    StepRepository stepRepository;
    Judge0APIService codeService; // Service gọi sang API bên ngoài để chạy code Python
    UserRepository userRepository;
    UserStepProgressRepository userStepProgressRepository;
    UserChapterProgressRepository userChapterProgressRepository;
    UserLessonProgressRepository userLessonProgressRepository;

    /**
     * HÀM CHÍNH: XỬ LÝ NỘP BÀI TẬP CỦA CẢ MỘT BÀI HỌC (LESSON)
     * Tư duy: Người dùng gửi lên danh sách câu trả lời của 10 câu, đúng hết mới tính điểm.
     */

    @Transactional // Nếu có bất kỳ lỗi nào xảy ra, toàn bộ quá trình sẽ được hủy bỏ (Rollback)
    public LessonBatchResponse verifyLesson(LessonBatchRequest request) {
        // 1. Xác định xem ai là người đang nộp bài
        User user = getCurrentUser();

        // 2. Tìm bài học (Lesson) trong database, không thấy thì báo lỗi
        Lesson lesson = lessonRepository.findByIdAndStatus(request.getLessonId(), ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

        // 3. Lấy danh sách steps từ lesson (đã có sẵn nhờ quan hệ JPA)
        Set<Step> lessonSteps = lesson.getSteps().stream().filter(step -> ContentStatus.ACTIVE.equals(step.getStatus())).collect(Collectors.toSet());

        // KIỂM TRA THIẾU 1: Số lượng step gửi lên phải khớp với số lượng step của bài học
        if (request.getRequestSteps().size() != lessonSteps.size()) {
            throw new AppException(ErrorCode.LESSON_INCOMPLETE, "Số lượng câu trả lời không khớp với bài học.");
        }

        boolean isAlreadyComplete = userLessonProgressRepository.findByUserAndLesson(user, lesson)
                .map(p -> p.getCompletedStatus() == IsCompleted.TRUE).orElse(false);
        if(userLessonProgressRepository.findByUserAndLesson(user, lesson).map(p -> p.getLockedStatus()
                == IsLocked.TRUE_LOCKED).orElse(true)){
            throw new AppException(ErrorCode.STEP_LOCKED);
        }
        // 4. Biến danh sách thành Map để tra cứu nhanh
        Map<String, Step> stepMap = lessonSteps.stream().collect(Collectors.toMap(Step::getId, step -> step));
        List<VerifyResponse> verifyResponses = new ArrayList<>();
        double totalXpGained = 0.0; // Biến dùng để cộng dồn điểm XP

        // KIỂM TRA THIẾU 2: Tránh việc gửi trùng ID Step để gian lận
        Set<String> processedStepIds = new HashSet<>();
        double progressPercentage = 0.0;
        // 5. DUYỆT QUA CÁC CÂU TRẢ LỜI CỦA USER GỬI LÊN
        for (VerifyRequest answerReq : request.getRequestSteps()) {
            if (processedStepIds.contains(answerReq.getStepId())) {
                throw new AppException(ErrorCode.INVALID_KEY, "Phát hiện ID Step bị trùng lặp trong yêu cầu.");
            }

            Step step = stepMap.get(answerReq.getStepId()); // Tìm thông tin Step gốc trong MapStepVerificationResult
            if(step == null) throw new AppException(ErrorCode.ID_NOT_EXISTED, "Step không thuộc bài học này.");

            // CHẤM ĐIỂM: Nếu chỉ cần 1 câu sai, ném lỗi INVALID_ANSWER ngay lập tức
            StepVerificationResult result = checkAnswer(step, answerReq.getAnswer());

            if(!result.isCorrect())
                throw new AppException(ErrorCode.INVALID_ANSWER, result.getLogs());
            // Nếu đúng, tích lũy XP của câu đó vào tổng điểm bài học
            double earnedXp = 0;
            if(!isAlreadyComplete) {
                earnedXp = step.getXp();
                totalXpGained += earnedXp;
            }
                verifyResponses.add(VerifyResponse.builder()
                                .isCorrect(true)
                                .earnedXp(earnedXp)
                                .correctAnswer(result.getExpectedValue() != null ? result.getExpectedValue().toString() : "")
                                .userOutput(result.getLogs())
                                .build());
            processedStepIds.add(answerReq.getStepId());
            progressPercentage = (double) verifyResponses.size() / lessonSteps.size() * 100;;
        }

        // 6. NẾU ĐÃ ĐI ĐẾN ĐÂY: Có nghĩa là đúng 100% câu hỏi trong bài
        // -> Đánh dấu hoàn thành cho từng Step (Hiện tích xanh)
        if(!isAlreadyComplete) {
            List<UserStepProgress> listToSave = new ArrayList<>();
            lessonSteps.forEach(step -> listToSave.add(updateStepToCompleted(user, step)));
            // -> Đánh dấu hoàn thành cho cả bài Lesson (Hiện tích xanh trên danh sách bài)
            updateLessonToCompleted(user, lesson);

            // 7. Cập nhật tổng XP vào tài khoản của người học và lưu lại
            user.setTotalXp(user.getTotalXp() + totalXpGained);
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);
            LocalDate lastActivity = user.getLastActivityDate();
            if (lastActivity == null || lastActivity.isBefore(yesterday)) {
                user.setStreak(1); // Mới học hoặc đứt chuỗi
            } else if (lastActivity.equals(yesterday)) {
                user.setStreak(user.getStreak() + 1); // Học liên tiếp
            }
            user.setLastActivityDate(today);
            userRepository.save(user);
            userStepProgressRepository.saveAll(listToSave);

            // 8. Kích hoạt logic tự động mở khóa bài học hoặc chương tiếp theo
            unlockNextLesson(user, lesson);
        }

        // 9. Trả về kết quả cho Frontend thông báo thành công
        return LessonBatchResponse.builder()
                .totalXpGained(totalXpGained)
                .verifyResponses(verifyResponses)
                .progressPercentage(progressPercentage)
                .isLessonCompleted(true)
                .build();
    }

    /**
     * BỘ MÁY CHẤM ĐIỂM (JUDGE): Quyết định xem một câu trả lời là Đúng hay Sai.
     */
    private StepVerificationResult checkAnswer(Step step, Object userAnswer) {
        try {
            // Đọc cột 'data' (chuỗi JSON) từ DB ra thành một Map để lấy đáp án đúng
            Map<String, Object> data = step.getData();
            String answerStr = (userAnswer != null) ? userAnswer.toString() : "";
            return switch (step.getType()) {
                case QUIZ -> {
                    Object correctValue = data.get("correctValue"); // Lấy đáp án đúng trong cấu hình bài tập
                    yield StepVerificationResult.builder()
                            .isCorrect(correctValue != null && userAnswer != null &&
                                    correctValue.toString().equals(userAnswer.toString()))
                            .expectedValue(correctValue)
                            .build(); // Lấy đáp án đúng trong cấu hình bài tập
                }
                case CODE -> // Nếu là bài tập lập trình
                        verifyCodeOutput(data, answerStr);
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

    /**
     * CHẤM ĐIỂM LẬP TRÌNH: Gửi code Python lên API và so sánh kết quả in ra (Output).
     */
    private StepVerificationResult verifyCodeOutput(Map<String, Object> data, String sourceCode) {
        // Lấy kết quả mà bài tập mong đợi (Ví dụ bài yêu cầu in ra 'Hello World')
        Object expectedOutputObj = data.get("expectedOutput");
        String expectedOutput = expectedOutputObj != null ? expectedOutputObj.toString().trim() : "";

        // Gửi code của user sang Judge0 API
        CodeResponse codeResponse = codeService.executePythonCode(
                CodeRequest.builder().input(sourceCode).build()
        );
        if(codeResponse == null){
            return StepVerificationResult.builder()
                    .isCorrect(false)
                    .logs("Hệ thống thực thi code không phản hồi.")
                    .build();
        }
        // Lấy output thực tế từ máy chủ
        String actualOutput = (codeResponse.getOutput() != null) ? codeResponse.getOutput().trim() : "";
        if (codeResponse.getStatus() == Status.ERROR){
            return StepVerificationResult.builder()
                    .isCorrect(false)
                    .logs(codeResponse.getMessageVn())
                    .build();
        }
        boolean isCorrect = expectedOutput.equalsIgnoreCase(actualOutput);
        // Nếu API chạy thành công, so sánh output thực tế với output mong đợi
        return StepVerificationResult.builder()
                .isCorrect(isCorrect)
                .logs(actualOutput) // Trả về toàn bộ output để hiển thị lên Console
                .expectedValue(expectedOutput)
                .build();
    }

    // --- CƠ CHẾ MỞ KHÓA TỰ ĐỘNG (AUTO-UNLOCK) ---

    private void unlockNextLesson(User user, Lesson currentLesson){
        Optional<Lesson> nextLessonOpt = lessonRepository.findFirstByChapterAndStatusAndOrderIndexGreaterThanOrderByOrderIndexAsc(
                currentLesson.getChapter(),ContentStatus.ACTIVE , currentLesson.getOrderIndex());

        if (nextLessonOpt.isPresent()) {
            Lesson nextLesson = nextLessonOpt.get();
            unlockLessonProgress(user, nextLesson);
            nextLesson.getSteps().stream()
                    .filter(step -> ContentStatus.ACTIVE.equals(step.getStatus()))
                    .min(Comparator.comparingInt(Step::getOrderIndex))
                    .ifPresent(firstStep -> unlockStep(user, firstStep));
        } else {
            // Nếu đây đã là bài cuối cùng của Chương, tiến hành mở khóa Chương (Chapter) mới và đánh dấu hoàn thành
            updateChapterToCompleted(user, currentLesson.getChapter());
            unlockNextChapter(user, currentLesson.getChapter());
        }
    }

    private void unlockNextChapter(User user, Chapter currentChapter){
        Optional<Chapter> nextChapterOpt = chapterRepository.findFirstByLanguageAndStatusAndOrderIndexGreaterThanOrderByOrderIndexAsc(
                currentChapter.getLanguage(), ContentStatus.ACTIVE, currentChapter.getOrderIndex());
        if(nextChapterOpt.isPresent()){
            Chapter nextChapter = nextChapterOpt.get();
            unlockChapterProgress(user, nextChapter);
            nextChapter.getLessons().stream()
                    .filter(lesson -> ContentStatus.ACTIVE.equals(lesson.getStatus()))
                    .min(Comparator.comparingInt(Lesson::getOrderIndex))
                    .ifPresent(firstLesson -> {
                        unlockLessonProgress(user, firstLesson);
                        firstLesson.getSteps().stream()
                                .filter(step -> ContentStatus.ACTIVE.equals(step.getStatus()))
                                .min(Comparator.comparingInt(Step::getOrderIndex))
                                .ifPresent(firstStep -> unlockStep(user, firstStep));
                    });
        }
    }

    // --- CÁC HÀM TIỆN ÍCH CẬP NHẬT TRẠNG THÁI (DATABASE HELPERS) ---

    private UserStepProgress updateStepToCompleted(User user, Step step) {
        UserStepProgress progress = userStepProgressRepository.findByUserAndStep(user, step)
                .orElseGet(() -> UserStepProgress.builder().user(user).step(step).build());
        progress.setLockedStatus(IsLocked.FALSE_LOCKED);
        progress.setCompletedStatus(IsCompleted.TRUE);
        return progress;
    }

    private void updateLessonToCompleted(User user, Lesson lesson) {
        UserLessonProgress progress = userLessonProgressRepository.findByUserAndLesson(user, lesson)
                .orElseGet(() -> UserLessonProgress.builder().user(user).lesson(lesson).build());
        progress.setLockedStatus(IsLocked.FALSE_LOCKED);
        progress.setCompletedStatus(IsCompleted.TRUE);
        userLessonProgressRepository.save(progress);
    }

    private void updateChapterToCompleted(User user, Chapter chapter) {
        UserChapterProgress progress = userChapterProgressRepository.findByUserAndChapter(user, chapter)
                .orElseGet(() -> UserChapterProgress.builder().user(user).chapter(chapter).build());
        progress.setLockedStatus(IsLocked.FALSE_LOCKED);
        progress.setCompletedStatus(IsCompleted.TRUE);
        userChapterProgressRepository.save(progress);
    }

    private void unlockStep(User user, Step step) {
        UserStepProgress progress = userStepProgressRepository.findByUserAndStep(user, step)
                .orElseGet(() -> UserStepProgress.builder().user(user).step(step).build());
        progress.setLockedStatus(IsLocked.FALSE_LOCKED);
        progress.setCompletedStatus(IsCompleted.FALSE);
        userStepProgressRepository.save(progress);
    }

    private void unlockLessonProgress(User user, Lesson lesson) {
        UserLessonProgress progress = userLessonProgressRepository.findByUserAndLesson(user, lesson)
                .orElseGet(() -> UserLessonProgress.builder().user(user).lesson(lesson).build());
        progress.setLockedStatus(IsLocked.FALSE_LOCKED);
        progress.setCompletedStatus(IsCompleted.FALSE);
        userLessonProgressRepository.save(progress);
    }

    private void unlockChapterProgress(User user, Chapter chapter) {
        UserChapterProgress progress = userChapterProgressRepository.findByUserAndChapter(user, chapter)
                .orElseGet(() -> UserChapterProgress.builder().user(user).chapter(chapter).build());
        progress.setLockedStatus(IsLocked.FALSE_LOCKED);
        progress.setCompletedStatus(IsCompleted.FALSE);
        userChapterProgressRepository.save(progress);
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
    @Transactional
    protected void initializeLearningProgressForLanguage(Language language, User user){
        Chapter firstChapter = chapterRepository.findFirstByLanguageAndStatusOrderByOrderIndexAsc(language, ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_UNDER_CONSTRUCTION));
        UserChapterProgress userChapterProgress = userChapterProgressRepository.findByUserAndChapter (user, firstChapter)
                .orElseGet(() -> UserChapterProgress.builder()
                        .user(user)
                        .chapter(firstChapter)
                        .completedStatus(IsCompleted.FALSE)
                        .build());

        userChapterProgress.setLockedStatus(IsLocked.FALSE_LOCKED);
        userChapterProgressRepository.save(userChapterProgress);

        Lesson firstLesson = lessonRepository.findFirstByChapterAndStatusOrderByOrderIndexAsc(firstChapter, ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_UNDER_CONSTRUCTION));

        UserLessonProgress userLessonProgress = userLessonProgressRepository.findByUserAndLesson(user, firstLesson)
                .orElseGet(() -> UserLessonProgress.builder()
                        .user(user)
                        .lesson(firstLesson)
                        .completedStatus(IsCompleted.FALSE)
                        .build());
        userLessonProgress.setLockedStatus(IsLocked.FALSE_LOCKED);
        userLessonProgressRepository.save(userLessonProgress);

        Step firstStep = stepRepository.findFirstByLessonIdAndStatusOrderByOrderIndexAsc(firstLesson.getId(), ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_UNDER_CONSTRUCTION));
        UserStepProgress userStepProgress = userStepProgressRepository.findByUserAndStep(user, firstStep)
                .orElseGet(() -> UserStepProgress.builder()
                        .user(user)
                        .step(firstStep)
                        .completedStatus(IsCompleted.FALSE)
                        .build());
        userStepProgress.setLockedStatus(IsLocked.FALSE_LOCKED);
        userStepProgressRepository.save(userStepProgress);
    }
}
