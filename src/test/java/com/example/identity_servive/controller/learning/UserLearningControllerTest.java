/* (C)2026 */
package com.example.identity_servive.controller.learning;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.identity_servive.dto.request.learningRequest.LessonBatchRequest;
import com.example.identity_servive.dto.request.learningRequest.PracticeSubmitRequest;
import com.example.identity_servive.dto.response.ai.CodeResponse;
import com.example.identity_servive.dto.response.learningResponse.*;
import com.example.identity_servive.dto.response.progress.LearingProgressResponse;
import com.example.identity_servive.dto.response.progress.UserChapterResponse;
import com.example.identity_servive.enums.Status;
import com.example.identity_servive.service.ai.ChatService;
import com.example.identity_servive.service.learning.CourseService;
import com.example.identity_servive.service.learning.LearningProgressService;
import com.example.identity_servive.service.learning.LearningService;
import com.example.identity_servive.service.learning.PracticeService;
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
public class UserLearningControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private CourseService courseService;
  @MockitoBean private LearningService learningService;
  @MockitoBean private PracticeService practiceService;
  @MockitoBean private LearningProgressService learningProgressService;
  @MockitoBean private ChatService chatService;

  private final String LESSON_ID = "lesson-1";
  private final String CHAPTER_ID = "chapter-1";
  private final String LANGUAGE = "java";
  private final String PROBLEM_ID = "problem-1";

  private StepResponse stepResponse;
  private ProblemResponse problemResponse;
  private LessonResponse lessonResponse;
  private ChapterResponse chapterResponse;
  private LanguageResponse languageResponse;
  private LessonBatchResponse batchResponse;
  private LearingProgressResponse progressResponse;
  private UserChapterResponse userChapterResponse;
  private CodeResponse codeResponse;
  private PracticeSubmitResponse practiceSubmitResponse;

  @BeforeEach
  void initData() {
    stepResponse = StepResponse.builder().id("step-1").title("Step 1").build();
    problemResponse = ProblemResponse.builder().id(PROBLEM_ID).title("Problem 1").build();
    lessonResponse = LessonResponse.builder().id(LESSON_ID).title("Lesson 1").build();
    chapterResponse = ChapterResponse.builder().id(CHAPTER_ID).title("Chapter 1").build();
    languageResponse = LanguageResponse.builder().name(LANGUAGE).description("Java").build();
    batchResponse =
        LessonBatchResponse.builder().totalXpGained(100L).isNewlyCompleted(true).build();
    progressResponse = LearingProgressResponse.builder().currentXp(50).totalXp(500).build();
    userChapterResponse = UserChapterResponse.builder().id(CHAPTER_ID).title("Chapter 1").build();
    codeResponse = CodeResponse.builder().status(Status.SUCCESS).passed(true).output("OK").build();
    practiceSubmitResponse =
        PracticeSubmitResponse.builder().passed(true).xp(100).message("Done!").build();
  }

  @Test
  void getStepByLessonId_success() throws Exception {
    Mockito.when(courseService.getStepByLesson(eq(LESSON_ID))).thenReturn(List.of(stepResponse));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/my-learning/lessons/{lessonId}/steps", LESSON_ID)
                .with(user("user").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result[0].id").value("step-1"));
  }

  @Test
  void getProblem_success() throws Exception {
    Mockito.when(courseService.getProblemByLesson(eq(LESSON_ID)))
        .thenReturn(List.of(problemResponse));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/my-learning/problems/{lessonId}/problems", LESSON_ID)
                .with(user("user").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result[0].id").value(PROBLEM_ID));
  }

  @Test
  void getLessonByChapterId_success() throws Exception {
    Mockito.when(courseService.getLessonByChapter(eq(CHAPTER_ID)))
        .thenReturn(List.of(lessonResponse));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/my-learning/chapters/{chapterId}/lessons", CHAPTER_ID)
                .with(user("user").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result[0].id").value(LESSON_ID));
  }

  @Test
  void getChapterByLanguageName_success() throws Exception {
    Mockito.when(courseService.getChapterByLanguage(eq(LANGUAGE)))
        .thenReturn(List.of(chapterResponse));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/my-learning/{languageName}/chapters", LANGUAGE)
                .with(user("user").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result[0].id").value(CHAPTER_ID));
  }

  @Test
  void getLearningProgress_success() throws Exception {
    Mockito.when(courseService.getLearningProgress(eq(LANGUAGE))).thenReturn(progressResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/my-learning/{language}/progress", LANGUAGE)
                .with(user("user").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.currentXp").value(50));
  }

  @Test
  void verifyLesson_success() throws Exception {
    Mockito.when(learningService.verifyLesson(any(LessonBatchRequest.class)))
        .thenReturn(batchResponse);

    LessonBatchRequest request =
        LessonBatchRequest.builder().lessonId(LESSON_ID).requestSteps(List.of()).build();
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(request);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/my-learning/verify")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.totalXpGained").value(100));
  }

  @Test
  void getMyLearning_success() throws Exception {
    Mockito.when(courseService.getMyLearning(eq(LANGUAGE)))
        .thenReturn(List.of(userChapterResponse));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/my-learning/{languageName}", LANGUAGE)
                .with(user("user").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result[0].id").value(CHAPTER_ID));
  }

  @Test
  void submitPractice_success() throws Exception {
    Mockito.when(practiceService.submit(anyString(), anyString())).thenReturn(codeResponse);

    PracticeSubmitRequest request =
        PracticeSubmitRequest.builder().problemId(PROBLEM_ID).code("print(1)").build();
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(request);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/my-learning/practice/submit")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.passed").value(true));
  }

  @Test
  void completePractice_success() throws Exception {
    Mockito.when(practiceService.complete(anyString())).thenReturn(practiceSubmitResponse);

    PracticeSubmitRequest request = PracticeSubmitRequest.builder().problemId(LESSON_ID).build();
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(request);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/my-learning/practice/complete")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.passed").value(true))
        .andExpect(jsonPath("result.xp").value(100));
  }

  @Test
  void resetProgress_success() throws Exception {
    Mockito.doNothing().when(learningProgressService).resetProgress(anyString());

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/my-learning")
                .with(user("user").roles("USER"))
                .param("languageName", LANGUAGE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000));
  }
}
