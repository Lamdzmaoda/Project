package com.example.democode3.features.profile.repository;

import android.content.Context;

import com.example.democode3.core.model.ApiResponse;
import com.example.democode3.core.network.RetrofitClient;

import com.example.democode3.features.community.api.response.PostResponse;

import com.example.democode3.features.profile.api.ProfileApiService;

import com.example.democode3.features.profile.model.BooleanResponse;
import com.example.democode3.features.profile.model.CountResponse;
import com.example.democode3.features.profile.model.FollowResponse;
import com.example.democode3.features.profile.model.ProfileResult;
import com.example.democode3.features.profile.model.UserProfileResponse;

import retrofit2.Callback;

public class ProfileRepository {

    private final ProfileApiService api;

    public ProfileRepository(
            Context context
    ) {

        api =
                RetrofitClient
                        .getInstance(context)
                        .create(ProfileApiService.class);
    }

    // =====================================
    // MY INFO
    // =====================================

    public void getMyInfo(

            Callback<ApiResponse<UserProfileResponse>> callback
    ) {

        api.getMyInfo()
                .enqueue(callback);
    }

    // =====================================
    // USER PROFILE
    // =====================================

    public void getUserProfile(

            String userId,

            Callback<ApiResponse<ProfileResult>> callback
    ) {

        api.getUserProfile(userId)
                .enqueue(callback);
    }

    // =====================================
    // FOLLOW
    // =====================================

    public void followUser(

            String userId,

            Callback<FollowResponse> callback
    ) {

        api.followUser(userId)
                .enqueue(callback);
    }

    // =====================================
    // UNFOLLOW
    // =====================================

    public void unfollowUser(

            String userId,

            Callback<FollowResponse> callback
    ) {

        api.unfollowUser(userId)
                .enqueue(callback);
    }

    // =====================================
    // CHECK FOLLOW
    // =====================================

    public void checkFollow(

            String userId,

            Callback<BooleanResponse> callback
    ) {

        api.checkFollow(userId)
                .enqueue(callback);
    }

    // =====================================
    // FOLLOWERS COUNT
    // =====================================

    public void getFollowersCount(

            String userId,

            Callback<CountResponse> callback
    ) {

        api.getFollowersCount(userId)
                .enqueue(callback);
    }

    // =====================================
    // FOLLOWING COUNT
    // =====================================

    public void getFollowingCount(

            String userId,

            Callback<CountResponse> callback
    ) {

        api.getFollowingCount(userId)
                .enqueue(callback);
    }

    // =====================================
    // USER POSTS
    // =====================================

    public void getUserPosts(

            String userId,

            Callback<PostResponse> callback
    ) {

        api.getUserPosts(
                userId,
                0,
                20
        ).enqueue(callback);
    }

    // =====================================
    // SEARCH USERS
    // =====================================

    public void searchUsers(

            String keyword,

            Callback<ApiResponse<ProfileResult>> callback
    ) {

        api.searchUsers(keyword)
                .enqueue(callback);
    }
}