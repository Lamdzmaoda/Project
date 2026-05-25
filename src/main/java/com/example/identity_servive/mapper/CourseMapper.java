/* (C)2026 */
package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.learningRequest.*;
import com.example.identity_servive.dto.response.learningResponse.*;
import com.example.identity_servive.entity.learning.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.mapstruct.*;

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
  @Mapping(target = "problems", ignore = true)
  @Mapping(target = "steps", ignore = true)
  Lesson toLesson(LessonRequest lessonRequest);

  LessonResponse toLessonResponse(Lesson lesson);

  @Mapping(target = "problems", ignore = true)
  @Mapping(target = "steps", ignore = true)
  void updateLesson(LessonUpdateRequest request, @MappingTarget Lesson lesson);

  // --- STEP MAPPING ---

  // MapStruct tự động ánh xạ Object data (DTO) <-> Map data (Entity)
  Step toStep(StepRequest stepRequest);

  StepResponse toStepResponse(Step step);

  @Mapping(target = "data", ignore = true)
  void updateStep(StepUpdateRequest request, @MappingTarget Step step);

  Problem toProblem(ProblemRequest problemRequest);

  @Mapping(target = "conditions", expression = "java(mapConditions(problem.getConditions()))")
  ProblemResponse toProblemResponse(Problem problem);

  @Mapping(target = "conditions", ignore = true)
  void updateProblem(ProblemUpdateRequest request, @MappingTarget Problem problem);

  default List<ProblemConditionResponse> mapConditions(Set<ProblemCondition> conditions) {
    if (conditions == null) return List.of();
    return conditions.stream()
        .map(
            c ->
                ProblemConditionResponse.builder()
                    .expectedCode(c.getExpectedCode())
                    .orderIndex(c.getOrderIndex())
                    .hint(c.getHint())
                    .build())
        .toList();
  }

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
