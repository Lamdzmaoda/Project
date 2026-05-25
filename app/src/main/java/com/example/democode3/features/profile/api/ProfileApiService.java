package com.example.democode3.features.profile.api;

import com.example.democode3.core.model.ApiResponse;

import com.example.democode3.features.community.api.response.PostResponse;

import com.example.democode3.features.profile.model.BooleanResponse;
import com.example.democode3.features.profile.model.CountResponse;
import com.example.democode3.features.profile.model.FollowResponse;
import com.example.democode3.features.profile.model.ProfileResult;
import com.example.democode3.features.profile.model.UserProfileResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProfileApiService {

    // =====================================
    // MY INFO
    // =====================================

    @GET("users/myInfo")
    Call<ApiResponse<UserProfileResponse>> getMyInfo();

    // =====================================
    // USER INFO
    // =====================================

    @GET("users/{userId}")
    Call<ApiResponse<ProfileResult>> getUserProfile(

            @Path("userId")
            String userId
    );

    // =====================================
    // FOLLOW
    // =====================================

    @POST("api/community/follow/{followeeId}")
    Call<FollowResponse> followUser(

            @Path("followeeId")
            String followeeId
    );

    // =====================================
    // UNFOLLOW
    // =====================================

    @DELETE("api/community/follow/{followeeId}")
    Call<FollowResponse> unfollowUser(

            @Path("followeeId")
            String followeeId
    );

    // =====================================
    // CHECK FOLLOW
    // =====================================

    @GET("api/community/follow/{followeeId}/check")
    Call<BooleanResponse> checkFollow(

            @Path("followeeId")
            String followeeId
    );

    // =====================================
    // FOLLOWERS COUNT
    // =====================================

    @GET("api/community/users/{userId}/followers/count")
    Call<CountResponse> getFollowersCount(

            @Path("userId")
            String userId
    );

    // =====================================
    // FOLLOWING COUNT
    // =====================================

    @GET("api/community/users/{userId}/following/count")
    Call<CountResponse> getFollowingCount(

            @Path("userId")
            String userId
    );

    // =====================================
    // USER POSTS
    // =====================================

    @GET("api/community/users/{userId}/posts")
    Call<PostResponse> getUserPosts(

            @Path("userId")
            String userId,

            @Query("page")
            int page,

            @Query("size")
            int size
    );

    // =====================================
    // SEARCH USERS
    // =====================================

    @GET("users/search")
    Call<ApiResponse<ProfileResult>> searchUsers(

            @Query("keyword")
            String keyword
    );
}