package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.*;
import com.example.identity_servive.dto.response.*;
import com.example.identity_servive.entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class CourseMapper {

    protected final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    // --- LANGUAGE MAPPING ---
    @Mapping(target = "chapters", ignore = true)
    public abstract Language toLanguage(LanguageRequest request);

    public abstract LanguageResponse toLanguageResponse(Language language);

    @Mapping(target = "chapters", ignore = true)
    public abstract void updateLanguage(LanguageUpdateRequest request, @MappingTarget Language language);


    // --- CHAPTER MAPPING ---
    @Mapping(target = "lessons", ignore = true)
    public abstract Chapter toChapter(ChapterRequest chapterRequest);

    // Map tên ngôn ngữ từ đối tượng Language vào trường languageName của DTO
    @Mapping(target = "languageName", source = "language.name")
    public abstract ChapterResponse toChapterResponse(Chapter chapter);

    @Mapping(target = "lessons", ignore = true)
    public abstract void updateChapter(ChapterRequest request, @MappingTarget Chapter chapter);


    // --- LESSON MAPPING ---
    @Mapping(target = "steps", ignore = true)
    public abstract Lesson toLesson(LessonRequest lessonRequest);

    // Không map ngược lại Chapter để tránh vòng lặp đệ quy
    public abstract LessonResponse toLessonResponse(Lesson lesson);

    @Mapping(target = "steps", ignore = true)
    public abstract void updateLesson(LessonRequest request, @MappingTarget Lesson lesson);


    // --- STEP MAPPING ---
    @Mapping(target = "data", ignore = true)
    public abstract Step toStep(StepRequest stepRequest);

    // Không map ngược lại Lesson để tránh vòng lặp đệ quy
    @Mapping(target = "data", ignore = true)
    public abstract StepResponse toStepResponse(Step step);

    @Mapping(target = "data", ignore = true)
    public abstract void updateStep(StepRequest request, @MappingTarget Step step);

    /**
     * Logic giải mã JSON:
     * Xử lý trường 'data' từ String (DB) sang Object (Client).
     */
    @AfterMapping
    protected void handleStepDataMapping(Step step, @MappingTarget StepResponse response) {
        try {
            if (step.getData() != null && !step.getData().isEmpty()) {
                Object dataObject = objectMapper.readValue(step.getData(), Object.class);
                response.setData(dataObject);
            }
        } catch (Exception e) {
            response.setData(null);
        }
    }
}