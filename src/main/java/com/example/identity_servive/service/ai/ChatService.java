/* (C)2026 */
package com.example.identity_servive.service.ai;

import com.example.identity_servive.dto.request.ai.ChatRequest;
import com.example.identity_servive.dto.response.ai.ChatResponse;
import com.example.identity_servive.entity.AI.OpenAI;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Problem;
import com.example.identity_servive.entity.learning.Step;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.mapper.ChatMapper;
import com.example.identity_servive.repository.ai.ChatRepository;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.learning.ProblemRepository;
import com.example.identity_servive.repository.learning.StepRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatService {
  ChatClient chatClient;
  ChatMapper chatMapper;
  StepRepository stepRepository;
  ProblemRepository problemRepository;
  UserRepository userRepository;
  ChatRepository chatRepository;

  public ChatService(
      ChatClient.Builder builder,
      JdbcChatMemoryRepository jdbcChatMemoryRepository,
      ChatMapper chatMapper,
      StepRepository stepRepository,
      ProblemRepository problemRepository,
      ObjectMapper objectMapper,
      UserRepository userRepository,
      ChatRepository chatRepository) {
    this.chatMapper = chatMapper;
    this.stepRepository = stepRepository;
    this.problemRepository = problemRepository;
    this.userRepository = userRepository;
    this.chatRepository = chatRepository;

    ChatMemory chatMemory =
        MessageWindowChatMemory.builder()
            .chatMemoryRepository(jdbcChatMemoryRepository)
            .maxMessages(20)
            .build();

    this.chatClient =
        builder.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).build();
  }

  @PreAuthorize("hasRole('ADMIN')")
  public List<ChatResponse> getHistorys(Pageable pageable) {
    return chatRepository.findAll(pageable).stream().map(chatMapper::toChatResponse).toList();
  }

  public List<ChatResponse> getHistoryByUser() {
    var context = SecurityContextHolder.getContext();
    String name = context.getAuthentication().getName();

    User user =
        userRepository
            .findByUsername(name)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    List<OpenAI> chatHistoryEntities = chatRepository.findByUser(user);

    return chatHistoryEntities.stream().map(chatMapper::toChatResponse).toList();
  }

  public ChatResponse chat(ChatRequest chatRequest) throws JsonProcessingException {
    var context = SecurityContextHolder.getContext();
    String name = context.getAuthentication().getName();
    User user =
        userRepository
            .findByUsername(name)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    Step step = null;
    Problem problem = null;
    StringBuilder contextBuilder = new StringBuilder();

    if (chatRequest.stepId() != null) {
      step =
          stepRepository
              .findById(chatRequest.stepId())
              .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
      contextBuilder.append("--- Step hiện tại ---\n");
      contextBuilder.append("Tiêu đề: ").append(step.getTitle()).append("\n");
      contextBuilder.append("Loại: ").append(step.getType()).append("\n");
      contextBuilder.append("Nội dung: ").append(step.getData()).append("\n");
    }

    if (chatRequest.problemId() != null) {
      problem =
          problemRepository
              .findById(chatRequest.problemId())
              .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
      contextBuilder.append("--- Problem hiện tại ---\n");
      contextBuilder.append("Tiêu đề: ").append(problem.getTitle()).append("\n");
      contextBuilder.append("Độ khó: ").append(problem.getDifficulty()).append("\n");
      contextBuilder.append("Mô tả: ").append(problem.getDescription()).append("\n");
      contextBuilder.append("Gợi ý: ").append(problem.getHint()).append("\n");
    }

    String lessonContext = buildLessonContext(step, problem);

    SystemMessage systemMessage = characterPersonality();

    StringBuilder userPromptBuilder = new StringBuilder();
    if (contextBuilder.length() > 0) {
      userPromptBuilder.append("Context bài tập:\n").append(contextBuilder).append("\n");
    }
    if (lessonContext != null) {
      userPromptBuilder.append(lessonContext).append("\n");
    }
    userPromptBuilder
        .append("Câu hỏi của học viên: ")
        .append(chatRequest.message())
        .append("\n")
        .append("Tên của học viên: ")
        .append(user.getUsername());

    Prompt prompt =
        new Prompt(List.of(systemMessage, new UserMessage(userPromptBuilder.toString())));

    String conversationId = buildConversationId(user.getId(), step, problem);

    ChatResponse response =
        chatClient
            .prompt(prompt)
            .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
            .call()
            .entity(ChatResponse.class);

    OpenAI chatEntity =
        OpenAI.builder()
            .user(user)
            .step(step)
            .problem(problem)
            .userMessage(chatRequest.message())
            .hints(response.hints())
            .aiExplanation(response.aiExplanation())
            .suggestedCode(response.suggestedCode())
            .motivationMessage(response.motivationMessage())
            .build();

    chatRepository.save(chatEntity);

    return chatMapper.toChatResponse(chatEntity);
  }

  private String buildConversationId(String userId, Step step, Problem problem) {
    StringBuilder id = new StringBuilder(userId);
    if (step != null) {
      id.append("_step_").append(step.getId());
    }
    if (problem != null) {
      id.append("_problem_").append(problem.getId());
    }
    if (step == null && problem == null) {
      id.append("_general");
    }
    return id.toString();
  }

  private String buildLessonContext(Step step, Problem problem) {
    try {
      String lessonId = null;
      if (step != null && step.getLesson() != null) {
        lessonId = step.getLesson().getId();
      } else if (problem != null && problem.getLesson() != null) {
        lessonId = problem.getLesson().getId();
      }

      if (lessonId == null) return "";

      List<Step> relatedSteps = stepRepository.findAllByLessonIdOrderByOrderIndexAsc(lessonId);
      List<Problem> relatedProblems =
          problemRepository.findAllByLessonIdOrderByOrderIndexAsc(lessonId);

      StringBuilder sb = new StringBuilder("--- Các nội dung trong bài học này (để gợi ý) ---\n");

      if (!relatedSteps.isEmpty()) {
        sb.append("Steps:\n");
        for (Step s : relatedSteps) {
          sb.append("- ")
              .append(s.getTitle())
              .append(" (ID: ")
              .append(s.getId())
              .append(", loại: ")
              .append(s.getType())
              .append(")\n");
        }
      }

      if (!relatedProblems.isEmpty()) {
        sb.append("Problems:\n");
        for (Problem p : relatedProblems) {
          sb.append("- ")
              .append(p.getTitle())
              .append(" (ID: ")
              .append(p.getId())
              .append(", độ khó: ")
              .append(p.getDifficulty())
              .append(")\n");
        }
      }

      return sb.toString();
    } catch (Exception e) {
      log.warn("Không thể build lesson context: {}", e.getMessage());
      return "";
    }
  }

  private SystemMessage characterPersonality() {
    return new SystemMessage(
        """
Bạn là gia sư hỗ trợ học tập. Giúp học code thú vị và dễ hiểu hơn.
Bạn là một người có tính cách như thầy Gojo Satoru trong anime chú thuật hồi chiến (jujutsu kaisen): mạnh mẽ, tự tin, hài hước, đôi khi hơi kiêu ngạo nhưng rất tận tâm và thông minh.
Hãy tuân thủ các quy tắc sau:
1. Không bao giờ đưa ra đáp án trực tiếp cho các bài tập (QUIZ, CODE hoặc Problem) ngay lập tức.
2. Hãy đưa ra các gợi ý (hints) hoặc giải thích các khái niệm liên quan để học viên tự tìm ra câu trả lời.
3. Nếu học viên gặp lỗi code, hãy giải thích nguyên nhân lỗi và hướng dẫn cách sửa thay vì viết lại toàn bộ code cho họ.
4. Trả lời ngắn gọn, súc tích và sử dụng định dạng Markdown cho các đoạn code.
5. Trả lời cực kỳ tập trung, không lan man. Phần giải thích không quá 3 câu.
6. Có thể gợi ý học viên nên làm step hoặc problem nào tiếp theo dựa trên ngữ cảnh và tiến độ của họ. Nếu gợi ý, hãy điền ID tương ứng vào trường suggestedStepIds hoặc suggestedProblemIds trong JSON response.

BẮT BUỘC: Bạn phải trả về câu trả lời duy nhất dưới định dạng JSON chuẩn.
Không được thêm bất kỳ văn bản nào ngoài khối JSON.
""");
  }
}
