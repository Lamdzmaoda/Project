/* (C)2026 */
package com.example.identity_servive.controller;

import com.example.identity_servive.dto.request.*;
import com.example.identity_servive.dto.response.*;
import com.example.identity_servive.service.CourseService;
import com.example.identity_servive.service.LearningService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j // Hỗ trợ ghi log để theo dõi luồng dữ liệu
@RestController // Đánh dấu là REST Controller, tự động chuyển kết quả trả về thành JSON
@RequestMapping("/course") // Định nghĩa đường dẫn gốc cho các API trong class này là /permissions
@RequiredArgsConstructor // Tự động tạo Constructor để Inject PermissionService
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Tự động biến các field thành 'private final'
public class CourseController {

    // Tiêm Service xử lý nghiệp vụ liên quan đến Permission
    CourseService courseService;
    LearningService learningService;

    @PostMapping("/steps")
    ApiResponse<StepResponse> createStep(@RequestBody StepRequest request) {
        // 1. Nhận dữ liệu từ Client, đẩy xuống Service để lưu vào DB
        // 2. Bọc kết quả trả về vào đối tượng ApiResponse chuẩn
        return ApiResponse.<StepResponse>builder()
                .result(courseService.createStep(request))
                .build();
    }
    @PostMapping("/lessons")
    ApiResponse<LessonResponse> createLesson(@RequestBody LessonRequest request) {
        // 1. Nhận dữ liệu từ Client, đẩy xuống Service để lưu vào DB
        // 2. Bọc kết quả trả về vào đối tượng ApiResponse chuẩn
        return ApiResponse.<LessonResponse>builder()
                .result(courseService.createLesson(request))
                .build();
    }
    @PostMapping("/chapters")
    ApiResponse<ChapterResponse> createChapter(@RequestBody ChapterRequest request) {
        // 1. Nhận dữ liệu từ Client, đẩy xuống Service để lưu vào DB
        // 2. Bọc kết quả trả về vào đối tượng ApiResponse chuẩn
        return ApiResponse.<ChapterResponse>builder()
                .result(courseService.createChapter(request))
                .build();
    }
    @PostMapping("/languages")
    ApiResponse<LanguageResponse> createLanguage(@RequestBody LanguageRequest request) {
        // 1. Nhận dữ liệu từ Client, đẩy xuống Service để lưu vào DB
        // 2. Bọc kết quả trả về vào đối tượng ApiResponse chuẩn
        return ApiResponse.<LanguageResponse>builder()
                .result(courseService.createLanguage(request))
                .build();
    }

    @GetMapping("/steps")
    ApiResponse<List<StepResponse>> getSteps() {
        // Gọi Service lấy toàn bộ danh sách và trả về cho Client
        return ApiResponse.<List<StepResponse>>builder()
                .result(courseService.getStep())
                .build();
    }
    @GetMapping("/lessons")
    ApiResponse<List<LessonResponse>> getLessons() {
        // Gọi Service lấy toàn bộ danh sách và trả về cho Client
        return ApiResponse.<List<LessonResponse>>builder()
                .result(courseService.getLesson())
                .build();
    }
    @GetMapping("/chapters")
    ApiResponse<List<ChapterResponse>> getChapter() {
        // Gọi Service lấy toàn bộ danh sách và trả về cho Client
        return ApiResponse.<List<ChapterResponse>>builder()
                .result(courseService.getChapter())
                .build();
    }
    @GetMapping("/languages")
    ApiResponse<List<LanguageResponse>> getLanguages() {
        // Gọi Service lấy toàn bộ danh sách và trả về cho Client
        return ApiResponse.<List<LanguageResponse>>builder()
                .result(courseService.getLanguage())
                .build();
    }

    @PutMapping("/steps/{stepsId}")
    StepResponse updateStep(@RequestBody StepRequest request, @PathVariable("stepsId") String stepsId) {
        return courseService.updateStep(stepsId, request);
    }
    @PutMapping("/lessons/{lessonId}")
    LessonResponse updateLesson(@RequestBody LessonRequest request, @PathVariable("lessonId") String lessonId) {
        return courseService.updateLesson(lessonId, request);
    }
    @PutMapping("/chapters/{chapterId}")
    ChapterResponse updateChapter(@RequestBody ChapterRequest request, @PathVariable("chapterId") String chapterId) {
        return courseService.updateChapter(chapterId, request);
    }
    @PutMapping("/languages/{languageName}")
    LanguageResponse updateLanguage(@RequestBody LanguageUpdateRequest request, @PathVariable("languageName") String languageName) {
        return courseService.updateLanguage(languageName, request);
    }
    @GetMapping("/steps/{stepId}")
    StepResponse getStep(@PathVariable("stepId") String stepId){
        return courseService.getStepById(stepId);
    }
    @GetMapping("/lessons/{lessonId}")
    LessonResponse getLesson(@PathVariable("lessonId") String lessonId){
        return courseService.getLessonById(lessonId);
    }
    @GetMapping("/chapters/{chapterId}")
    ChapterResponse getChapter(@PathVariable("chapterId") String chapterId){
        return courseService.getChapterById(chapterId);
    }
    @GetMapping("/languages/{languageName}")
    LanguageResponse getLanguage(@PathVariable("languageName") String languageName){
        return courseService.getLanguageById(languageName);
    }
    @GetMapping("/steps/BetterLesson/{lessonId}")
    ApiResponse<List<StepResponse>> getStepByLessonId(@PathVariable("lessonId") String lessonId){
        return ApiResponse.<List<StepResponse>>builder()
                .result(courseService.getStepByLesson(lessonId))
                .build();
    }
    @GetMapping("/lessons/BetterChapter/{chapterId}")
    ApiResponse<List<LessonResponse>> getLessonByChapterId(@PathVariable("chapterId") String chapterId){
        return ApiResponse.<List<LessonResponse>>builder()
                .result(courseService.getLessonByChapter(chapterId))
                .build();
    }
    @GetMapping("/chapters/BetterLanguage/{languageName}")
    ApiResponse<List<ChapterResponse>> getChapterByLanguageName(@PathVariable("languageName") String languageName){
        return ApiResponse.<List<ChapterResponse>>builder()
                .result(courseService.getChapterByLanguage(languageName))
                .build();
    }

    @DeleteMapping("/steps/{stepId}")
    ApiResponse<Void> deleteStep(@PathVariable("stepId") String stepId) {
        // 1. Lấy tên permission từ đường dẫn (URL Path)
        // 2. Gọi Service thực hiện lệnh xóa
        courseService.deleteStep(stepId);

        // 3. Trả về phản hồi trống (chỉ báo thành công)
        return ApiResponse.<Void>builder().build();
    }
    @DeleteMapping("/lessons/{lessonId}")
    ApiResponse<Void> deleteLesson(@PathVariable("lessonId") String lessonId) {
        // 1. Lấy tên permission từ đường dẫn (URL Path)
        // 2. Gọi Service thực hiện lệnh xóa
        courseService.deleteLesson(lessonId);

        // 3. Trả về phản hồi trống (chỉ báo thành công)
        return ApiResponse.<Void>builder().build();
    }
    @DeleteMapping("/chapters/{chapterId}")
    ApiResponse<Void> deleteChapter(@PathVariable("chapterId") String chapterId) {
        // 1. Lấy tên permission từ đường dẫn (URL Path)
        // 2. Gọi Service thực hiện lệnh xóa
        courseService.deleteChapter(chapterId);

        // 3. Trả về phản hồi trống (chỉ báo thành công)
        return ApiResponse.<Void>builder().build();
    }
    @DeleteMapping("/languages/{languageName}")
    ApiResponse<Void> deleteLanguage(@PathVariable("languageName") String languageName) {
        // 1. Lấy tên permission từ đường dẫn (URL Path)
        // 2. Gọi Service thực hiện lệnh xóa
        courseService.deleteLanguage(languageName);

        // 3. Trả về phản hồi trống (chỉ báo thành công)
        return ApiResponse.<Void>builder().build();
    }
}