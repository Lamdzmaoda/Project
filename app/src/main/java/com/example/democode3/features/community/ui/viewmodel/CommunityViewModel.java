package com.example.democode3.features.community.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.democode3.core.enums.FeedType;
import com.example.democode3.features.community.api.response.PostResponse;
import com.example.democode3.features.community.model.Post;
import com.example.democode3.features.community.repository.CommunityRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityViewModel
        extends ViewModel {

    // =====================================
    // POSTS
    // =====================================

    private final MutableLiveData<List<Post>>
            posts =
            new MutableLiveData<>();

    // =====================================
    // REPOSITORY
    // =====================================

    private CommunityRepository repository;

    // =====================================
    // CURRENT FEED
    // =====================================

    private FeedType currentFeed =
            FeedType.ALL;

    // =====================================
    // INIT
    // =====================================

    public void init(
            Context context
    ) {

        if (repository == null) {

            repository =
                    new CommunityRepository(
                            context
                    );
        }
    }

    // =====================================
    // OBSERVE
    // =====================================

    public LiveData<List<Post>> getPosts() {

        return posts;
    }

    // =====================================
    // LOAD FEED
    // =====================================

    public void loadFeed(
            FeedType type
    ) {

        currentFeed = type;

        if (repository == null)
            return;

        repository.getPosts(

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

                            if (response.body().result != null) {

                                posts.setValue(
                                        response.body().result.content
                                );
                            }
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

    // =====================================
    // LIKE
    // =====================================

    public void toggleLike(
            Post post
    ) {

        if (repository == null)
            return;

        if (post.likedByMe) {

            repository.unlikePost(

                    String.valueOf(post.id),

                    new Callback<Void>() {

                        @Override
                        public void onResponse(

                                Call<Void> call,

                                Response<Void> response
                        ) {

                            post.likedByMe = false;

                            if (post.likeCount > 0) {

                                post.likeCount--;
                            }

                            posts.setValue(
                                    posts.getValue()
                            );
                        }

                        @Override
                        public void onFailure(

                                Call<Void> call,

                                Throwable t
                        ) {

                            t.printStackTrace();
                        }
                    }
            );
        }

        else {

            repository.likePost(

                    String.valueOf(post.id),

                    new Callback<Void>() {

                        @Override
                        public void onResponse(

                                Call<Void> call,

                                Response<Void> response
                        ) {

                            post.likedByMe = true;

                            post.likeCount++;

                            posts.setValue(
                                    posts.getValue()
                            );
                        }

                        @Override
                        public void onFailure(

                                Call<Void> call,

                                Throwable t
                        ) {

                            t.printStackTrace();
                        }
                    }
            );
        }
    }

    // =====================================
    // SAVE
    // =====================================

    public void toggleSave(
            Post post
    ) {

        if (repository == null)
            return;

        if (post.savedByMe) {

            repository.unsavePost(

                    String.valueOf(post.id),

                    new Callback<Void>() {

                        @Override
                        public void onResponse(

                                Call<Void> call,

                                Response<Void> response
                        ) {

                            post.savedByMe = false;

                            posts.setValue(
                                    posts.getValue()
                            );
                        }

                        @Override
                        public void onFailure(

                                Call<Void> call,

                                Throwable t
                        ) {

                            t.printStackTrace();
                        }
                    }
            );
        }

        else {

            repository.savePost(

                    String.valueOf(post.id),

                    new Callback<Void>() {

                        @Override
                        public void onResponse(

                                Call<Void> call,

                                Response<Void> response
                        ) {

                            post.savedByMe = true;

                            posts.setValue(
                                    posts.getValue()
                            );
                        }

                        @Override
                        public void onFailure(

                                Call<Void> call,

                                Throwable t
                        ) {

                            t.printStackTrace();
                        }
                    }
            );
        }
    }

    // =====================================
    // DELETE POST
    // =====================================

    public void deletePost(
            long postId
    ) {

        if (repository == null)
            return;

        repository.deletePost(

                String.valueOf(postId),

                new Callback<Void>() {

                    @Override
                    public void onResponse(

                            Call<Void> call,

                            Response<Void> response
                    ) {

                        loadFeed(currentFeed);
                    }

                    @Override
                    public void onFailure(

                            Call<Void> call,

                            Throwable t
                    ) {

                        t.printStackTrace();
                    }
                }
        );
    }

    // =====================================
    // CREATE POST
    // =====================================

    public void createPost(
            FeedType type,
            Post post
    ) {

        loadFeed(type);
    }
}