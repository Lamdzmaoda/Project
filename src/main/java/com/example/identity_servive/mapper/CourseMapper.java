package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.*;
import com.example.identity_servive.dto.response.*;
import com.example.identity_servive.entity.*;
import org.mapstruct.*;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    // --- LANGUAGE MAPPING ---
    @Mapping(target = "chapters", ignore = true)
    Language toLanguage(LanguageRequest request);

    LanguageResponse toLanguageResponse(Language language);

    @Mapping(target = "chapters", ignore = true)
    void updateLanguage(LanguageUpdateRequest request, @MappingTarget Language language);


    // --- CHAPTER MAPPING ---
    @Mapping(target = "lessons", ignore = true)
    Chapter toChapter(ChapterRequest chapterRequest);

    @Mapping(target = "languageName", source = "language.name")
    ChapterResponse toChapterResponse(Chapter chapter);

    @Mapping(target = "lessons", ignore = true)
    void updateChapter(ChapterUpdateRequest request, @MappingTarget Chapter chapter);


    // --- LESSON MAPPING ---
    @Mapping(target = "steps", ignore = true)
    Lesson toLesson(LessonRequest lessonRequest);

    LessonResponse toLessonResponse(Lesson lesson);

    @Mapping(target = "steps", ignore = true)
    void updateLesson(LessonUpdateRequest request, @MappingTarget Lesson lesson);


    // --- STEP MAPPING ---

    // MapStruct tự động ánh xạ Object data (DTO) <-> Map data (Entity)
    Step toStep(StepRequest stepRequest);

    StepResponse toStepResponse(Step step);

    @Mapping(target = "data", ignore = true)
    void updateStep(StepUpdateRequest request, @MappingTarget Step step);

    /**
     * Helper method để MapStruct xử lý việc ép kiểu từ Object sang Map.
     * Vì đây là interface nên ta dùng default method.
     */
    default Map<String, Object> mapObjectToMap(Object data) {
        if (data instanceof Map) {
            return (Map<String, Object>) data;
        }
        return null;
    }
}