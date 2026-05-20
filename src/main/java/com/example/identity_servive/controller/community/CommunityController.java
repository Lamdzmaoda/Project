package com.example.identity_servive.controller.community;

import com.example.identity_servive.dto.request.community.CommentRequest;
import com.example.identity_servive.dto.request.community.PostRequest;
import com.example.identity_servive.dto.response.ApiResponse;
import com.example.identity_servive.dto.response.community.*;
import com.example.identity_servive.service.community.*;
import com.example.identity_servive.service.learning.LearningProgressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommunityController {

    PostService postService;
    CommentService commentService;
    PostLikeService postLikeService;
    FollowService followService;
    SavedPostService savedPostService;
    LearningProgressService learningProgressService;

    private String getCurrentUserId() {
        return learningProgressService.getCurrentUser().getId();
    }

    // ──────────────────────────────────────────────
    // POSTS
    // ──────────────────────────────────────────────

    @PostMapping("/posts")
    ApiResponse<PostResponse> createPost(@RequestBody PostRequest request) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request, getCurrentUserId()))
                .build();
    }

    @GetMapping("/posts")
    ApiResponse<Page<PostResponse>> getFeed(Pageable pageable) {
        return ApiResponse.<Page<PostResponse>>builder()
                .result(postService.getFeed(pageable, getCurrentUserId()))
                .build();
    }

    @GetMapping("/posts/{postId}")
    ApiResponse<PostResponse> getPost(@PathVariable String postId) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.getPostById(postId, getCurrentUserId()))
                .build();
    }

    @GetMapping("/users/{userId}/posts")
    ApiResponse<Page<PostResponse>> getUserPosts(@PathVariable String userId, Pageable pageable) {
        return ApiResponse.<Page<PostResponse>>builder()
                .result(postService.getUserPosts(userId, pageable, getCurrentUserId()))
                .build();
    }

    @DeleteMapping("/posts/{postId}")
    ApiResponse<Void> deletePost(@PathVariable String postId) {
        postService.deletePost(postId, getCurrentUserId());
        return ApiResponse.<Void>builder().build();
    }

    // ──────────────────────────────────────────────
    // COMMENTS
    // ──────────────────────────────────────────────

    @PostMapping("/comments")
    ApiResponse<CommentResponse> createComment(@RequestBody CommentRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.createComment(request, getCurrentUserId()))
                .build();
    }

    @GetMapping("/posts/{postId}/comments")
    ApiResponse<List<CommentResponse>> getComments(@PathVariable String postId) {
        return ApiResponse.<List<CommentResponse>>builder()
                .result(commentService.getCommentsByPost(postId))
                .build();
    }

    @DeleteMapping("/comments/{commentId}")
    ApiResponse<Void> deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(commentId, getCurrentUserId());
        return ApiResponse.<Void>builder().build();
    }

    // ──────────────────────────────────────────────
    // LIKES
    // ──────────────────────────────────────────────

    @PostMapping("/posts/{postId}/like")
    ApiResponse<PostLikeResponse> likePost(@PathVariable String postId) {
        return ApiResponse.<PostLikeResponse>builder()
                .result(postLikeService.likePost(postId, getCurrentUserId()))
                .build();
    }

    @DeleteMapping("/posts/{postId}/like")
    ApiResponse<Void> unlikePost(@PathVariable String postId) {
        postLikeService.unlikePost(postId, getCurrentUserId());
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/posts/{postId}/liked")
    ApiResponse<Boolean> isLiked(@PathVariable String postId) {
        return ApiResponse.<Boolean>builder()
                .result(postLikeService.isLiked(postId, getCurrentUserId()))
                .build();
    }

    // ──────────────────────────────────────────────
    // FOLLOWS
    // ──────────────────────────────────────────────

    @PostMapping("/follow/{followeeId}")
    ApiResponse<FollowResponse> follow(@PathVariable String followeeId) {
        return ApiResponse.<FollowResponse>builder()
                .result(followService.follow(followeeId, getCurrentUserId()))
                .build();
    }

    @DeleteMapping("/follow/{followeeId}")
    ApiResponse<Void> unfollow(@PathVariable String followeeId) {
        followService.unfollow(followeeId, getCurrentUserId());
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/users/{userId}/following")
    ApiResponse<List<FollowResponse>> getFollowing(@PathVariable String userId) {
        return ApiResponse.<List<FollowResponse>>builder()
                .result(followService.getFollowing(userId))
                .build();
    }

    @GetMapping("/users/{userId}/followers")
    ApiResponse<List<FollowResponse>> getFollowers(@PathVariable String userId) {
        return ApiResponse.<List<FollowResponse>>builder()
                .result(followService.getFollowers(userId))
                .build();
    }

    @GetMapping("/users/{userId}/following/count")
    ApiResponse<Integer> getFollowingCount(@PathVariable String userId) {
        return ApiResponse.<Integer>builder()
                .result(followService.getFollowingCount(userId))
                .build();
    }

    @GetMapping("/users/{userId}/followers/count")
    ApiResponse<Integer> getFollowerCount(@PathVariable String userId) {
        return ApiResponse.<Integer>builder()
                .result(followService.getFollowerCount(userId))
                .build();
    }

    @GetMapping("/follow/{followeeId}/check")
    ApiResponse<Boolean> isFollowing(@PathVariable String followeeId) {
        return ApiResponse.<Boolean>builder()
                .result(followService.isFollowing(getCurrentUserId(), followeeId))
                .build();
    }

    // ──────────────────────────────────────────────
    // SAVED POSTS
    // ──────────────────────────────────────────────

    @PostMapping("/posts/{postId}/save")
    ApiResponse<SavedPostResponse> savePost(@PathVariable String postId) {
        return ApiResponse.<SavedPostResponse>builder()
                .result(savedPostService.savePost(postId, getCurrentUserId()))
                .build();
    }

    @DeleteMapping("/posts/{postId}/save")
    ApiResponse<Void> unsavePost(@PathVariable String postId) {
        savedPostService.unsavePost(postId, getCurrentUserId());
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/saved-posts")
    ApiResponse<List<SavedPostResponse>> getSavedPosts() {
        return ApiResponse.<List<SavedPostResponse>>builder()
                .result(savedPostService.getSavedPosts(getCurrentUserId()))
                .build();
    }

    @GetMapping("/posts/{postId}/saved")
    ApiResponse<Boolean> isSaved(@PathVariable String postId) {
        return ApiResponse.<Boolean>builder()
                .result(savedPostService.isSaved(postId, getCurrentUserId()))
                .build();
    }
}
