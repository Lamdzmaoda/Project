package com.example.identity_servive.service;

import com.example.identity_servive.dto.request.*;
import com.example.identity_servive.dto.response.*;
import com.example.identity_servive.entity.*;
import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.mapper.CourseMapper;
import com.example.identity_servive.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j // Hỗ trợ ghi lại lịch sử hoạt động (Logging)
@Service // Đăng ký lớp này là một Service do Spring quản lý (Bean)
@RequiredArgsConstructor // Tự động tạo Constructor để tiêm (Inject) các Repository và Mapper vào
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseService {
    LessonRepository lessonRepository;
    ChapterRepository chapterRepository;
    StepRepository stepRepository;
    LanguageRepository languageRepository;
    UserRepository userRepository;
    CourseMapper courseMapper;
    UserStepProgressRepository userStepProgressRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public StepResponse createStep(StepRequest stepRequest) {
        Step step = courseMapper.toStep(stepRequest);
        var lesson = lessonRepository.findById(stepRequest.getLessonId())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

        try{
                String jsonBody = objectMapper.writeValueAsString(stepRequest.getData());
                step.setData(jsonBody);
        }catch (JsonProcessingException e){
            log.error("Lỗi parse dữ liệu: {}", e.getMessage());
            throw new AppException(ErrorCode.UNAUTHORIZED_EXISTED);
        }
        step.setLesson(lesson);
        step = stepRepository.save(step);
        return convertToResponse(step);
    }

    @Transactional
    public LessonResponse createLesson(LessonRequest lessonRequest) {
        var chapter = chapterRepository.findById(lessonRequest.getChapterId())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

        var lesson = courseMapper.toLesson(lessonRequest);
        lesson.setChapter(chapter);

        var steps = stepRepository.findAllById(lessonRequest.getSteps());
        steps.forEach(step -> step.setLesson(lesson));
        lesson.setSteps(new HashSet<>(steps));


        var savedLesson = lessonRepository.save(lesson);

        return courseMapper.toLessonResponse(savedLesson);
    }
    @Transactional
    public ChapterResponse createChapter(ChapterRequest chapterRequest) {
        var chapter = courseMapper.toChapter(chapterRequest);

        var language = languageRepository.findById(chapterRequest.getLanguageName())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        chapter.setLanguage(language);

        var lessons = lessonRepository.findAllById(chapterRequest.getLessons());

        lessons.forEach(lesson -> lesson.setChapter(chapter));

        chapter.setLessons(new HashSet<>(lessons));

        chapterRepository.save(chapter);

        return courseMapper.toChapterResponse(chapter);
    }

    @Transactional
    public LanguageResponse createLanguage(LanguageRequest languageRequest) {

        if(languageRepository.existsByName(languageRequest.getName()))
            throw new AppException(ErrorCode.NAME_EXISTED);
        var language = courseMapper.toLanguage(languageRequest);

        var chapters = chapterRepository.findAllById(languageRequest.getChapters());

        chapters.forEach(chapter -> chapter.setLanguage(language));
        language.setChapters(new HashSet<>(chapters));
        languageRepository.save(language);
        return courseMapper.toLanguageResponse(language);
    }
    public StepResponse getStepById(String stepId){
        return courseMapper.toStepResponse(stepRepository.findById(stepId).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED)));
    }
    public LessonResponse getLessonById(String lessonId){
        return courseMapper.toLessonResponse(lessonRepository.findById(lessonId).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED)));
    }
    public ChapterResponse getChapterById(String chapterId){
        return courseMapper.toChapterResponse(chapterRepository.findById(chapterId).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED)));
    }
    public LanguageResponse getLanguageById(String languageName){
        return courseMapper.toLanguageResponse(languageRepository.findById(languageName).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED)));
    }
    public List<StepResponse> getStepByLesson(String lessonId){
        var steps = stepRepository.findByLessonId(lessonId);
        return steps.stream().map(this::convertToResponse).toList();
    }
    public List<LessonResponse> getLessonByChapter(String chapterId){
        var lessons = lessonRepository.findByChapterId(chapterId);
        return lessons.stream().map(courseMapper::toLessonResponse).toList();
    }
    public List<ChapterResponse> getChapterByLanguage(String languageName){
        var chapters = chapterRepository.findByLanguageName(languageName);
        return chapters.stream().map(courseMapper::toChapterResponse).toList();
    }
    public List<StepResponse> getStep() {
        var steps = stepRepository.findAll();


        return steps.stream().map(this::convertToResponse).toList();
    }
    private StepResponse convertToResponse(Step step) {
        StepResponse stepResponse = courseMapper.toStepResponse(step);

        try{
            if(step.getData() != null && !step.getData().isEmpty()){
                Object dataObject = objectMapper.readValue(step.getData(), Object.class);
                stepResponse.setData(dataObject);
            }
        }catch (JsonProcessingException e){
            log.error("Lỗi parse JSON: {}", e.getMessage());
        }
        return stepResponse;
    }
    public List<LessonResponse> getLesson() {
        var lessons = lessonRepository.findAll();

        return lessons.stream().map(courseMapper::toLessonResponse).toList();
    }
    public List<ChapterResponse> getChapter() {
        return chapterRepository.findAll().stream().map(courseMapper::toChapterResponse).toList();
    }
    public List<LanguageResponse> getLanguage() {
        return languageRepository.findAll().stream().map(courseMapper::toLanguageResponse).toList();
    }
    public StepResponse getStepOfUserProgress(String stepId) {
        Step step = stepRepository.findById(stepId).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED));

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String name = authentication.getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // 3. Lấy tiến trình của User cho Step này
        UserStepProgress progress = userStepProgressRepository.findByUserAndStep(user, step)
                .orElse(null); // Hoặc tạo mặc định là LOCKED

        // 4. Kiểm tra quyền truy cập (Ví dụ: Nếu bị khóa thì không cho xem)
        if (progress == null || progress.getLockedStatus() == IsLocked.TRUE_LOCKED) {
            throw new AppException(ErrorCode.STEP_LOCKED); // Giả sử bạn có mã lỗi này
        }

        // 5. Chuyển đổi sang Response (Dùng Mapper của bạn)
        StepResponse response = convertToResponse(step);

        // Gán thêm thông tin tiến trình
        response.setCompletedStatus(progress.getCompletedStatus());
        response.setLockedStatus(progress.getLockedStatus());

        return response;
    }

    @Transactional
    public void deleteStep(String stepId) {
        stepRepository.deleteById(stepId);
    }
    @Transactional
    public void deleteLesson(String lessonId) {
        lessonRepository.deleteById(lessonId);
    }
    @Transactional
    public void deleteChapter(String chapterId) {
        chapterRepository.deleteById(chapterId);
    }
    @Transactional
    public void deleteLanguage(String languageName) {
        languageRepository.deleteById(languageName);
    }

    @Transactional
    public StepResponse updateStep(String stepId, StepRequest stepRequest) {
        Step step = stepRepository.findById(stepId).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED));
        courseMapper.updateStep(stepRequest, step);
        if(stepRequest.getData() != null){
            try{
                step.setData(objectMapper.writeValueAsString(stepRequest.getData()));
            }catch (JsonProcessingException e){
                log.error("Error serializing step data: {}", e.getMessage());
                throw new AppException(ErrorCode.UNAUTHORIZED_EXISTED);
            }
        }
        stepRepository.save(step);
        return convertToResponse(step);
    }

    @Transactional
    public LessonResponse updateLesson(String lessonId, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED));
        courseMapper.updateLesson(request, lesson);
        if(request.getSteps() != null){
            var newSteps = stepRepository.findAllById(request.getSteps());

            updateCollection(lesson.getSteps(), new HashSet<>(newSteps));
            newSteps.forEach(step -> step.setLesson(lesson));
        }

        var savedlesson = lessonRepository.save(lesson);
        return courseMapper.toLessonResponse(savedlesson);
    }

    @Transactional
    public ChapterResponse updateChapter(String chapterId, ChapterRequest chapterRequest) {
        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED));
        courseMapper.updateChapter(chapterRequest, chapter);
        if(chapterRequest.getLessons() != null){
            var newLessons = lessonRepository.findAllById(chapterRequest.getLessons());
            updateCollection(chapter.getLessons(),new HashSet<>(newLessons));
            newLessons.forEach(lesson -> lesson.setChapter(chapter));
        }
        var savedchapter = chapterRepository.save(chapter);
        return  courseMapper.toChapterResponse(savedchapter);
    }

    @Transactional
    public LanguageResponse updateLanguage(String languageId, LanguageUpdateRequest request) {
        Language language = languageRepository.findById(languageId).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED));
        courseMapper.updateLanguage(request, language);
        if(request.getChapters() != null){
            var newLessons = chapterRepository.findAllById(request.getChapters());
            updateCollection(language.getChapters(),new HashSet<>(newLessons));
            newLessons.forEach( chapter-> chapter.setLanguage(language));
        }
        var savedLanguage = languageRepository.save(language);
        return  courseMapper.toLanguageResponse(savedLanguage);
    }
    private <T> void updateCollection(Set<T> targetSet, Set<T> sourceSet) {
        // 1. Xóa hết phần tử cũ trong "túi" mà Hibernate đang giữ
        targetSet.clear();

        // 2. Nếu dữ liệu mới không rỗng thì đổ vào "túi" đó
        if (sourceSet != null) {
            targetSet.addAll(sourceSet);
        }
    }
}
