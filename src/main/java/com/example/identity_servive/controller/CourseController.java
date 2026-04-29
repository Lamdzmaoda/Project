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

    @GetMapping("/steps/admin/{lessonId}")
    ApiResponse<List<StepResponse>> getSteps(@PathVariable("lessonId") String lessonId) {
        // Gọi Service lấy toàn bộ danh sách và trả về cho Client
        return ApiResponse.<List<StepResponse>>builder()
                .result(courseService.getAllStepsByLessonForAdmin(lessonId))
                .build();
    }
    @GetMapping("/lessons/admin/{chapterId}")
    ApiResponse<List<LessonResponse>> getLessons(@PathVariable("chapterId") String chapterId) {
        // Gọi Service lấy toàn bộ danh sách và trả về cho Client
        return ApiResponse.<List<LessonResponse>>builder()
                .result(courseService.getAllLessonsByChapterForAdmin(chapterId))
                .build();
    }
    @GetMapping("/chapters/admin/{languageName}")
    ApiResponse<List<ChapterResponse>> getChapters(@PathVariable("languageName") String languageName) {
        // Gọi Service lấy toàn bộ danh sách và trả về cho Client
        return ApiResponse.<List<ChapterResponse>>builder()
                .result(courseService.getAllChaptersByLanguageForAdmin(languageName))
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
    StepResponse updateStep(@RequestBody StepUpdateRequest request, @PathVariable("stepsId") String stepsId) {
        return courseService.updateStep(stepsId, request);
    }
    @PutMapping("/lessons/{lessonId}")
    LessonResponse updateLesson(@RequestBody LessonUpdateRequest request, @PathVariable("lessonId") String lessonId) {
        return courseService.updateLesson(lessonId, request);
    }
    @PutMapping("/chapters/{chapterId}")
    ChapterResponse updateChapter(@RequestBody ChapterUpdateRequest request, @PathVariable("chapterId") String chapterId) {
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

    // Sửa toàn bộ nhóm hàm delete
    @DeleteMapping("/steps/{stepId}") // Đổi từ @PutMapping thành @DeleteMapping
    ApiResponse<Void> deleteStep(@PathVariable("stepId") String stepId) {
        courseService.deleteStep(stepId);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/lessons/{lessonId}")
    ApiResponse<Void> deleteLesson(@PathVariable("lessonId") String lessonId) {
        courseService.deleteLesson(lessonId);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/chapters/{chapterId}")
    ApiResponse<Void> deleteChapter(@PathVariable("chapterId") String chapterId) {
        courseService.deleteChapter(chapterId);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/languages/{languageName}")
    ApiResponse<Void> deleteLanguage(@PathVariable("languageName") String languageName) {
        courseService.deleteLanguage(languageName);
        return ApiResponse.<Void>builder().build();
    }
    @DeleteMapping("/steps/admin/{stepId}") // Đổi từ @PutMapping thành @DeleteMapping
    ApiResponse<Void> purgeStep(@PathVariable("stepId") String stepId) {
        courseService.purgeStep(stepId);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/lessons/admin/{lessonId}")
    ApiResponse<Void> purgeLesson(@PathVariable("lessonId") String lessonId) {
        courseService.purgeLesson(lessonId);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/chapters/admin/{chapterId}")
    ApiResponse<Void> purgeChapter(@PathVariable("chapterId") String chapterId) {
        courseService.purgeChapter(chapterId);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/languages/admin/{languageName}")
    ApiResponse<Void> purgeLanguage(@PathVariable("languageName") String languageName) {
        courseService.purgeLanguage(languageName);
        return ApiResponse.<Void>builder().build();
    }
}