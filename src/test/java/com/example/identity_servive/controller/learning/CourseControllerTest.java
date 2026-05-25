/* (C)2026 */
package com.example.identity_servive.controller.learning;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.identity_servive.dto.request.learningRequest.*;
import com.example.identity_servive.dto.response.learningResponse.*;
import com.example.identity_servive.service.learning.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Slf4j
@SpringBootTest
@TestPropertySource("/test.properties")
@AutoConfigureMockMvc
public class CourseControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private CourseService courseService;

  private final String STEP_ID = "step-1";
  private final String PROBLEM_ID = "problem-1";
  private final String LESSON_ID = "lesson-1";
  private final String CHAPTER_ID = "chapter-1";
  private final String LANGUAGE = "java";

  private StepResponse stepResponse;
  private ProblemResponse problemResponse;
  private LessonResponse lessonResponse;
  private ChapterResponse chapterResponse;
  private LanguageResponse languageResponse;

  @BeforeEach
  void initData() {
    stepResponse = StepResponse.builder().id(STEP_ID).title("Step 1").build();
    problemResponse = ProblemResponse.builder().id(PROBLEM_ID).title("Problem 1").build();
    lessonResponse = LessonResponse.builder().id(LESSON_ID).title("Lesson 1").build();
    chapterResponse = ChapterResponse.builder().id(CHAPTER_ID).title("Chapter 1").build();
    languageResponse = LanguageResponse.builder().name(LANGUAGE).description("Java").build();
  }

  // ───── CREATE ─────

  @Test
  void createStep_success() throws Exception {
    Mockito.when(courseService.createStep(any(StepRequest.class))).thenReturn(stepResponse);

    StepRequest req = StepRequest.builder().lessonId(LESSON_ID).title("Step 1").build();
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(req);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/course/steps")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.id").value(STEP_ID));
  }

  @Test
  void createProblem_success() throws Exception {
    Mockito.when(courseService.createProblem(any(ProblemRequest.class)))
        .thenReturn(problemResponse);

    ProblemRequest req = ProblemRequest.builder().lessonId(LESSON_ID).title("Problem 1").build();
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(req);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/course/problems")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.id").value(PROBLEM_ID));
  }

  @Test
  void createLesson_success() throws Exception {
    Mockito.when(courseService.createLesson(any(LessonRequest.class))).thenReturn(lessonResponse);

    LessonRequest req = LessonRequest.builder().chapterId(CHAPTER_ID).title("Lesson 1").build();
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(req);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/course/lessons")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.id").value(LESSON_ID));
  }

  @Test
  void createChapter_success() throws Exception {
    Mockito.when(courseService.createChapter(any(ChapterRequest.class)))
        .thenReturn(chapterResponse);

    ChapterRequest req = ChapterRequest.builder().languageName(LANGUAGE).title("Chapter 1").build();
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(req);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/course/chapters")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.id").value(CHAPTER_ID));
  }

  @Test
  void createLanguage_success() throws Exception {
    Mockito.when(courseService.createLanguage(any(LanguageRequest.class)))
        .thenReturn(languageResponse);

    LanguageRequest req = LanguageRequest.builder().name(LANGUAGE).build();
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(req);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/course/languages")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.name").value(LANGUAGE));
  }

  // Note: @PreAuthorize on CourseService is not evaluated when service is mocked,
  // so forbidden/authorization tests cannot be verified at controller level.
  // These tests are intentionally omitted.

  // ───── GET (single) ─────

  @Test
  void getStep_success() throws Exception {
    Mockito.when(courseService.getStepById(eq(STEP_ID))).thenReturn(stepResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/course/steps/{stepId}", STEP_ID)
                .with(user("user").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.id").value(STEP_ID));
  }

  @Test
  void getProblem_success() throws Exception {
    Mockito.when(courseService.getProblemById(eq(PROBLEM_ID))).thenReturn(problemResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/course/problems/{problemId}", PROBLEM_ID)
                .with(user("user").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.id").value(PROBLEM_ID));
  }

  @Test
  void getLesson_success() throws Exception {
    Mockito.when(courseService.getLessonById(eq(LESSON_ID))).thenReturn(lessonResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/course/lessons/{lessonId}", LESSON_ID)
                .with(user("user").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.id").value(LESSON_ID));
  }

  @Test
  void getChapter_success() throws Exception {
    Mockito.when(courseService.getChapterById(eq(CHAPTER_ID))).thenReturn(chapterResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/course/chapters/{chapterId}", CHAPTER_ID)
                .with(user("user").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.id").value(CHAPTER_ID));
  }

  @Test
  void getLanguage_success() throws Exception {
    Mockito.when(courseService.getLanguageById(eq(LANGUAGE))).thenReturn(languageResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/course/languages/{languageName}", LANGUAGE)
                .with(user("user").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.name").value(LANGUAGE));
  }

  // ───── GET (admin lists) ─────

  @Test
  void getStepsByLessonForAdmin_success() throws Exception {
    Mockito.when(courseService.getAllStepsByLessonForAdmin(eq(LESSON_ID)))
        .thenReturn(List.of(stepResponse));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/course/steps/admin/{lessonId}", LESSON_ID)
                .with(user("admin").roles("ADMIN")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result[0].id").value(STEP_ID));
  }

  @Test
  void getLanguages_success() throws Exception {
    Mockito.when(courseService.getLanguage()).thenReturn(List.of(languageResponse));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/course/languages").with(user("admin").roles("ADMIN")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result[0].name").value(LANGUAGE));
  }

  // ───── UPDATE ─────

  @Test
  void updateStep_success() throws Exception {
    StepUpdateRequest req = StepUpdateRequest.builder().title("Updated Step").build();
    StepResponse updated = StepResponse.builder().id(STEP_ID).title("Updated Step").build();
    Mockito.when(courseService.updateStep(eq(STEP_ID), any(StepUpdateRequest.class)))
        .thenReturn(updated);

    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(req);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/course/steps/{stepsId}", STEP_ID)
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.title").value("Updated Step"));
  }

  // ───── DELETE (soft) ─────

  @Test
  void deleteStep_success() throws Exception {
    Mockito.doNothing().when(courseService).deleteStep(eq(STEP_ID));

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/course/steps/{stepId}", STEP_ID)
                .with(user("admin").roles("ADMIN")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000));
  }

  // ───── PURGE (hard delete) ─────

  @Test
  void purgeStep_success() throws Exception {
    Mockito.doNothing().when(courseService).purgeStep(eq(STEP_ID));

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/course/steps/admin/{stepId}", STEP_ID)
                .with(user("admin").roles("ADMIN")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000));
  }
}
