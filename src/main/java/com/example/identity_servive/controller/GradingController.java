package com.example.identity_servive.controller;
//
//
//import com.example.identity_servive.dto.request.GradedRequest;
//import com.example.identity_servive.dto.response.GradedResponse;
//import com.example.identity_servive.service.GradedService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
///**
// * Controller xử lý chấm bài tự động sử dụng AI
// *
// * API endpoints:
// * - POST /api/v1/grading/grade → Chấm bài tự luận
// * - POST /api/v1/grading/grade-code → Chấm bài code
// * - POST /api/v1/grading/grade-mcq → Chấm bài trắc nghiệm
// */
//@RestController
//@RequestMapping("/api/v1/grading")
//@RequiredArgsConstructor
//@CrossOrigin("*")
//@Slf4j
//public class GradingController {
//
//    private final GradingService gradingService;
//
//    /**
//     * Chấm bài tự luận
//     *
//     * @param request Request chứa question, studentAnswer, modelAnswer, rubric
//     * @return Kết quả chấm với score, feedback, suggestions
//     */
//    @PostMapping("/grade")
//    public ResponseEntity<GradedResponse> gradeEssay(
//            @Valid @RequestBody GradedRequest request) {
//
//        log.info("Received grading request for assignment type: {}",
//                 request.getAssignmentType());
//
//        // Gọi service chấm bài
//        String aiResponse = gradingService.gradeEssay(
//            request.getQuestion(),
//            request.getStudentAnswer(),
//            request.getModelAnswer(),
//            request.getRubric(),
//            request.getMaxScore()
//        );
//
//        // Parse JSON response từ AI
//        GradedResponse response = gradingService.parseGradingResult(aiResponse);
//
//        log.info("Grading completed - Score: {}", response.getScore());
//
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * Chấm bài code lập trình
//     *
//     * @param request Request chứa problemDescription, studentCode, expectedOutput
//     * @return Kết quả chấm với score, feedback về code quality
//     */
//    @PostMapping("/grade-code")
//    public ResponseEntity<GradedResponse> gradeCode(
//            @Valid @RequestBody GradedRequest request) {
//
//        log.info("Received code grading request for language: {}",
//                 request.getAssignmentType());
//
//        // Gọi service chấm code
//        String aiResponse = gradingService.gradeCode(
//            request.getQuestion(),          // Problem description
//            request.getModelAnswer(),       // Expected output/test cases
//            request.getStudentAnswer(),     // Student's code
//            request.getAssignmentType(),    // Programming language
//            request.getMaxScore()
//        );
//
//        GradedResponse response = gradingService.parseGradingResult(aiResponse);
//
//        log.info("Code grading completed - Score: {}", response.getScore());
//
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * Chấm bài trắc nghiệm (MCQ)
//     *
//     * @param request Request chứa questions, studentAnswers, correctAnswers
//     * @return Kết quả chấm với score và danh sách câu đúng/sai
//     */
//    @PostMapping("/grade-mcq")
//    public ResponseEntity<GradedResponse> gradeMCQ(
//            @Valid @RequestBody GradedRequest request) {
//
//        log.info("Received MCQ grading request");
//
//        String aiResponse = gradingService.gradeMCQ(
//            request.getQuestion(),          // List of questions
//            request.getStudentAnswer(),     // Student's answers
//            request.getModelAnswer(),       // Correct answers
//            request.getMaxScore()
//        );
//
//        GradedResponse response = gradingService.parseGradingResult(aiResponse);
//
//        log.info("MCQ grading completed - Score: {}", response.getScore());
//
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * Health check endpoint cho grading service
//     */
//    @GetMapping("/health")
//    public ResponseEntity<String> healthCheck() {
//        return ResponseEntity.ok("Grading service is running!");
//    }
//}
