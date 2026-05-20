package com.example.identity_servive.service.learning;

import com.example.identity_servive.dto.request.learningRequest.*;
import com.example.identity_servive.dto.response.learningResponse.*;
import com.example.identity_servive.dto.response.learningResponse.ProblemResponse;
import com.example.identity_servive.dto.response.progress.LearingProgressResponse;
import com.example.identity_servive.dto.response.progress.UserChapterResponse;
import com.example.identity_servive.dto.response.progress.UserLessonResponse;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.*;
import com.example.identity_servive.entity.progress.Enrollment;
import com.example.identity_servive.entity.progress.UserLessonProgress;
import com.example.identity_servive.enums.ContentStatus;
import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
import com.example.identity_servive.enums.LessonType;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.mapper.CourseMapper;
import com.example.identity_servive.repository.Progress.EnrollmentRepository;
import com.example.identity_servive.repository.Progress.UserChapterProgressRepository;
import com.example.identity_servive.repository.Progress.UserLessonProgressRepository;
import com.example.identity_servive.repository.learning.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


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
    EnrollmentRepository enrollmentRepository;
    CourseMapper courseMapper;
    UserLessonProgressRepository userLessonProgressRepository;
    UserChapterProgressRepository userChapterProgressRepository;
    LearningProgressService learningProgressService;
    ProblemRepository problemRepository;



    @PreAuthorize("hasAuthority('CREATE_COURSE') or hasRole('ADMIN')")
    public StepResponse createStep(StepRequest stepRequest) {
        Step step = courseMapper.toStep(stepRequest);
        var lesson = lessonRepository.findByIdAndLessonType(stepRequest.getLessonId(), LessonType.LEARN)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        step.setLesson(lesson);
        step = stepRepository.save(step);
        return courseMapper.toStepResponse(step);
    }

    @PreAuthorize("hasAuthority('CREATE_COURSE') or hasRole('ADMIN')")
    public ProblemResponse createProblem(ProblemRequest request) {
        Lesson lesson = lessonRepository.findByIdAndLessonType(request.getLessonId(), LessonType.PRACTICE)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        Problem problem = courseMapper.toProblem(request);
        problem.setLesson(lesson);
        createConditions(problem, request.getConditions());

        return courseMapper.toProblemResponse(problemRepository.save(problem));
    }


    @PreAuthorize("hasAuthority('CREATE_COURSE') or hasRole('ADMIN')")
    public LessonResponse createLesson(LessonRequest lessonRequest) {
        var chapter = chapterRepository.findById(lessonRequest.getChapterId())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

        var lesson = courseMapper.toLesson(lessonRequest);
        lesson.setChapter(chapter);
        if(LessonType.LEARN.equals(lessonRequest.getLessonType())) {
            var steps = stepRepository.findAllById(lessonRequest.getSteps());
            steps.forEach(step -> step.setLesson(lesson));
            lesson.setSteps(new HashSet<>(steps));
        }else if(LessonType.PRACTICE.equals(lessonRequest.getLessonType())){
            var problems = problemRepository.findAllById(lessonRequest.getProblems());
            problems.forEach(problem -> problem.setLesson(lesson));
            lesson.setProblems(new HashSet<>(problems));
        }else {
            throw new AppException(ErrorCode.CREATE_FAIL, "lesson type not supported");
        }

        var savedLesson = lessonRepository.save(lesson);
        updateChapterXp(chapter);
        return courseMapper.toLessonResponse(savedLesson);
    }

    @PreAuthorize("hasAuthority('CREATE_COURSE') or hasRole('ADMIN')")
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

    @PreAuthorize("hasAuthority('CREATE_COURSE') or hasRole('ADMIN')")
    public LanguageResponse createLanguage(LanguageRequest languageRequest) {

        if (languageRepository.existsByName(languageRequest.getName()))
            throw new AppException(ErrorCode.NAME_EXISTED);
        var language = courseMapper.toLanguage(languageRequest);

        var chapters = chapterRepository.findAllById(languageRequest.getChapters());

        chapters.forEach(chapter -> chapter.setLanguage(language));
        language.setChapters(new HashSet<>(chapters));
        languageRepository.save(language);
        return courseMapper.toLanguageResponse(language);
    }

    @PreAuthorize("hasRole('USER')")
    public StepResponse getStepById(String stepId) {
        return courseMapper.toStepResponse(stepRepository.findByIdAndStatus(stepId, ContentStatus.ACTIVE).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED)));
    }
    @PreAuthorize("hasRole('USER')")
    public ProblemResponse getProblemById(String problemId){
        return courseMapper.toProblemResponse(problemRepository.findByIdAndStatus(problemId, ContentStatus.ACTIVE).orElseThrow(()
        -> new AppException(ErrorCode.ID_NOT_EXISTED)));
    }

    @PreAuthorize("hasRole('USER')")
    public LessonResponse getLessonById(String lessonId) {
        return courseMapper.toLessonResponse(lessonRepository.findByIdAndStatus(lessonId, ContentStatus.ACTIVE).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED)));
    }

    @PreAuthorize("hasRole('USER')")
    public ChapterResponse getChapterById(String chapterId) {
        return courseMapper.toChapterResponse(chapterRepository.findByIdAndStatus(chapterId, ContentStatus.ACTIVE).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED)));
    }

    @PreAuthorize("hasRole('USER')")
    public LanguageResponse getLanguageById(String languageName) {
        return courseMapper.toLanguageResponse(languageRepository.findByNameAndStatus(languageName, ContentStatus.ACTIVE).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED)));
    }

    @PreAuthorize("hasRole('USER')")
    public List<StepResponse> getStepByLesson(String lessonId) {
        var steps = stepRepository.findAllByLessonIdAndStatusOrderByOrderIndexAsc(lessonId, ContentStatus.ACTIVE);
        return steps.stream().map(courseMapper::toStepResponse).toList();
    }
    @PreAuthorize("hasRole('USER')")
    public List<ProblemResponse> getProblemByLesson(String lessonId) {

        return problemRepository.findAllByLessonIdAndStatusOrderByOrderIndexAsc(lessonId, ContentStatus.ACTIVE)
                .stream().map(courseMapper::toProblemResponse).toList();
    }


    @PreAuthorize("hasRole('USER')")
    public List<LessonResponse> getLessonByChapter(String chapterId) {
        var lessons = lessonRepository.findAllByChapterIdAndStatusOrderByOrderIndexAsc(chapterId, ContentStatus.ACTIVE);
        return lessons.stream().map(courseMapper::toLessonResponse).toList();
    }

    @PreAuthorize("hasRole('USER')")
    public List<ChapterResponse> getChapterByLanguage(String languageName) {
        var chapters = chapterRepository.findAllByLanguageNameAndStatusOrderByOrderIndexAsc(languageName, ContentStatus.ACTIVE);
        return chapters.stream().map(courseMapper::toChapterResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<StepResponse> getAllStepsByLessonForAdmin(String lessonId) {
        var steps = stepRepository.findAllByLessonIdOrderByOrderIndexAsc(lessonId);
        return steps.stream().map(courseMapper::toStepResponse).toList();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProblemResponse> getAllProblemsByLessonForAdmin(String lessonId) {
        var problems = problemRepository.findAllByLessonIdOrderByOrderIndexAsc(lessonId);
        return problems.stream().map(courseMapper::toProblemResponse).toList();
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
    public void deleteStep(String stepId) {
        Step step = stepRepository.findByIdAndStatus(stepId, ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        step.setStatus(ContentStatus.DELETED);
        stepRepository.save(step);
    }
    @PreAuthorize("hasAuthority('DELETE_COURSE')")
    public void deleteProblem(String problemId) {
        Problem problem = problemRepository.findByIdAndStatus(problemId, ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        problem.setStatus(ContentStatus.DELETED);
        problemRepository.save(problem);
    }

    @PreAuthorize("hasAuthority('DELETE_COURSE')")
    public void deleteLesson(String lessonId) {
        Lesson lesson = lessonRepository.findByIdAndStatus(lessonId, ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        softDelete(lesson);
        lesson.setStatus(ContentStatus.DELETED);
        lessonRepository.save(lesson);
    }

    private void softDelete(Lesson lesson) {
        if(LessonType.LEARN.equals(lesson.getLessonType())) {
            lesson.getSteps().forEach(step -> step.setStatus(ContentStatus.DELETED));
            lesson.setStatus(ContentStatus.DELETED);
        }
        else if(LessonType.PRACTICE.equals(lesson.getLessonType())){
            lesson.getProblems().forEach(problem -> problem.setStatus(ContentStatus.DELETED));
            lesson.setStatus(ContentStatus.DELETED);
        }
    }


    @PreAuthorize("hasAuthority('DELETE_COURSE')")
    public void deleteChapter(String chapterId) {
        Chapter chapter = chapterRepository.findByIdAndStatus(chapterId, ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        chapter.getLessons().forEach(lesson -> {
            softDelete(lesson);
            lessonRepository.save(lesson);
        });
        chapter.setStatus(ContentStatus.DELETED);
        chapterRepository.save(chapter);
    }

    @PreAuthorize("hasAuthority('DELETE_COURSE')")
    public void deleteLanguage(String languageName) {

        Language language = languageRepository.findByNameAndStatus(languageName, ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        language.getChapters().forEach(chapter -> deleteChapter(chapter.getId()));

        language.setStatus(ContentStatus.DELETED);
        languageRepository.save(language);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void purgeStep(String stepId) {
        Step step = stepRepository.findById(stepId).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        if (step.getStatus() != ContentStatus.DELETED) {
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
        stepRepository.deleteById(stepId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void purgeProblem(String problemId) {
        Problem problem = problemRepository.findById(problemId).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        if (!ContentStatus.DELETED.equals(problem.getStatus())) {
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
        problemRepository.deleteById(problemId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void purgeLesson(String lessonId) {
        if (lessonRepository.findById(lessonId).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED))
                .getStatus() != ContentStatus.DELETED) {
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
        lessonRepository.deleteById(lessonId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void purgeChapter(String chapterId) {
        if (chapterRepository.findById(chapterId).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED)).getStatus() != ContentStatus.DELETED) {
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
        chapterRepository.deleteById(chapterId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void purgeLanguage(String languageName) {
        if (languageRepository.findById(languageName).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED)).getStatus() != ContentStatus.DELETED) {
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
        languageRepository.deleteById(languageName);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE_COURSE')")
    public StepResponse updateStep(String stepId, StepUpdateRequest stepRequest) {
        Step step = stepRepository.findById(stepId).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED));
        courseMapper.updateStep(stepRequest, step);
        stepRepository.save(step);
        return courseMapper.toStepResponse(step);
    }
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE_COURSE')")
    public ProblemResponse updateProblem(String problemId, ProblemUpdateRequest request) {
        Problem problem = problemRepository.findById(problemId).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED));
        courseMapper.updateProblem(request, problem);
        createConditions(problem, request.getConditions());
        problemRepository.save(problem);
        return courseMapper.toProblemResponse(problem);
    }

    private void createConditions(Problem problem, List<ProblemConditionRequest> conditions2) {
        if (conditions2 != null) {
            var conditions = conditions2.stream().map(c -> ProblemCondition.builder()
                    .expectedCode(c.getExpectedCode())
                    .hint(c.getHint())
                    .orderIndex(c.getOrderIndex())
                    .build()
            ).toList();
            conditions.forEach(c -> c.setProblem(problem));
            problem.setConditions(new HashSet<>(conditions));
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE_COURSE')")
    public LessonResponse updateLesson(String lessonId, LessonUpdateRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED));
        courseMapper.updateLesson(request, lesson);
        if (request.getSteps() != null || request.getProblems() != null) {
            if (LessonType.LEARN.equals(lesson.getLessonType())) {
                assert request.getSteps() != null;
                var newSteps = stepRepository.findAllById(request.getSteps());

                updateCollection(lesson.getSteps(), new HashSet<>(newSteps));
                newSteps.forEach(step -> step.setLesson(lesson));
            } else if (LessonType.PRACTICE.equals(lesson.getLessonType())) {
                var newProblems = problemRepository.findAllById(request.getProblems());

                updateCollection(lesson.getProblems(), new HashSet<>(newProblems));
                newProblems.forEach(problem -> problem.setLesson(lesson));
            }
        }
        var savedlesson = lessonRepository.save(lesson);
        updateChapterXp(lesson.getChapter());
        return courseMapper.toLessonResponse(savedlesson);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE_COURSE')")
    public ChapterResponse updateChapter(String chapterId, ChapterUpdateRequest chapterRequest) {
        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED));
        courseMapper.updateChapter(chapterRequest, chapter);
        if (chapterRequest.getLessons() != null) {
            var newLessons = lessonRepository.findAllById(chapterRequest.getLessons());
            updateCollection(chapter.getLessons(), new HashSet<>(newLessons));
            newLessons.forEach(lesson -> lesson.setChapter(chapter));
        }
        var savedchapter = chapterRepository.save(chapter);
        return courseMapper.toChapterResponse(savedchapter);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE_COURSE')")
    public LanguageResponse updateLanguage(String languageId, LanguageUpdateRequest request) {
        Language language = languageRepository.findById(languageId).orElseThrow(()
                -> new AppException(ErrorCode.ID_NOT_EXISTED));
        courseMapper.updateLanguage(request, language);
        if (request.getChapters() != null) {
            var newLessons = chapterRepository.findAllById(request.getChapters());
            updateCollection(language.getChapters(), new HashSet<>(newLessons));
            newLessons.forEach(chapter -> chapter.setLanguage(language));
        }
        var savedLanguage = languageRepository.save(language);
        return courseMapper.toLanguageResponse(savedLanguage);
    }

    private <T> void updateCollection(Set<T> targetSet, Set<T> sourceSet) {
        // 1. Xóa hết phần tử cũ trong "túi" mà Hibernate đang giữ
        targetSet.clear();

        // 2. Nếu dữ liệu mới không rỗng thì đổ vào "túi" đó
        if (sourceSet != null) {
            targetSet.addAll(sourceSet);
        }
    }

    public List<UserChapterResponse> getMyLearning(String languageName) {
        User user = learningProgressService.getCurrentUser();

        var chapters = chapterRepository.findAllByLanguageNameAndStatusOrderByOrderIndexAsc(languageName, ContentStatus.ACTIVE);

        return chapters.stream().map(chapter -> {
            var chapterProgress = userChapterProgressRepository.findByUserAndChapter(user, chapter)
                    .orElse(null);
            var lessons = lessonRepository.findAllByChapterIdAndStatusOrderByOrderIndexAsc(chapter.getId(), ContentStatus.ACTIVE);

            List<UserLessonResponse> lessonResponses = lessons.stream().map(lesson -> {
                UserLessonProgress lessonProgress = userLessonProgressRepository.findByUserAndLesson(user, lesson)
                        .orElse(null);

                return UserLessonResponse.builder()
                        .id(lesson.getId())
                        .title(lesson.getTitle())
                        .orderIndex(lesson.getOrderIndex())
                        .xp(lesson.getXp())
                        .lessonType(lesson.getLessonType())
                        .description(lesson.getDescription())
                        .lockedStatus(lessonProgress != null ? lessonProgress.getLockedStatus() : IsLocked.TRUE_LOCKED)
                        .completedStatus(lessonProgress != null ? lessonProgress.getCompletedStatus() : IsCompleted.FALSE)
                        .steps(LessonType.LEARN.equals(lesson.getLessonType()) ?
                                lesson.getSteps().stream()
                                        .filter(s -> ContentStatus.ACTIVE.equals(s.getStatus()))
                                        .map(courseMapper::toStepResponse)
                                        .toList() : null)
                        .problems(LessonType.PRACTICE.equals(lesson.getLessonType()) ?
                                lesson.getProblems()
                                        .stream()
                                        .filter(s -> ContentStatus.ACTIVE.equals(s.getStatus()))
                                        .map(courseMapper::toProblemResponse).toList() : null)
                        .build();
            }).toList();

            return UserChapterResponse.builder()
                    .id(chapter.getId())
                    .title(chapter.getTitle())
                    .description(chapter.getDescription())
                    .orderIndex(chapter.getOrderIndex())
                    .totalXp(chapter.getTotalXp())
                    .slug(chapter.getSlug())
                    .completedStatus(chapterProgress != null ? chapterProgress.getCompletedStatus() : IsCompleted.FALSE)
                    .lockedStatus(chapterProgress != null ? chapterProgress.getLockedStatus() : IsLocked.TRUE_LOCKED)
                    .lessons(lessonResponses)
                    .build();
        }).toList();
    }
    public LearingProgressResponse getLearningProgress(String languageName){
        User user = learningProgressService.getCurrentUser();
        Language language = languageRepository.findByNameAndStatus(languageName, ContentStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

        Enrollment enrollment = enrollmentRepository.findByUserAndLanguage(user, language)
                .orElse(null);

        long currentXp = enrollment != null ? enrollment.getCurrentXp() : 0;
        long totalXp = calculateLanguageXp(languageName);
        int totalLesson = lessonRepository.countByChapterLanguageAndStatus(language, ContentStatus.ACTIVE);
        int completeLesson = userLessonProgressRepository
                .countByUserAndLesson_Chapter_LanguageAndCompletedStatus(user, language, IsCompleted.TRUE);
        int completeChapter = userChapterProgressRepository
                .countByUserAndChapter_LanguageAndCompletedStatus(user, language, IsCompleted.TRUE);

        return LearingProgressResponse.builder()
                .currentXp(currentXp)
                .totalXp(totalXp)
                .completedChapter(completeChapter)
                .completedLessons(completeLesson)
                .totalLessons(totalLesson)
                .build();
    }
    private void updateChapterXp(Chapter chapter){
        long xp = calculateChapterXp(chapter.getId());
        chapter.setTotalXp(xp);
        chapterRepository.save(chapter);

        Language lang = chapter.getLanguage();
        lang.setTotalXp(calculateLanguageXp(lang.getName()));
        languageRepository.save(lang);
    }
    // Tính XP của 1 chapter — query trực tiếp, không save
    private long calculateChapterXp(String chapterId) {
        return lessonRepository.sumXpByChapterId(chapterId, ContentStatus.ACTIVE);
    }

    // Tính XP của toàn bộ language
    private long calculateLanguageXp(String languageName) {
        return lessonRepository.sumXpByLanguageName(languageName, ContentStatus.ACTIVE);
    }
}