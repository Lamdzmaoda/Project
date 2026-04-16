package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.ChatRequest;
import com.example.identity_servive.dto.response.ChatResponse;
import com.example.identity_servive.entity.OpenAI;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    OpenAI toChat(ChatRequest chatRequest);
    ChatResponse toChatResponse(OpenAI openAI);
}
