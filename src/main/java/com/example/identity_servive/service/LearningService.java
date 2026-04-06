package com.example.identity_servive.service;

import com.example.identity_servive.dto.request.VerifyRequest;
import com.example.identity_servive.dto.response.VerifyResponse;
import com.example.identity_servive.entity.Step;
import com.example.identity_servive.entity.User;
import com.example.identity_servive.entity.UserProgress;
import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.Type;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j // Hỗ trợ ghi lại lịch sử hoạt động (Logging)
@Service // Đăng ký lớp này là một Service do Spring quản lý (Bean)
@RequiredArgsConstructor // Tự động tạo Constructor để tiêm (Inject) các Repository và Mapper vào
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LearningService {
    LessonRepository lessonRepository;
    ChapterRepository chapterRepository;
    StepRepository stepRepository;
    LanguageRepository languageRepository;
    UserProgressRepository userProgressRepository;
    UserRepository userRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public VerifyResponse verifyStep(VerifyRequest verifyRequest) {
        // 1. Lấy thông tin User đang đăng nhập từ hệ thống Security
        var context = SecurityContextHolder.getContext();

        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 2. Tìm bài tập (Step) cần kiểm tra
        Step step = stepRepository.findById(verifyRequest.getStepId())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

        Object dbCorrectAnswer = null;
        boolean isCorrect = false;
        double xpToGive = 0;

        try {
            // 3. Giải mã trường 'data' từ JSON String trong DB sang Map để lấy đáp án đúng
            Map<String, Object> data = objectMapper.readValue(step.getData(), Map.class);
            /**
             //2. Ví dụ thực tế Giả sử trong Database, cột data của một bài tập Quiz có nội dung như sau:JSON{
             //  "question": "Biến là gì?",
             //  "correctIndex": 2,
             //  "explanation": "Biến dùng để lưu trữ dữ liệu"
             //}
             //Khi dòng lệnh Map<String, Object> data = ... chạy xong, biến data trong Java sẽ tương đương với
             // :data.get("question") Trả về chuỗi "Biến là gì?" (Kiểu String)
             // .data.get("correctIndex")  Trả về giá trị 2 (Kiểu Integer/Object).
             // data.get("explanation") Trả về chuỗi "Biến dùng để..." (Kiểu String).**/
            // 4. Nếu là dạng bài tập trắc nghiệm (QUIZ)
            if (Type.QUIZ.equals(step.getType())) {
                dbCorrectAnswer = data.get("correctValue"); // Lấy index đáp án đúng (0, 1, 2...)

                // So sánh đáp án người dùng gửi (answer) với đáp án trong DB (correctIndex)
                if (dbCorrectAnswer != null && verifyRequest.getAnswer() != null &&
                        dbCorrectAnswer.toString().equals(verifyRequest.getAnswer().toString())) {
                    isCorrect = true;
                    xpToGive = step.getXp();
                }
            } else if (Type.QUESTION.equals(step.getType())) {
                if(verifyRequest.getAnswer() != null) {
                    isCorrect = true;
                    xpToGive = step.getXp();
                }

            } else if(Type.FILL_CODE.equals(step.getType())) {

            }
            else {
                // Nếu là dạng INFO hoặc bài học thông thường, xem như luôn đúng khi nhấn hoàn thành
                isCorrect = true;
                xpToGive = step.getXp();
            }

            // 5. Nếu làm đúng, lưu tiến độ học tập vào bảng UserProgress
            if (isCorrect) {
                UserProgress progress = UserProgress.builder()
                        .userID(user.getId()) // Lưu ID user
                        .step(step)           // Lưu bài tập vừa làm
                        .completedStatus(IsCompleted.TRUE) // Đánh dấu đã xong
                        .earnedXp(xpToGive)    // Ghi nhận số XP nhận được
                        .build();

                userProgressRepository.save(progress);
            }

            // 6. Trả về kết quả cho Client
            return VerifyResponse.builder()
                    .isCorrect(isCorrect)
                    .earnedXp(isCorrect ? xpToGive : 0)
                    .message(isCorrect ? "Chúc mừng! Bạn đã hoàn thành bài tập." : "Rất tiếc, đáp án chưa chính xác.")
                    .correctAnswer(isCorrect ? null : dbCorrectAnswer) // Nếu sai thì trả về đáp án đúng (tùy chọn)
                    .build();

        } catch (JsonProcessingException e) {
            // Lỗi xảy ra khi dữ liệu JSON trong cột data bị sai định dạng
            log.error("Lỗi xử lý tính điểm (JSON Parse Error): " + e.getMessage());
            throw new RuntimeException("Hệ thống không thể kiểm tra đáp án lúc này do lỗi định dạng dữ liệu.");
        }
    }
}
