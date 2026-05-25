package com.example.democode3.features.community.repository;

import android.content.Context;

import com.example.democode3.core.network.RetrofitClient;

import com.example.democode3.features.community.api.CommunityApiService;

import com.example.democode3.features.community.api.request.CreateCommentRequest;
import com.example.democode3.features.community.api.request.CreatePostRequest;

import com.example.democode3.features.community.api.response.CommentResponse;
import com.example.democode3.features.community.api.response.PostResponse;
import com.example.democode3.features.community.api.response.UploadImageResponse;

import okhttp3.MultipartBody;

import retrofit2.Callback;

public class CommunityRepository {

    // =====================================
    // API
    // =====================================

    private final CommunityApiService api;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public CommunityRepository(
            Context context
    ) {

        api =
                RetrofitClient

                        .getInstance(context)

                        .create(
                                CommunityApiService.class
                        );
    }

    // =====================================
    // POSTS
    // =====================================

    public void getPosts(

            Callback<PostResponse> callback
    ) {

        api.getPosts()
                .enqueue(callback);
    }

    // =====================================
    // CREATE POST
    // =====================================

    public void createPost(

            CreatePostRequest request,

            Callback<Void> callback
    ) {

        api.createPost(request)
                .enqueue(callback);
    }

    // =====================================
    // DELETE POST
    // =====================================

    public void deletePost(

            String postId,

            Callback<Void> callback
    ) {

        api.deletePost(postId)
                .enqueue(callback);
    }

    // =====================================
    // LIKE POST
    // =====================================

    public void likePost(

            String postId,

            Callback<Void> callback
    ) {

        api.likePost(postId)
                .enqueue(callback);
    }

    // =====================================
    // UNLIKE POST
    // =====================================

    public void unlikePost(

            String postId,

            Callback<Void> callback
    ) {

        api.unlikePost(postId)
                .enqueue(callback);
    }

    // =====================================
    // SAVE POST
    // =====================================

    public void savePost(

            String postId,

            Callback<Void> callback
    ) {

        api.savePost(postId)
                .enqueue(callback);
    }

    // =====================================
    // UNSAVE POST
    // =====================================

    public void unsavePost(

            String postId,

            Callback<Void> callback
    ) {

        api.unsavePost(postId)
                .enqueue(callback);
    }

    // =====================================
    // COMMENTS
    // =====================================

    public void getComments(

            String postId,

            Callback<CommentResponse> callback
    ) {

        api.getComments(postId)
                .enqueue(callback);
    }

    // =====================================
    // CREATE COMMENT
    // =====================================

    public void createComment(

            CreateCommentRequest request,

            Callback<Void> callback
    ) {

        api.createComment(request)
                .enqueue(callback);
    }

    // =====================================
    // UPLOAD IMAGE
    // =====================================

    public void uploadImage(

            MultipartBody.Part file,

            Callback<UploadImageResponse> callback
    ) {

        api.uploadImage(file)
                .enqueue(callback);
    }
}