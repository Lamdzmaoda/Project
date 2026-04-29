package com.example.identity_servive.service;

import com.example.identity_servive.dto.request.*;
import com.example.identity_servive.dto.response.*;
import com.example.identity_servive.entity.*;
import com.example.identity_servive.enums.ContentStatus;
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
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

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
    UserLessonProgressRepository userLessonProgressRepository;


    @Transactional
    @PreAuthorize("hasAuthority('CREATE_COURSE')")
    public StepResponse createStep(StepRequest stepRequest) {
        Step step = courseMapper.toStep(stepRequest);
        var lesson = lessonRepository.findById(stepRequest.getLessonId())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        step.setLesson(lesson);
        step = stepRepository.save(step);
        return courseMapper.toStepResponse(step);
    }
    @Transactional
    @PreAuthorize("hasAuthority('CREATE_COURSE')")
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
    @PreAuthorize("hasAuthority('CREATE_COURSE')")
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
    @PreAuthorize("hasAuthority('CREATE_COURSE')")
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
    @PreAuthorize("hasRole('USER')")
    public StepResponse getStepById(String stepId){
        return courseMapper.toStepResponse(stepRepository.findByIdAndStatus(stepId, ContentStatus.ACTIVE).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED)));
    }
    @PreAuthorize("hasRole('USER')")
    public LessonResponse getLessonById(String lessonId){
        return courseMapper.toLessonResponse(lessonRepository.findByIdAndStatus(lessonId, ContentStatus.ACTIVE).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED)));
    }
    @PreAuthorize("hasRole('USER')")
    public ChapterResponse getChapterById(String chapterId) {
        return courseMapper.toChapterResponse(chapterRepository.findByIdAndStatus(chapterId, ContentStatus.ACTIVE).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED)));
    }
    @PreAuthorize("hasRole('USER')")
    public LanguageResponse getLanguageById(String languageName){
        return courseMapper.toLanguageResponse(languageRepository.findByNameAndStatus(languageName, ContentStatus.ACTIVE).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED)));
    }
    @PreAuthorize("hasRole('USER')")
    public List<StepResponse> getStepByLesson(String lessonId){
        var steps = stepRepository.findAllByLessonIdAndStatusOrderByOrderIndexAsc(lessonId, ContentStatus.ACTIVE);
        return steps.stream().map(courseMapper::toStepResponse).toList();
    }
    @PreAuthorize("hasRole('USER')")
    public List<LessonResponse> getLessonByChapter(String chapterId){
        var lessons = lessonRepository.findAllByChapterIdAndStatusOrderByOrderIndexAsc(chapterId, ContentStatus.ACTIVE);
        return lessons.stream().map(courseMapper::toLessonResponse).toList();
    }
    @PreAuthorize("hasRole('USER')")
    public List<ChapterResponse> getChapterByLanguage(String languageName){
        var chapters = chapterRepository.findAllByLanguageNameAndStatusOrderByOrderIndexAsc(languageName, ContentStatus.ACTIVE);
        return chapters.stream().map(courseMapper::toChapterResponse).toList();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<StepResponse> getAllStepsByLessonForAdmin(String lessonId) {
        var steps = stepRepository.findAllByLessonIdOrderByOrderIndexAsc(lessonId);
        return steps.stream().map(courseMapper::toStepResponse).toList();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<LessonResponse> getAllLessonsByChapterForAdmin(String chapterId) {
        var lessons = lessonRepository.findAllByChapterIdOrderByOrderIndexAsc(chapterId);
        return lessons.stream().map(courseMapper::toLessonResponse).toList();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<ChapterResponse> getAllChaptersByLanguageForAdmin(String languageName) {
        return chapterRepository.findAllByLanguageNameOrderByOrderIndexAsc(languageName).stream().map(courseMapper::toChapterResponse).toList();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<LanguageResponse> getLanguage() {
        return languageRepository.findAll().stream().map(courseMapper::toLanguageResponse).toList();
    }
    @PreAuthorize("hasAuthority('DELETE_COURSE')")
    @Transactional
    public void deleteStep(String stepId) {
        Step step = stepRepository.findByIdAndStatus(stepId, ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
                step.setStatus(ContentStatus.DELETED);
                stepRepository.save(step);
    }
    @PreAuthorize("hasAuthority('DELETE_COURSE')")
    @Transactional
    public void deleteLesson(String lessonId) {
        Lesson lesson = lessonRepository.findByIdAndStatus(lessonId, ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        lesson.getSteps().forEach(step -> {
            deleteStep(step.getId());
        });
        lesson.setStatus(ContentStatus.DELETED);
        lessonRepository.save(lesson);
    }
    @PreAuthorize("hasAuthority('DELETE_COURSE')")
    @Transactional
    public void deleteChapter(String chapterId) {
        Chapter chapter = chapterRepository.findByIdAndStatus(chapterId, ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        chapter.getLessons().forEach(lesson -> {
            deleteLesson(lesson.getId());
        });
        chapter.setStatus(ContentStatus.DELETED);
        chapterRepository.save(chapter);
    }
    @PreAuthorize("hasAuthority('DELETE_COURSE')")
    @Transactional
    public void deleteLanguage(String languageName) {

        Language language = languageRepository.findByNameAndStatus(languageName, ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        language.getChapters().forEach(chapter -> {
            deleteChapter(chapter.getId());
        });

        language.setStatus(ContentStatus.DELETED);
        languageRepository.save(language);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void purgeStep(String stepId){
        Step step = stepRepository.findById(stepId).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        if(step.getStatus() != ContentStatus.DELETED){
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
        stepRepository.deleteById(stepId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void purgeLesson(String lessonId){
        if(lessonRepository.findById(lessonId).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED)).getStatus() != ContentStatus.DELETED){
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
        lessonRepository.deleteById(lessonId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void purgeChapter(String chapterId){
        if(chapterRepository.findById(chapterId).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED)).getStatus() != ContentStatus.DELETED){
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
        chapterRepository.deleteById(chapterId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void purgeLanguage(String languageName){
        if(languageRepository.findById(languageName).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED)).getStatus() != ContentStatus.DELETED){
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
        languageRepository.deleteById(languageName);
    }
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE_COURSE')")
    @Transactional
    public StepResponse updateStep(String stepId, StepUpdateRequest stepRequest) {
        Step step = stepRepository.findById(stepId).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED));
        courseMapper.updateStep(stepRequest, step);
        if(stepRequest.getData() != null){

        }
        stepRepository.save(step);
        return courseMapper.toStepResponse(step);
    }
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE_COURSE')")
    @Transactional
    public LessonResponse updateLesson(String lessonId, LessonUpdateRequest request) {
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
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE_COURSE')")
    @Transactional
    public ChapterResponse updateChapter(String chapterId, ChapterUpdateRequest chapterRequest) {
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
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE_COURSE')")
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
