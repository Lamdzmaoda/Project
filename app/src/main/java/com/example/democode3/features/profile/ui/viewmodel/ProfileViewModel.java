package com.example.democode3.features.profile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.democode3.core.model.ApiResponse;

import com.example.democode3.features.community.api.response.PostResponse;
import com.example.democode3.features.community.model.Post;

import com.example.democode3.features.profile.model.BooleanResponse;
import com.example.democode3.features.profile.model.CountResponse;
import com.example.democode3.features.profile.model.FollowResponse;
import com.example.democode3.features.profile.model.ProfileResult;
import com.example.democode3.features.profile.model.ProfileResponse;
import com.example.democode3.features.profile.model.UserProfileResponse;

import com.example.democode3.features.profile.repository.ProfileRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel
        extends AndroidViewModel {

    // =====================================
    // PROFILE
    // =====================================

    private final MutableLiveData<UserProfileResponse>
            profile =
            new MutableLiveData<>();

    // =====================================
    // POSTS
    // =====================================

    private final MutableLiveData<List<Post>>
            posts =
            new MutableLiveData<>();

    // =====================================
    // FOLLOWED
    // =====================================

    private final MutableLiveData<Boolean>
            followed =
            new MutableLiveData<>(false);

    // =====================================
    // FOLLOWERS
    // =====================================

    private final MutableLiveData<Integer>
            followersCount =
            new MutableLiveData<>(0);

    // =====================================
    // FOLLOWING
    // =====================================

    private final MutableLiveData<Integer>
            followingCount =
            new MutableLiveData<>(0);

    // =====================================
    // REPOSITORY
    // =====================================

    private final ProfileRepository repository;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public ProfileViewModel(
            @NonNull Application application
    ) {

        super(application);

        repository =
                new ProfileRepository(
                        application
                );
    }

    // =====================================
    // PROFILE
    // =====================================

    public LiveData<UserProfileResponse>
    getProfile() {

        return profile;
    }

    // =====================================
    // POSTS
    // =====================================

    public LiveData<List<Post>>
    getPosts() {

        return posts;
    }

    // =====================================
    // FOLLOWED
    // =====================================

    public LiveData<Boolean>
    isFollowed() {

        return followed;
    }

    // =====================================
    // FOLLOWERS
    // =====================================

    public LiveData<Integer>
    getFollowersCount() {

        return followersCount;
    }

    // =====================================
    // FOLLOWING
    // =====================================

    public LiveData<Integer>
    getFollowingCount() {

        return followingCount;
    }

    // =====================================
    // LOAD MY PROFILE
    // =====================================

    public void loadMyProfile() {

        repository.getMyInfo(

                new Callback<ApiResponse<UserProfileResponse>>() {

                    @Override
                    public void onResponse(

                            Call<ApiResponse<UserProfileResponse>> call,

                            Response<ApiResponse<UserProfileResponse>> response
                    ) {

                        if (

                                response.isSuccessful()

                                        &&

                                        response.body() != null

                                        &&

                                        response.body().result != null
                        ) {

                            profile.setValue(
                                    response.body().result
                            );
                        }
                    }

                    @Override
                    public void onFailure(

                            Call<ApiResponse<UserProfileResponse>> call,

                            Throwable t
                    ) {

                        t.printStackTrace();
                    }
                }
        );
    }

    // =====================================
    // LOAD USER PROFILE
    // =====================================

    public void loadUserProfile(
            String userId
    ) {

        repository.getUserProfile(

                userId,

                new Callback<ApiResponse<ProfileResult>>() {

                    @Override
                    public void onResponse(

                            Call<ApiResponse<ProfileResult>> call,

                            Response<ApiResponse<ProfileResult>> response
                    ) {

                        if (

                                response.isSuccessful()

                                        &&

                                        response.body() != null

                                        &&

                                        response.body().result != null
                        ) {

                            ProfileResult result =
                                    response.body().result;

                            UserProfileResponse user =
                                    new UserProfileResponse();

                            user.id =
                                    result.id;

                            user.username =
                                    result.username;

                            user.displayName =
                                    result.displayName;

                            user.email =
                                    result.email;

                            user.avatarUrl =
                                    result.avatarUrl;

                            user.bio =
                                    result.bio;

                            user.level =
                                    result.level;

                            user.xp =
                                    result.xp;

                            user.coin =
                                    result.coin;

                            user.streak =
                                    result.streak;

                            user.verified =
                                    result.verified;

                            user.roles =
                                    result.roles;

                            profile.setValue(user);
                        }
                    }

                    @Override
                    public void onFailure(

                            Call<ApiResponse<ProfileResult>> call,

                            Throwable t
                    ) {

                        t.printStackTrace();
                    }
                }
        );

        loadFollowStatus(userId);

        loadFollowersCount(userId);

        loadFollowingCount(userId);

        loadPosts(userId);
    }

    // =====================================
    // FOLLOW STATUS
    // =====================================

    public void loadFollowStatus(
            String userId
    ) {

        repository.checkFollow(

                userId,

                new Callback<BooleanResponse>() {

                    @Override
                    public void onResponse(

                            Call<BooleanResponse> call,

                            Response<BooleanResponse> response
                    ) {

                        if (

                                response.isSuccessful()

                                        &&

                                        response.body() != null
                        ) {

                            followed.setValue(
                                    response.body().result
                            );
                        }
                    }

                    @Override
                    public void onFailure(

                            Call<BooleanResponse> call,

                            Throwable t
                    ) {

                        t.printStackTrace();
                    }
                }
        );
    }

    // =====================================
    // FOLLOW
    // =====================================

    public void followUser(
            String userId
    ) {

        repository.followUser(

                userId,

                new Callback<FollowResponse>() {

                    @Override
                    public void onResponse(

                            Call<FollowResponse> call,

                            Response<FollowResponse> response
                    ) {

                        followed.setValue(true);

                        loadFollowersCount(userId);
                    }

                    @Override
                    public void onFailure(

                            Call<FollowResponse> call,

                            Throwable t
                    ) {

                        t.printStackTrace();
                    }
                }
        );
    }

    // =====================================
    // UNFOLLOW
    // =====================================

    public void unfollowUser(
            String userId
    ) {

        repository.unfollowUser(

                userId,

                new Callback<FollowResponse>() {

                    @Override
                    public void onResponse(

                            Call<FollowResponse> call,

                            Response<FollowResponse> response
                    ) {

                        followed.setValue(false);

                        loadFollowersCount(userId);
                    }

                    @Override
                    public void onFailure(

                            Call<FollowResponse> call,

                            Throwable t
                    ) {

                        t.printStackTrace();
                    }
                }
        );
    }

    // =====================================
    // FOLLOWERS COUNT
    // =====================================

    public void loadFollowersCount(
            String userId
    ) {

        repository.getFollowersCount(

                userId,

                new Callback<CountResponse>() {

                    @Override
                    public void onResponse(

                            Call<CountResponse> call,

                            Response<CountResponse> response
                    ) {

                        if (

                                response.isSuccessful()

                                        &&

                                        response.body() != null
                        ) {

                            followersCount.setValue(
                                    response.body().result
                            );
                        }
                    }

                    @Override
                    public void onFailure(

                            Call<CountResponse> call,

                            Throwable t
                    ) {

                        t.printStackTrace();
                    }
                }
        );
    }

    // =====================================
    // FOLLOWING COUNT
    // =====================================

    public void loadFollowingCount(
            String userId
    ) {

        repository.getFollowingCount(

                userId,

                new Callback<CountResponse>() {

                    @Override
                    public void onResponse(

                            Call<CountResponse> call,

                            Response<CountResponse> response
                    ) {

                        if (

                                response.isSuccessful()

                                        &&

                                        response.body() != null
                        ) {

                            followingCount.setValue(
                                    response.body().result
                            );
                        }
                    }

                    @Override
                    public void onFailure(

                            Call<CountResponse> call,

                            Throwable t
                    ) {

                        t.printStackTrace();
                    }
                }
        );
    }

    // =====================================
    // POSTS
    // =====================================

    public void loadPosts(
            String userId
    ) {

        repository.getUserPosts(

                userId,

                new Callback<PostResponse>() {

                    @Override
                    public void onResponse(

                            Call<PostResponse> call,

                            Response<PostResponse> response
                    ) {

                        if (

                                response.isSuccessful()

                                        &&

                                        response.body() != null

                                        &&

                                        response.body().result != null
                        ) {

                            posts.setValue(
                                    response.body().result.content
                            );
                        }
                    }

                    @Override
                    public void onFailure(

                            Call<PostResponse> call,

                            Throwable t
                    ) {

                        t.printStackTrace();
                    }
                }
        );
    }
}