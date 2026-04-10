package com.example.appcodetest.api;

import com.example.appcodetest.model.*;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // =========================
    // 🔐 LOGIN
    // =========================
    @POST("auth/token")
    Call<AuthResponse> login(@Body LoginRequest request);

    // =========================
    // 📝 REGISTER
    // =========================
    @POST("users")
    Call<Object> register(@Body RegisterRequest request);

    // =========================
    // 👤 USER
    // =========================
    @GET("users/myInfo")
    Call<ApiResponse<UserResponse>> getMyInfo(
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @PUT("users/update")
    Call<Void> updateProfile(@Field("fullName") String name);

    @Multipart
    @POST("users/upload-avatar")
    Call<Void> uploadAvatar(@Part MultipartBody.Part file);

    // =========================
    // 📚 GET DATA
    // =========================

    // 🚀 LANGUAGE
    @GET("course/languages")
    Call<ApiResponse<List<Language>>> getLanguages(
            @Header("Authorization") String token
    );

    // 🚀 CHAPTER
    @GET("course/chapters")
    Call<ApiResponse<List<Chapter>>> getChapters(
            @Query("languageName") String languageName,
            @Header("Authorization") String token
    );

    // 🚀 LESSON
    @GET("course/lessons")
    Call<ApiResponse<List<Lesson>>> getLessons(
            @Query("chapterId") String chapterId,
            @Header("Authorization") String token
    );

    // 🚀 STEP
    @GET("course/steps")
    Call<ApiResponse<List<LessonStep>>> getSteps(
            @Query("lessonId") String lessonId,
            @Header("Authorization") String token
    );

    // =========================
    // 🔥 CREATE
    // =========================

    @POST("course/languages")
    Call<Object> createLanguage(
            @Header("Authorization") String token,
            @Body RequestBody body
    );

    @POST("course/chapters")
    Call<Object> createChapter(
            @Header("Authorization") String token,
            @Body RequestBody body
    );

    @POST("course/lessons")
    Call<Object> createLesson(
            @Header("Authorization") String token,
            @Body RequestBody body
    );

    @POST("course/steps")
    Call<Object> createStep(
            @Header("Authorization") String token,
            @Body RequestBody body
    );

    // =========================
    // 🔥 UPDATE
    // =========================

    // LANGUAGE (🔥 dùng name thay id)
    @PUT("course/languages/{name}")
    Call<Object> updateLanguage(
            @Header("Authorization") String token,
            @Path("name") String name,
            @Body RequestBody body
    );

    // CHAPTER (dùng id vì backend có id)
    @PUT("course/chapters/{id}")
    Call<Object> updateChapter(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Body RequestBody body
    );

    // LESSON
    @PUT("course/lessons/{id}")
    Call<Object> updateLesson(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Body RequestBody body
    );

    // STEP
    @PUT("course/steps/{id}")
    Call<Object> updateStep(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Body RequestBody body
    );

    // =========================
    // ❌ DELETE
    // =========================

    // LANGUAGE (🔥 dùng name)
    @DELETE("course/languages/{name}")
    Call<Object> deleteLanguage(
            @Header("Authorization") String token,
            @Path("name") String name
    );

    // CHAPTER
    @DELETE("course/chapters/{id}")
    Call<Object> deleteChapter(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    // LESSON
    @DELETE("course/lessons/{id}")
    Call<Object> deleteLesson(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    // STEP
    @DELETE("course/steps/{id}")
    Call<Object> deleteStep(
            @Header("Authorization") String token,
            @Path("id") String id
    );
}