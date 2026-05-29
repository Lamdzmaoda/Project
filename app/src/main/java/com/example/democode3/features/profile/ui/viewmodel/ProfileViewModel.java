package com.example.democode3.features.profile.ui.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.democode3.features.community.model.Post;
import com.example.democode3.features.fake.model.FakeUser;
import com.example.democode3.features.fake.session.FakeSessionManager;
import com.example.democode3.features.profile.model.UserProfileResponse;

import java.util.List;

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
    // LOADING
    // =====================================

    private final MutableLiveData<Boolean>
            loading =
            new MutableLiveData<>(false);

    // =====================================
    // SESSION
    // =====================================

    private final FakeSessionManager session =
            FakeSessionManager.getInstance();

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public ProfileViewModel(
            @NonNull Application application
    ) {

        super(application);
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
    // LOADING
    // =====================================

    public LiveData<Boolean>
    isLoading() {

        return loading;
    }

    // =====================================
    // LOAD MY PROFILE
    // =====================================

    public void loadMyProfile() {

        loading.setValue(true);

        new Handler(

                Looper.getMainLooper()

        ).postDelayed(() -> {

            FakeUser fakeUser =
                    session.getCurrentUser();

            UserProfileResponse user =
                    mapFakeUser(fakeUser);

            profile.setValue(user);

            followersCount.setValue(
                    fakeUser.followers
            );

            followingCount.setValue(
                    fakeUser.following
            );

            posts.setValue(

                    session.getPostsByUser(
                            fakeUser.id
                    )
            );

            loading.setValue(false);

        }, 1000);
    }

    // =====================================
    // LOAD USER PROFILE
    // =====================================

    public void loadUserProfile(
            String userId
    ) {

        loading.setValue(true);

        new Handler(

                Looper.getMainLooper()

        ).postDelayed(() -> {

            FakeUser targetUser = null;

            for (

                    FakeUser user
                    : session.getAllUsers()
            ) {

                if (

                        user.id.equals(
                                userId
                        )
                ) {

                    targetUser = user;

                    break;
                }
            }

            if (targetUser == null) {

                loading.setValue(false);

                return;
            }

            profile.setValue(
                    mapFakeUser(targetUser)
            );

            followed.setValue(

                    session.isFollowing(
                            userId
                    )
            );

            followersCount.setValue(
                    targetUser.followers
            );

            followingCount.setValue(
                    targetUser.following
            );

            posts.setValue(

                    session.getPostsByUser(
                            userId
                    )
            );

            loading.setValue(false);

        }, 1000);
    }

    // =====================================
    // FOLLOW
    // =====================================

    public void followUser(
            String userId
    ) {

        session.followUser(userId);

        followed.setValue(true);

        Integer current =
                followersCount.getValue();

        if (current == null)
            current = 0;

        followersCount.setValue(
                current + 1
        );
    }

    // =====================================
    // UNFOLLOW
    // =====================================

    public void unfollowUser(
            String userId
    ) {

        session.unfollowUser(userId);

        followed.setValue(false);

        Integer current =
                followersCount.getValue();

        if (current == null)
            current = 0;

        if (current > 0) {

            current--;
        }

        followersCount.setValue(
                current
        );
    }

    // =====================================
    // MAP USER
    // =====================================

    private UserProfileResponse mapFakeUser(
            FakeUser fake
    ) {

        UserProfileResponse user =
                new UserProfileResponse();

        user.id =
                fake.id;

        user.username =
                fake.username;

        user.displayName =
                fake.fullName;

        user.avatarUrl =
                fake.avatar;

        user.bio =
                fake.bio;

        user.level =
                fake.level;

        user.xp =
                fake.xp;

        user.streak =
                fake.streak;

        user.verified =
                fake.verified;

        return user;
    }
}