package com.example.democode3.features.community.api;

import com.example.democode3.features.community.api.request.CreateCommentRequest;
import com.example.democode3.features.community.api.request.CreatePostRequest;
import com.example.democode3.features.community.api.response.CommentResponse;
import com.example.democode3.features.community.api.response.PostResponse;
import com.example.democode3.features.community.api.response.UploadImageResponse;

import okhttp3.MultipartBody;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface CommunityApiService {

    // =====================================
    // POSTS
    // =====================================

    @GET("api/community/posts")
    Call<PostResponse> getPosts();

    // =====================================
    // CREATE POST
    // =====================================

    @POST("api/community/posts")
    Call<Void> createPost(

            @Body
            CreatePostRequest request
    );

    // =====================================
    // DELETE POST
    // =====================================

    @DELETE("api/community/posts/{id}")
    Call<Void> deletePost(

            @Path("id")
            String postId
    );

    // =====================================
    // LIKE POST
    // =====================================

    @POST("api/community/posts/{id}/like")
    Call<Void> likePost(

            @Path("id")
            String postId
    );

    // =====================================
    // UNLIKE POST
    // =====================================

    @DELETE("api/community/posts/{id}/like")
    Call<Void> unlikePost(

            @Path("id")
            String postId
    );

    // =====================================
    // SAVE POST
    // =====================================

    @POST("api/community/posts/{id}/save")
    Call<Void> savePost(

            @Path("id")
            String postId
    );

    // =====================================
    // UNSAVE POST
    // =====================================

    @DELETE("api/community/posts/{id}/save")
    Call<Void> unsavePost(

            @Path("id")
            String postId
    );

    // =====================================
    // COMMENTS
    // =====================================

    @GET("api/community/posts/{id}/comments")
    Call<CommentResponse> getComments(

            @Path("id")
            String postId
    );

    // =====================================
    // CREATE COMMENT
    // =====================================

    @POST("api/community/comments")
    Call<Void> createComment(

            @Body
            CreateCommentRequest request
    );

    // =====================================
    // UPLOAD IMAGE
    // =====================================

    @Multipart
    @POST("api/community/upload-image")
    Call<UploadImageResponse> uploadImage(

            @Part MultipartBody.Part file
    );
}