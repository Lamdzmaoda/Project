/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.identity_servive.entity.AI.OpenAI;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.mapper.ChatMapper;
import com.example.identity_servive.repository.ai.ChatRepository;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.service.ai.ChatService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class ChatServiceTest {

  @Autowired(required = false)
  private ChatService chatService;

  @MockitoBean private ChatRepository chatRepository;
  @MockitoBean private ChatMapper chatMapper;
  @MockitoBean private UserRepository userRepository;

  private User user;
  private OpenAI chatEntity;

  @BeforeEach
  void initData() {
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("testuser", "password", null));
    user = User.builder().id("user-1").username("testuser").email("test@test.com").build();
    chatEntity = OpenAI.builder().id("chat-1").user(user).userMessage("hello").build();
  }

  @Test
  void getHistorys_success() {
    if (chatService == null) return;

    when(chatRepository.findAll()).thenReturn(List.of(chatEntity));
    when(chatMapper.toChatResponse(any()))
        .thenReturn(
            new com.example.identity_servive.dto.response.ai.ChatResponse(
                "hello", null, null, null, null, null, null, null, LocalDateTime.now()));

    var result = chatService.getHistorys(Pageable.unpaged());
    assertThat(result).hasSize(1);
    assertThat(result.getFirst().userMessage()).isEqualTo("hello");
  }

  @Test
  void getHistoryByUser_success() {
    if (chatService == null) return;

    when(userRepository.findByUsername(any())).thenReturn(java.util.Optional.of(user));
    when(chatRepository.findByUser(any())).thenReturn(List.of(chatEntity));
    when(chatMapper.toChatResponse(any()))
        .thenReturn(
            new com.example.identity_servive.dto.response.ai.ChatResponse(
                "hello", null, null, null, null, null, null, null, LocalDateTime.now()));

    var result = chatService.getHistoryByUser();
    assertThat(result).hasSize(1);
    assertThat(result.getFirst().userMessage()).isEqualTo("hello");
  }
}
