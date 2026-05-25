/* (C)2026 */
package com.example.identity_servive.controller.learning;

import com.example.identity_servive.dto.request.learningRequest.*;
import com.example.identity_servive.dto.response.ApiResponse;
import com.example.identity_servive.dto.response.learningResponse.*;
import com.example.identity_servive.service.learning.CourseService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j // Hỗ trợ ghi log để theo dõi luồng dữ liệu
@RestController // Đánh dấu là REST Controller, tự động chuyển kết quả trả về thành JSON
@RequestMapping("/course") // Định nghĩa đường dẫn gốc cho các API trong class này là /permissions
@RequiredArgsConstructor // Tự động tạo Constructor để Inject PermissionService
@FieldDefaults(
    level = AccessLevel.PRIVATE,
    makeFinal = true) // Tự động biến các field thành 'private final'
public class CourseController {

  // Tiêm Service xử lý nghiệp vụ liên quan đến Permission
  CourseService courseService;

  @PostMapping("/steps")
  ApiResponse<StepResponse> createStep(@RequestBody StepRequest request) {
    // 1. Nhận dữ liệu từ Client, đẩy xuống Service để lưu vào DB
    // 2. Bọc kết quả trả về vào đối tượng ApiResponse chuẩn
    return ApiResponse.<StepResponse>builder().result(courseService.createStep(request)).build();
  }

  @PostMapping("/problems")
  ApiResponse<ProblemResponse> createProblem(@RequestBody ProblemRequest request) {
    return ApiResponse.<ProblemResponse>builder()
        .result(courseService.createProblem(request))
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

  @GetMapping("/problems/admin/{lessonId}")
  ApiResponse<List<ProblemResponse>> getProblems(@PathVariable("lessonId") String lessonId) {
    // Gọi Service lấy toàn bộ danh sách và trả về cho Client
    return ApiResponse.<List<ProblemResponse>>builder()
        .result(courseService.getAllProblemsByLessonForAdmin(lessonId))
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
  ApiResponse<List<ChapterResponse>> getChapters(
      @PathVariable("languageName") String languageName) {
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
  ApiResponse<StepResponse> updateStep(
      @RequestBody StepUpdateRequest request, @PathVariable("stepsId") String stepsId) {
    return ApiResponse.<StepResponse>builder()
        .result(courseService.updateStep(stepsId, request))
        .build();
  }

  @PutMapping("/problems/{problemId}")
  ApiResponse<ProblemResponse> updateProblem(
      @RequestBody ProblemUpdateRequest request, @PathVariable("problemId") String problemId) {
    return ApiResponse.<ProblemResponse>builder()
        .result(courseService.updateProblem(problemId, request))
        .build();
  }

  @PutMapping("/lessons/{lessonId}")
  ApiResponse<LessonResponse> updateLesson(
      @RequestBody LessonUpdateRequest request, @PathVariable("lessonId") String lessonId) {
    return ApiResponse.<LessonResponse>builder()
        .result(courseService.updateLesson(lessonId, request))
        .build();
  }

  @PutMapping("/chapters/{chapterId}")
  ApiResponse<ChapterResponse> updateChapter(
      @RequestBody ChapterUpdateRequest request, @PathVariable("chapterId") String chapterId) {
    return ApiResponse.<ChapterResponse>builder()
        .result(courseService.updateChapter(chapterId, request))
        .build();
  }

  @PutMapping("/languages/{languageName}")
  ApiResponse<LanguageResponse> updateLanguage(
      @RequestBody LanguageUpdateRequest request,
      @PathVariable("languageName") String languageName) {
    return ApiResponse.<LanguageResponse>builder()
        .result(courseService.updateLanguage(languageName, request))
        .build();
  }

  @GetMapping("/steps/{stepId}")
  ApiResponse<StepResponse> getStep(@PathVariable("stepId") String stepId) {
    return ApiResponse.<StepResponse>builder().result(courseService.getStepById(stepId)).build();
  }

  @GetMapping("/problems/{problemId}")
  ApiResponse<ProblemResponse> getProblem(@PathVariable("problemId") String problemId) {
    return ApiResponse.<ProblemResponse>builder()
        .result(courseService.getProblemById(problemId))
        .build();
  }

  @GetMapping("/lessons/{lessonId}")
  ApiResponse<LessonResponse> getLesson(@PathVariable("lessonId") String lessonId) {
    return ApiResponse.<LessonResponse>builder()
        .result(courseService.getLessonById(lessonId))
        .build();
  }

  @GetMapping("/chapters/{chapterId}")
  ApiResponse<ChapterResponse> getChapter(@PathVariable("chapterId") String chapterId) {
    return ApiResponse.<ChapterResponse>builder()
        .result(courseService.getChapterById(chapterId))
        .build();
  }

  @GetMapping("/languages/{languageName}")
  ApiResponse<LanguageResponse> getLanguage(@PathVariable("languageName") String languageName) {
    return ApiResponse.<LanguageResponse>builder()
        .result(courseService.getLanguageById(languageName))
        .build();
  }

  // Sửa toàn bộ nhóm hàm delete
  @DeleteMapping("/steps/{stepId}") // Đổi từ @PutMapping thành @DeleteMapping
  ApiResponse<Void> deleteStep(@PathVariable("stepId") String stepId) {
    courseService.deleteStep(stepId);
    return ApiResponse.<Void>builder().build();
  }

  @DeleteMapping("/problems/{problemId}") // Đổi từ @PutMapping thành @DeleteMapping
  ApiResponse<Void> deleteProblem(@PathVariable("problemId") String problemId) {
    courseService.deleteProblem(problemId);
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

  @DeleteMapping("/problems/admin/{problemId}") // Đổi từ @PutMapping thành @DeleteMapping
  ApiResponse<Void> purgeProblem(@PathVariable("problemId") String problemId) {
    courseService.purgeProblem(problemId);
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
