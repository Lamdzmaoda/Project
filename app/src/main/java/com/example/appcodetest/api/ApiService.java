package com.example.appcodetest.api;

import com.example.appcodetest.model.ApiResponse;
import com.example.appcodetest.model.AuthResponse;
import com.example.appcodetest.model.LoginRequest;
import com.example.appcodetest.model.RegisterRequest;
import com.example.appcodetest.model.UserResponse;

import okhttp3.MultipartBody;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // 🔐 LOGIN
    @POST("auth/token") // ✅ ĐÚNG BACKEND
    Call<AuthResponse> login(@Body LoginRequest request);

    // 🔥 REGISTER (ĐÚNG BACKEND: POST /users)
    @POST("users")
    Call<Object> register(@Body RegisterRequest request);

    // 🔥 UPDATE PROFILE
    @FormUrlEncoded
    @PUT("users/update")
    Call<Void> updateProfile(@Field("fullName") String name);

    // 🔥 UPLOAD AVATAR
    @Multipart
    @POST("users/upload-avatar")
    Call<Void> uploadAvatar(@Part MultipartBody.Part file);
    @GET("users/myInfo")
    Call<ApiResponse<UserResponse>> getMyInfo(
            @Header("Authorization") String token
    );
}