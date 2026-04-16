package com.example.identity_servive.service;

import com.example.identity_servive.dto.request.CodeRequest;
import com.example.identity_servive.dto.request.LessonBatchRequest;
import com.example.identity_servive.dto.request.VerifyRequest;
import com.example.identity_servive.dto.response.CodeResponse;
import com.example.identity_servive.dto.response.LessonBatchResponse;
import com.example.identity_servive.dto.response.StepVerificationResult;
import com.example.identity_servive.dto.response.VerifyResponse;
import com.example.identity_servive.entity.*;
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
    PistonAPISevice codeService; // Service gọi sang API bên ngoài để chạy code Python
    UserRepository userRepository;
    UserStepProgressRepository userStepProgressRepository;
    UserChapterProgressRepository userChapterProgressRepository;
    UserLessonProgressRepository userLessonProgressRepository;
    ObjectMapper objectMapper = new ObjectMapper(); // Công cụ để đọc/ghi dữ liệu JSON

    /**
     * HÀM CHÍNH: XỬ LÝ NỘP BÀI TẬP CỦA CẢ MỘT BÀI HỌC (LESSON)
     * Tư duy: Người dùng gửi lên danh sách câu trả lời của 10 câu, đúng hết mới tính điểm.
     */
    @Transactional // Nếu có bất kỳ lỗi nào xảy ra, toàn bộ quá trình sẽ được hủy bỏ (Rollback)
    public LessonBatchResponse verifyLesson(LessonBatchRequest request) {
        // 1. Xác định xem ai là người đang nộp bài
        User user = getCurrentUser();

        // 2. Tìm bài học (Lesson) trong database, không thấy thì báo lỗi
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

        // 3. Lấy toàn bộ danh sách các bước (Step) thuộc bài học này từ DB
        List<Step> steps = stepRepository.findByLessonId(request.getLessonId());

        // 4. BIẾN DANH SÁCH THÀNH MAP: Để tìm kiếm Step theo ID nhanh hơn (O(1)) thay vì dùng vòng lặp
        Map<String, Step> stepMap = steps.stream().collect(Collectors.toMap(Step::getId, step -> step));
        List<VerifyResponse> verifyResponses = new ArrayList<>();
        double totalXpGained = 0; // Biến dùng để cộng dồn điểm XP

        // 5. DUYỆT QUA CÁC CÂU TRẢ LỜI CỦA USER GỬI LÊN
        for (VerifyRequest answerReq : request.getRequestSteps()) {
            Step step = stepMap.get(answerReq.getStepId()); // Tìm thông tin Step gốc trong MapStepVerificationResult
            if(step == null) throw new AppException(ErrorCode.ID_NOT_EXISTED);

            // CHẤM ĐIỂM: Nếu chỉ cần 1 câu sai, ném lỗi INVALID_ANSWER ngay lập tức
            StepVerificationResult result = checkAnswer(step, answerReq.getAnswer());

            if(!result.isCorrect())
                throw new AppException(ErrorCode.INVALID_ANSWER);

            // Nếu đúng, tích lũy XP của câu đó vào tổng điểm bài học
            totalXpGained += step.getXp();
            verifyResponses.add(VerifyResponse.builder()
                    .isCorrect(true)
                    .earnedXp(step.getXp())
                    .correctAnswer(step.getData())
                    .userOutput(result.getLogs())
                    .build());
        }

        // 6. NẾU ĐÃ ĐI ĐẾN ĐÂY: Có nghĩa là đúng 100% câu hỏi trong bài
        // -> Đánh dấu hoàn thành cho từng Step (Hiện tích xanh)
        steps.forEach(step -> updateStepToCompleted(user, step));
        // -> Đánh dấu hoàn thành cho cả bài Lesson (Hiện tích xanh trên danh sách bài)
        updateLessonToCompleted(user, lesson);

        // 7. Cập nhật tổng XP vào tài khoản của người học và lưu lại
        user.setTotalXp(user.getTotalXp() + totalXpGained);
        userRepository.save(user);

        // 8. Kích hoạt logic tự động mở khóa bài học hoặc chương tiếp theo
        unlockNextLesson(user, lesson);

        // 9. Trả về kết quả cho Frontend thông báo thành công
        return LessonBatchResponse.builder()
                .totalXpGained(totalXpGained)
                .verifyResponses(verifyResponses)
                .isLessonCompleted(true)
                .build();
    }

    /**
     * BỘ MÁY CHẤM ĐIỂM (JUDGE): Quyết định xem một câu trả lời là Đúng hay Sai.
     */
    private StepVerificationResult checkAnswer(Step step, Object userAnswer) {
        try {
            // Đọc cột 'data' (chuỗi JSON) từ DB ra thành một Map để lấy đáp án đúng
            Map<String, Object> data = objectMapper.readValue(step.getData(), Map.class);
            String answerStr = (userAnswer != null) ? userAnswer.toString() : "";
            switch (step.getType()) {
                case QUIZ: // Nếu là trắc nghiệm
                    Object correctValue = data.get("correctValue"); // Lấy đáp án đúng trong cấu hình bài tập
                    return StepVerificationResult.builder()
                            .isCorrect(correctValue != null && userAnswer != null &&
                                    correctValue.toString().equals(userAnswer.toString()))
                            .expectedValue(correctValue)
                            .build();
                case CODE: // Nếu là bài tập lập trình
                    return verifyCodeOutput(data, answerStr);
                default: // Các loại INFO hoặc QUESTION đơn giản (chỉ cần có trả lời là đúng)
                    return StepVerificationResult.builder()
                            .isCorrect(userAnswer != null)
                            .build();
            }
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

        // Gửi code của user sang Piston API (máy chủ chạy code)
        CodeResponse codeResponse = codeService.executePythonCode(
                CodeRequest.builder().input(sourceCode).build()
        );
        if(codeResponse == null){
            return StepVerificationResult.builder()
                    .isCorrect(false)
                    .logs("Hệ thống thực thi code không phản hồi.")
                    .build();
        }

        // Lấy output thực tế từ máy chủ Piston
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

    /**
     * MỞ KHÓA BÀI HỌC TIẾP THEO
     */
    private void unlockNextLesson(User user, Lesson currentLesson){
        // Tìm xem bài học sau bài này (cùng Chapter, OrderIndex lớn hơn bài hiện tại)
        Optional<Lesson> nextLessonOpt = lessonRepository.findFirstByChapterAndOrderIndexGreaterThanOrderByOrderIndexAsc(
                currentLesson.getChapter(), currentLesson.getOrderIndex());

        if (nextLessonOpt.isPresent()) {
            Lesson nextLesson = nextLessonOpt.get();
            unlockLessonProgress(user, nextLesson); // Gỡ bỏ "ổ khóa" cho Lesson mới

            // Đồng thời mở luôn "ổ khóa" cho Step đầu tiên của bài đó để user vào học luôn
            nextLesson.getSteps().stream()
                    .min(Comparator.comparingInt(Step::getOrderIndex))
                    .ifPresent(firstStep -> unlockStep(user, firstStep));
        } else {
            // Nếu đây đã là bài cuối cùng của Chương, tiến hành mở khóa Chương (Chapter) mới
            unlockNextChapter(user, currentLesson.getChapter());
        }
    }

    /**
     * MỞ KHÓA CHƯƠNG TIẾP THEO
     */
    private void unlockNextChapter(User user, Chapter currentChapter){
        // Tìm chương tiếp theo của ngôn ngữ đang học
        Optional<Chapter> nextChapterOpt = chapterRepository.findFirstByLanguageAndOrderIndexGreaterThanOrderByOrderIndexAsc
                (currentChapter.getLanguage(), currentChapter.getOrderIndex());

        if(nextChapterOpt.isPresent()){
            Chapter nextChapter = nextChapterOpt.get();
            unlockChapterProgress(user, nextChapter); // Gỡ "ổ khóa" Chương

            // Mở khóa "dây chuyền" xuống bài đầu tiên và câu hỏi đầu tiên của Chương mới
            nextChapter.getLessons().stream()
                    .min(Comparator.comparingInt(Lesson::getOrderIndex))
                    .ifPresent(firstLesson -> {
                        unlockLessonProgress(user, firstLesson);
                        firstLesson.getSteps().stream()
                                .min(Comparator.comparingInt(Step::getOrderIndex))
                                .ifPresent(firstStep -> unlockStep(user, firstStep));
                    });
        } else {
            log.info("CHÚC MỪNG: User {} đã phá đảo khóa học!", user.getUsername());
        }
    }

    // --- CÁC HÀM TIỆN ÍCH CẬP NHẬT TRẠNG THÁI (DATABASE HELPERS) ---

    private void updateStepToCompleted(User user, Step step) {
        // Tìm bản ghi tiến độ, nếu chưa có thì tạo mới (Builder)
        UserStepProgress progress = userStepProgressRepository.findByUserAndStep(user, step)
                .orElseGet(() -> UserStepProgress.builder().user(user).step(step).build());
        progress.setLockedStatus(IsLocked.FALSE_LOCKED); // Mở khóa = FALSE
        progress.setCompletedStatus(IsCompleted.TRUE); // Hoàn thành = TRUE (Tích xanh)
        userStepProgressRepository.save(progress);
    }

    private void updateLessonToCompleted(User user, Lesson lesson) {
        UserLessonProgress progress = userLessonProgressRepository.findByUserAndLesson(user, lesson)
                .orElseGet(() -> UserLessonProgress.builder().user(user).lesson(lesson).build());
        progress.setLockedStatus(IsLocked.FALSE_LOCKED);
        progress.setCompletedStatus(IsCompleted.TRUE); // Hoàn thành bài học
        userLessonProgressRepository.save(progress);
    }

    private void unlockStep(User user, Step step) {
        UserStepProgress progress = userStepProgressRepository.findByUserAndStep(user, step)
                .orElseGet(() -> UserStepProgress.builder().user(user).step(step).build());
        progress.setLockedStatus(IsLocked.FALSE_LOCKED); // Đã gỡ khóa
        progress.setCompletedStatus(IsCompleted.FALSE); // Nhưng chưa học xong (Chưa tích xanh)
        userStepProgressRepository.save(progress);
    }

    private void unlockLessonProgress(User user, Lesson lesson) {
        UserLessonProgress progress = userLessonProgressRepository.findByUserAndLesson(user, lesson)
                .orElseGet(() -> UserLessonProgress.builder().user(user).lesson(lesson).build());
        progress.setLockedStatus(IsLocked.FALSE_LOCKED);
        progress.setCompletedStatus(IsCompleted.FALSE); // Mở bài mới để user thấy
        userLessonProgressRepository.save(progress);
    }

    private void unlockChapterProgress(User user, Chapter chapter) {
        UserChapterProgress progress = userChapterProgressRepository.findByUserAndChapter(user, chapter)
                .orElseGet(() -> UserChapterProgress.builder().user(user).chapter(chapter).build());
        progress.setLockedStatus(IsLocked.FALSE_LOCKED);
        progress.setCompletedStatus(IsCompleted.FALSE); // Mở chương mới
        userChapterProgressRepository.save(progress);
    }

    /**
     * LẤY USER HIỆN TẠI TỪ TOKEN (SPRING SECURITY)
     */
    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED); // Báo lỗi nếu chưa đăng nhập
        }
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}