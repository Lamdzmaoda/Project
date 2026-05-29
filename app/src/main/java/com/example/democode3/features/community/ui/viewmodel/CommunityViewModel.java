package com.example.democode3.features.community.ui.viewmodel;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.democode3.core.enums.FeedType;
import com.example.democode3.features.community.model.Post;
import com.example.democode3.features.fake.session.FakeSessionManager;

import java.util.ArrayList;
import java.util.List;

public class CommunityViewModel
        extends ViewModel {

    // =====================================
    // POSTS
    // =====================================

    private final MutableLiveData<List<Post>>
            posts =
            new MutableLiveData<>();

    // =====================================
    // LOADING
    // =====================================

    private final MutableLiveData<Boolean>
            loading =
            new MutableLiveData<>(false);

    // =====================================
    // CURRENT FEED
    // =====================================

    private FeedType currentFeed =
            FeedType.ALL;

    // =====================================
    // SESSION
    // =====================================

    private final FakeSessionManager session =
            FakeSessionManager.getInstance();

    // =====================================
    // INIT
    // =====================================

    public void init(
            Context context
    ) {

    }

    // =====================================
    // POSTS
    // =====================================

    public LiveData<List<Post>>
    getPosts() {

        return posts;
    }

    // =====================================
    // LOADING
    // =====================================

    public LiveData<Boolean>
    isLoading() {

        return loading;
    }

    // =====================================
    // LOAD FEED
    // =====================================

    public void loadFeed(
            FeedType type
    ) {

        currentFeed = type;

        loading.setValue(true);

        new Handler(

                Looper.getMainLooper()

        ).postDelayed(() -> {

            List<Post> fakePosts =
                    session.getCommunityPosts();

            // =============================
            // FOLLOWING FEED
            // =============================

            if (type == FeedType.FOLLOWING) {

                List<Post> followingPosts =
                        new ArrayList<>();

                for (Post post : fakePosts) {

                    if (

                            session.isFollowing(
                                    post.userId
                            )
                    ) {

                        followingPosts.add(post);
                    }
                }

                posts.setValue(
                        followingPosts
                );
            }

            else {

                posts.setValue(
                        fakePosts
                );
            }

            loading.setValue(false);

        }, 1200);
    }

    // =====================================
    // LIKE
    // =====================================

    public void toggleLike(
            Post post
    ) {

        if (post == null)
            return;

        if (post.likedByMe) {

            post.likedByMe = false;

            if (post.likeCount > 0) {

                post.likeCount--;
            }

            session.unlikePost(
                    post.id
            );
        }

        else {

            post.likedByMe = true;

            post.likeCount++;

            session.likePost(
                    post.id
            );
        }

        refreshPosts();
    }

    // =====================================
    // SAVE
    // =====================================

    public void toggleSave(
            Post post
    ) {

        if (post == null)
            return;

        if (post.savedByMe) {

            post.savedByMe = false;

            session.unsavePost(
                    post.id
            );
        }

        else {

            post.savedByMe = true;

            session.savePost(
                    post.id
            );
        }

        refreshPosts();
    }

    // =====================================
    // DELETE
    // =====================================

    public void deletePost(
            long postId
    ) {

        List<Post> current =
                posts.getValue();

        if (current == null)
            return;

        List<Post> updated =
                new ArrayList<>();

        for (Post post : current) {

            if (

                    !String.valueOf(postId)
                            .equals(post.id)
            ) {

                updated.add(post);
            }
        }

        posts.setValue(updated);
    }

    // =====================================
    // CREATE
    // =====================================

    public void createPost(
            FeedType type,
            Post post
    ) {

        if (post == null)
            return;

        List<Post> current =
                posts.getValue();

        if (current == null) {

            current =
                    new ArrayList<>();
        }

        current.add(0, post);

        posts.setValue(current);
    }

    // =====================================
    // REFRESH
    // =====================================

    private void refreshPosts() {

        List<Post> current =
                posts.getValue();

        if (current == null)
            return;

        posts.setValue(
                new ArrayList<>(current)
        );
    }
}