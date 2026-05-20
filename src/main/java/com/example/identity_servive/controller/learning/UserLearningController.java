package com.example.identity_servive.controller.learning;

import com.example.identity_servive.dto.request.learningRequest.LessonBatchRequest;
import com.example.identity_servive.dto.request.learningRequest.PracticeSubmitRequest;
import com.example.identity_servive.dto.response.ApiResponse;
import com.example.identity_servive.dto.response.ai.CodeResponse;
import com.example.identity_servive.dto.response.learningResponse.*;
import com.example.identity_servive.dto.response.progress.LearingProgressResponse;
import com.example.identity_servive.dto.response.progress.UserChapterResponse;
import com.example.identity_servive.service.learning.CourseService;
import com.example.identity_servive.service.learning.LearningProgressService;
import com.example.identity_servive.service.learning.LearningService;
import com.example.identity_servive.service.learning.PracticeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j // Hỗ trợ ghi log để theo dõi luồng dữ liệu
@RestController // Đánh dấu là REST Controller, tự động chuyển kết quả trả về thành JSON
@RequestMapping("/my-learning") // Định nghĩa đường dẫn gốc cho các API trong class này là /permissions
@RequiredArgsConstructor // Tự động tạo Constructor để Inject PermissionService
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserLearningController {
    CourseService courseService;
    LearningService learningService;
    PracticeService practiceService;
    private final RestClient.Builder builder;
    private final LearningProgressService learningProgressService;

    @GetMapping("/lessons/{lessonId}/steps")
    ApiResponse<List<StepResponse>> getStepByLessonId(@PathVariable("lessonId") String lessonId){
        return ApiResponse.<List<StepResponse>>builder()
                .result(courseService.getStepByLesson(lessonId))
                .build();
    }
    @GetMapping("/problems/{lessonId}/problems")
    ApiResponse<List<ProblemResponse>> getProblem(@PathVariable("lessonId") String lessonId) {
        return ApiResponse.<List<ProblemResponse>>builder()
                .result(courseService.getProblemByLesson(lessonId))
                .build();
    }
    @GetMapping("/chapters/{chapterId}/lessons")
    ApiResponse<List<LessonResponse>> getLessonByChapterId(@PathVariable("chapterId") String chapterId){
        return ApiResponse.<List<LessonResponse>>builder()
                .result(courseService.getLessonByChapter(chapterId))
                .build();
    }
    @GetMapping("/{languageName}/chapters")
    ApiResponse<List<ChapterResponse>> getChapterByLanguageName(@PathVariable("languageName") String languageName){
        return ApiResponse.<List<ChapterResponse>>builder()
                .result(courseService.getChapterByLanguage(languageName))
                .build();
    }
    @GetMapping ("/{language}/progress")
    ApiResponse<LearingProgressResponse> learingProgressResponseApiResponse(@PathVariable("language") String language){
        return ApiResponse.<LearingProgressResponse>builder()
                .result(courseService.getLearningProgress(language))
                .build();
    }
    @PostMapping("/verify")
    ApiResponse<LessonBatchResponse> verifyLesson(@RequestBody LessonBatchRequest request) {
        return ApiResponse.<LessonBatchResponse>builder()
                .result(learningService.verifyLesson(request))
                .build();
    }
    @GetMapping("/{languageName}")
    ApiResponse<List<UserChapterResponse>> getMyLearning(@PathVariable String languageName){
        return ApiResponse.<List<UserChapterResponse>>builder()
                .result(courseService.getMyLearning(languageName))
                .build();
    }
    @PostMapping("/practice/submit")
    ApiResponse<CodeResponse> submitPractice(@RequestBody PracticeSubmitRequest request) {
        return ApiResponse.<CodeResponse>builder()
                .result(practiceService.summit(request.getProblemId(), request.getCode()))
                .build();
    }
    @PostMapping("/practice/complete")
    ApiResponse<PracticeSubmitResponse> completePractice(@RequestBody PracticeSubmitRequest request) {
        return ApiResponse.<PracticeSubmitResponse>builder()
                .result(practiceService.complete(request.getProblemId()))
                .build();
    }
    @DeleteMapping("")
    ApiResponse<Void> resetProgress(String languageName){
        learningProgressService.restProgress(languageName);
        return ApiResponse.<Void>builder().build();
    }
}
