package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.*;
import com.example.identity_servive.dto.response.*;
import com.example.identity_servive.entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mapstruct.*;


/**
 * Đổi sang abstract class để có thể sử dụng ObjectMapper và xử lý logic phức tạp.
 */
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

    // Lấy name từ language để map vào languageName trong DTO
    @Mapping(target = "languageName", source = "language.name")
    public abstract ChapterResponse toChapterResponse(Chapter chapter);

    @Mapping(target = "lessons", ignore = true)
    public abstract void updateChapter(ChapterRequest request, @MappingTarget Chapter chapter);


    // --- LESSON MAPPING ---
    @Mapping(target = "steps", ignore = true)
    public abstract Lesson toLesson(LessonRequest lessonRequest);

    public abstract LessonResponse toLessonResponse(Lesson lesson);

    @Mapping(target = "steps", ignore = true)
    public abstract void updateLesson(LessonRequest request, @MappingTarget Lesson lesson);


    // --- STEP MAPPING (TRUNG TÂM CỦA VẤN ĐỀ) ---

    @Mapping(target = "data", ignore = true)
    public abstract Step toStep(StepRequest stepRequest);

    @Mapping(target = "data", ignore = true)
    public abstract StepResponse toStepResponse(Step step);

    @Mapping(target = "data", ignore = true)
    public abstract void updateStep(StepRequest request, @MappingTarget Step step);

    /**
     * Logic giải mã JSON: 
     * Sau khi MapStruct map xong các trường cơ bản, hàm này sẽ chạy để xử lý trường 'data'.
     * Nó giúp 'data' không còn bị null khi get Language hay Lesson nữa.
     */
    @AfterMapping
    protected void handleStepDataMapping(Step step, @MappingTarget StepResponse response) {
        try {
            if (step.getData() != null && !step.getData().isEmpty()) {
                // Chuyển chuỗi JSON (String) từ Database thành Object (Map/List) để trả về Client
                Object dataObject = objectMapper.readValue(step.getData(), Object.class);
                response.setData(dataObject);
            }
        } catch (Exception e) {
            // Nếu lỗi parse JSON thì để data là null để không crash ứng dụng
            response.setData(null);
        }
    }
}