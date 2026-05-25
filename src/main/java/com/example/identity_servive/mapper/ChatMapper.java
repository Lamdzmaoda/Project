/* (C)2026 */
package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.ai.ChatRequest;
import com.example.identity_servive.dto.response.ai.ChatResponse;
import com.example.identity_servive.entity.AI.OpenAI;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMapper {
  OpenAI toChat(ChatRequest chatRequest);

  ChatResponse toChatResponse(OpenAI openAI);
}
