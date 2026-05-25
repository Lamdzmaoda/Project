/* (C)2026 */
package com.example.identity_servive.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.identity_servive.dto.request.ai.ChatRequest;
import com.example.identity_servive.dto.response.ai.ChatResponse;
import com.example.identity_servive.service.ai.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Slf4j
@SpringBootTest
@TestPropertySource("/test.properties")
@AutoConfigureMockMvc
public class ChatControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private ChatService chatService;

  private ChatResponse chatResponse;

  @BeforeEach
  void initData() {
    chatResponse =
        new ChatResponse(
            "hello",
            "Hi there!",
            List.of("hint1"),
            "code",
            "Good job!",
            null,
            null,
            null,
            LocalDateTime.now());
  }

  @Test
  void chat_success() throws Exception {
    Mockito.when(chatService.chat(any(ChatRequest.class))).thenReturn(chatResponse);

    ChatRequest request = new ChatRequest("hello", "step-1", null);
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(request);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/chats")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.userMessage").value("hello"))
        .andExpect(jsonPath("result.aiExplanation").value("Hi there!"));
  }

  @Test
  void getChat_success() throws Exception {
    Mockito.when(chatService.getHistorys(Pageable.unpaged())).thenReturn(List.of(chatResponse));

    // Endpoint /chats GET yêu cầu ADMIN role để tránh leak chat history của tất cả user
    mockMvc
        .perform(MockMvcRequestBuilders.get("/chats").with(user("admin").roles("ADMIN")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result[0].userMessage").value("hello"));
  }

  @Test
  void getChatOfUser_success() throws Exception {
    Mockito.when(chatService.getHistoryByUser()).thenReturn(List.of(chatResponse));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/chats/user").with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result[0].userMessage").value("hello"));
  }
}
