package com.example.identity_servive.service;

import com.example.identity_servive.dto.request.ChatRequest;
import com.example.identity_servive.dto.response.ChatResponse;
import com.example.identity_servive.entity.OpenAI;
import com.example.identity_servive.entity.Step;
import com.example.identity_servive.entity.User;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.mapper.ChatMapper;
import com.example.identity_servive.repository.ChatRepository;
import com.example.identity_servive.repository.StepRepository;
import com.example.identity_servive.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatService {
    ChatClient chatClient;
    JdbcChatMemoryRepository jdbcChatMemoryRepository;
    ChatMapper chatMapper;
    StepRepository stepRepository;
    ObjectMapper objectMapper;
    UserRepository userRepository;
    ChatRepository chatRepository;

    public ChatService(ChatClient.Builder builder, JdbcChatMemoryRepository jdbcChatMemoryRepository, ChatMapper chatMapper,
                       StepRepository stepRepository, ObjectMapper objectMapper, UserRepository userRepository,
                       ChatRepository chatRepository) {
        this.jdbcChatMemoryRepository = jdbcChatMemoryRepository;
        this.chatMapper = chatMapper;
        this.stepRepository = stepRepository;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(20)
                .build();

        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
    public List<ChatResponse> getHistorys(){
        return chatRepository.findAll().stream()
                .map(chatMapper::toChatResponse).toList();
    }
    public List<ChatResponse> getHistoryByUser(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<OpenAI> chatHistoryEntities = chatRepository.findByUser(user);

        return chatHistoryEntities.stream()
                .map(chatMapper::toChatResponse)
                .toList();
    }

    public ChatResponse chat(ChatRequest chatRequest) throws JsonProcessingException {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Step step = stepRepository.findById(chatRequest.stepId())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

        Map<String, Object> data = step.getData();
        String stepContext = "Tiêu đề bài học: " + step.getTitle() + "\n"
                + "Loại bài tập: " + step.getType() + "\n"
                + "Nội dung chi tiết: " + data.toString();

        SystemMessage systemMessage = characterPersonality();

        String userPromptContent = "Context bài tập:\n" + stepContext + "\n\nCâu hỏi của học viên: " + chatRequest.message() + "\nTên của học viên: " + user.getUsername();
        Prompt prompt = new Prompt(List.of(systemMessage, new UserMessage(userPromptContent)));

        String conversationId = user.getId() + "_" + step.getId();

        ChatResponse response = chatClient
                .prompt(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .entity(ChatResponse.class);

        OpenAI chatEntity = OpenAI.builder()
                .user(user)
                .step(step)
                .userMessage(chatRequest.message())
                .hints(response.hints())
                .aiExplanation(response.aiExplanation())
                .suggestedCode(response.suggestedCode())
                .motivationMessage(response.motivationMessage())
                .build();

        chatRepository.save(chatEntity);

        return chatMapper.toChatResponse(chatEntity);
    }
    private SystemMessage characterPersonality(){
        return new SystemMessage("""
                Bạn là gia sư hỗ trợ học tập. Giúp học code thú vị và dễ hiểu hơn.
                Bạn là một người có tính cách như thầy Gojo Satoru trong anime chú thuật hồi chiến (jujutsu kaisen): mạnh mẽ, tự tin, hài hước, đôi khi hơi kiêu ngạo nhưng rất tận tâm và thông minh.
                Hãy tuân thủ các quy tắc sau:
                1. Không bao giờ đưa ra đáp án trực tiếp cho các bài tập (QUIZ hoặc CODE) ngay lập tức.
                2. Hãy đưa ra các gợi ý (hints) hoặc giải thích các khái niệm liên quan để học viên tự tìm ra câu trả lời.
                3. Nếu học viên gặp lỗi code, hãy giải thích nguyên nhân lỗi và hướng dẫn cách sửa thay vì viết lại toàn bộ code cho họ.
                4. Trả lời ngắn gọn, súc tích và sử dụng định dạng Markdown cho các đoạn code.
                5. Trả lời cực kỳ tập trung, không lan man. Phần giải thích không quá 3 câu.
                
                BẮT BUỘC: Bạn phải trả về câu trả lời duy nhất dưới định dạng JSON chuẩn.
                Không được thêm bất kỳ văn bản nào ngoài khối JSON.
                """);

    }
}
