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
    Call<AuthResponse> login(
            @Body LoginRequest request
    );

    // =========================
    // 📝 REGISTER
    // =========================

    @POST("users")
    Call<Object> register(
            @Body RegisterRequest request
    );

    // =========================
    // 👤 USER
    // =========================

    @GET("users/myInfo")
    Call<ApiResponse<UserResponse>> getMyInfo(
            @Header("Authorization") String token
    );

    // update profile (cần token)
    @FormUrlEncoded
    @PUT("users/update")
    Call<Void> updateProfile(
            @Header("Authorization") String token,
            @Field("fullName") String name
    );

    @Multipart
    @POST("users/upload-avatar")
    Call<Void> uploadAvatar(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file
    );

    // =========================
    // 📚 GET DATA
    // =========================
    // 🔥 QUAN TRỌNG:
    // dùng @Query bản cũ chuẩn backend thật
    // KHÔNG dùng BetterLanguage / BetterChapter / BetterLesson
    // =========================

    @GET("course/languages")
    Call<ApiResponse<List<Language>>> getLanguages(
            @Header("Authorization") String token
    );

    // ✅ CHAPTER
    @GET("course/chapters")
    Call<ApiResponse<List<Chapter>>> getChapters(
            @Query("languageName") String languageName,
            @Header("Authorization") String token
    );

    // ✅ LESSON
    @GET("course/lessons")
    Call<ApiResponse<List<Lesson>>> getLessons(
            @Query("chapterId") String chapterId,
            @Header("Authorization") String token
    );

    // ✅ STEP
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
    // ✏️ UPDATE
    // =========================
    // language update dùng NAME
    // còn chapter/lesson/step dùng ID thật
    // =========================

    @PUT("course/languages/{name}")
    Call<Object> updateLanguage(
            @Header("Authorization") String token,
            @Path("name") String name,
            @Body RequestBody body
    );

    @PUT("course/chapters/{id}")
    Call<Object> updateChapter(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Body RequestBody body
    );

    @PUT("course/lessons/{id}")
    Call<Object> updateLesson(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Body RequestBody body
    );

    @PUT("course/steps/{id}")
    Call<Object> updateStep(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Body RequestBody body
    );

    // =========================
    // ❌ DELETE
    // =========================
    // language delete dùng NAME
    // còn lại dùng ID thật
    // =========================

    @DELETE("course/languages/{name}")
    Call<Object> deleteLanguage(
            @Header("Authorization") String token,
            @Path(value = "name", encoded = true) String name
    );

    @DELETE("course/chapters/{id}")
    Call<Object> deleteChapter(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    @DELETE("course/lessons/{id}")
    Call<Object> deleteLesson(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    @DELETE("course/steps/{id}")
    Call<Object> deleteStep(
            @Header("Authorization") String token,
            @Path("id") String id
    );


    // =========================
// 👑 ADMIN - USER MANAGEMENT
// =========================

    // 🔥 Lấy danh sách user
    @GET("users")
    Call<ApiResponse<List<UserResponse>>> getUsers(
            @Header("Authorization") String token
    );

    // 🔥 Xóa user
    @DELETE("users/{id}")
    Call<Object> deleteUser(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    // 🔥 Update user (nếu backend có)
    @PUT("users/{id}")
    Call<Object> updateUser(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Body RequestBody body
    );
}