package com.example.democode3.features.ai.api;

import com.example.democode3.features.ai.model.ChatRequest;
import com.example.democode3.features.ai.model.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatApiService {

    @POST("/identity/chats")
    Call<ChatResponse> askAi(

            @Body ChatRequest request
    );
}