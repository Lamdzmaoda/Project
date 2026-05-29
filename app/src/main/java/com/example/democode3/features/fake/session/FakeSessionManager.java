package com.example.democode3.features.fake.session;

import com.example.democode3.features.community.model.Comment;
import com.example.democode3.features.community.model.Post;
import com.example.democode3.features.fake.model.FakePost;
import com.example.democode3.features.fake.model.FakeUser;
import com.example.democode3.features.fake.repository.FakeCommunityRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FakeSessionManager {

    // =================================================
    // SINGLETON
    // =================================================

    private static FakeSessionManager instance;

    public static FakeSessionManager getInstance() {

        if (instance == null) {

            instance =
                    new FakeSessionManager();
        }

        return instance;
    }

    // =================================================
    // DATA
    // =================================================

    private FakeUser currentUser;

    private final Set<String> likedPosts =
            new HashSet<>();

    private final Set<String> savedPosts =
            new HashSet<>();

    private final Set<String> followedUsers =
            new HashSet<>();

    private final FakeCommunityRepository repository =
            new FakeCommunityRepository();

    // =================================================
    // REAL COMMENTS
    // =================================================

    private final List<Comment> fakeComments =
            new ArrayList<>();

    // =================================================
    // CONSTRUCTOR
    // =================================================

    private FakeSessionManager() {

        List<FakeUser> users =
                repository.getFakeUsers();

        currentUser =
                users.get(0);

        seedComments();
    }

    // =================================================
    // SEED COMMENTS
    // =================================================

    private void seedComments() {

        List<FakePost> posts =
                repository.getFakePosts();

        int index = 0;

        for (FakePost post : posts) {

            for (int i = 0; i < 4; i++) {

                Comment comment =
                        new Comment();

                comment.id =
                        "cmt_" + index;

                comment.postId =
                        post.id;

                comment.userId =
                        "u" + (i + 1);

                comment.username =
                        "coder_" + (i + 1);

                comment.userAvatar =
                        "";

                comment.content =
                        getRandomComment(i);

                comment.createdAt =
                        (i + 1) + " phút trước";

                fakeComments.add(comment);

                index++;
            }

            post.commentCount = 4;
        }
    }

    // =================================================
    // CURRENT USER
    // =================================================

    public FakeUser getCurrentUser() {

        return currentUser;
    }

    public void login(
            FakeUser user
    ) {

        currentUser = user;
    }

    // =================================================
    // FOLLOW
    // =================================================

    public void followUser(
            String userId
    ) {

        followedUsers.add(userId);
    }

    public void unfollowUser(
            String userId
    ) {

        followedUsers.remove(userId);
    }

    public boolean isFollowing(
            String userId
    ) {

        return followedUsers.contains(userId);
    }

    // =================================================
    // LIKE
    // =================================================

    public void likePost(
            String postId
    ) {

        likedPosts.add(postId);

        for (FakePost post : repository.getFakePosts()) {

            if (post.id.equals(postId)) {

                post.likeCount++;
            }
        }
    }

    public void unlikePost(
            String postId
    ) {

        likedPosts.remove(postId);

        for (FakePost post : repository.getFakePosts()) {

            if (
                    post.id.equals(postId)
                            &&
                            post.likeCount > 0
            ) {

                post.likeCount--;
            }
        }
    }

    public boolean isPostLiked(
            String postId
    ) {

        return likedPosts.contains(postId);
    }

    // =================================================
    // SAVE
    // =================================================

    public void savePost(
            String postId
    ) {

        savedPosts.add(postId);
    }

    public void unsavePost(
            String postId
    ) {

        savedPosts.remove(postId);
    }

    public boolean isPostSaved(
            String postId
    ) {

        return savedPosts.contains(postId);
    }

    // =================================================
    // POSTS
    // =================================================

    public List<FakePost> getFeedPosts() {

        List<FakePost> posts =
                repository.getFakePosts();

        for (FakePost post : posts) {

            post.likedByMe =
                    likedPosts.contains(
                            post.id
                    );

            post.savedByMe =
                    savedPosts.contains(
                            post.id
                    );

            post.commentCount =
                    getFakeComments(post.id).size();
        }

        return posts;
    }

    // =================================================
    // USERS
    // =================================================

    public List<FakeUser> getAllUsers() {

        return repository.getFakeUsers();
    }

    // =================================================
    // USER POSTS
    // =================================================

    public List<Post> getPostsByUser(
            String userId
    ) {

        List<Post> result =
                new ArrayList<>();

        for (
                FakePost fake
                : repository.getFakePosts()
        ) {

            if (
                    fake.userId.equals(userId)
            ) {

                Post post =
                        convertPost(fake);

                result.add(post);
            }
        }

        return result;
    }

    // =================================================
    // COMMUNITY POSTS
    // =================================================

    public List<Post> getCommunityPosts() {

        List<Post> result =
                new ArrayList<>();

        for (
                FakePost fake
                : repository.getFakePosts()
        ) {

            result.add(
                    convertPost(fake)
            );
        }

        return result;
    }

    // =================================================
    // CONVERT
    // =================================================

    private Post convertPost(
            FakePost fake
    ) {

        Post post =
                new Post();

        post.id =
                fake.id;

        post.userId =
                fake.userId;

        post.username =
                fake.username;

        post.userAvatar =
                fake.userAvatar;

        post.title =
                fake.title;

        post.content =
                fake.content;

        post.imageUrl =
                fake.imageUrl;

        post.codeSnippet =
                fake.codeSnippet;

        post.likeCount =
                fake.likeCount;

        post.commentCount =
                getFakeComments(fake.id).size();

        post.likedByMe =
                likedPosts.contains(fake.id);

        post.savedByMe =
                savedPosts.contains(fake.id);

        post.createdAt =
                fake.createdAt;

        return post;
    }

    // =================================================
    // COMMENTS
    // =================================================

    public List<Comment> getFakeComments(
            String postId
    ) {

        List<Comment> result =
                new ArrayList<>();

        for (Comment c : fakeComments) {

            if (
                    c.postId != null
                            &&
                            c.postId.equals(postId)
            ) {

                result.add(c);
            }
        }

        return result;
    }

    // =================================================
    // ADD COMMENT
    // =================================================

    public void addComment(
            String postId,
            String content
    ) {

        Comment comment =
                new Comment();

        comment.id =
                String.valueOf(
                        System.currentTimeMillis()
                );

        comment.postId =
                postId;

        comment.userId =
                currentUser.id;

        comment.username =
                currentUser.username;

        comment.userAvatar =
                currentUser.avatar;

        comment.content =
                content;

        comment.createdAt =
                "Vừa xong";

        fakeComments.add(0, comment);
    }

    // =================================================
    // RANDOM COMMENT
    // =================================================

    private String getRandomComment(
            int index
    ) {

        String[] comments = {

                "Hay quá 😭🔥",

                "Code sạch thật",

                "Đỉnh vậy 😭",

                "Tôi học được rồi",

                "Giải thích dễ hiểu thật",

                "Thanks bro 😭🔥"
        };

        return comments[
                index % comments.length
                ];
    }
    public int getPostLikeCount(
            String postId
    ) {

        for (FakePost post
                : repository.getFakePosts()) {

            if (
                    post.id.equals(postId)
            ) {

                return post.likeCount;
            }
        }

        return 0;
    }
}