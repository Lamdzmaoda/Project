/* (C)2026 */
package com.example.identity_servive.service.community;

import com.example.identity_servive.dto.request.community.PostRequest;
import com.example.identity_servive.dto.response.community.PostResponse;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.community.Post;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.mapper.PostMapper;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.community.FollowRepository;
import com.example.identity_servive.repository.community.PostLikeRepository;
import com.example.identity_servive.repository.community.PostRepository;
import com.example.identity_servive.repository.community.SavedPostRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {

  PostRepository postRepository;
  PostMapper postMapper;
  PostLikeRepository postLikeRepository;
  SavedPostRepository savedPostRepository;
  FollowRepository followRepository;
  UserRepository userRepository;

  public PostResponse createPost(PostRequest request, String userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    Post post = postMapper.toPost(request);
    post.setUser(user);
    post = postRepository.save(post);
    return buildPostResponse(post, userId);
  }

  public Page<PostResponse> getFeed(Pageable pageable, String currentUserId) {
    return postRepository
        .findAllByOrderByCreatedAtDesc(pageable)
        .map(post -> buildPostResponse(post, currentUserId));
  }

  public Page<PostResponse> getFollowingFeed(Pageable pageable, String currentUserId) {
    var follows = followRepository.findByFollowerId(currentUserId);
    if (follows.isEmpty()) {
      return Page.empty(pageable);
    }
    List<String> followerIds =
        follows.stream().map(follow -> follow.getFollowee().getId()).toList();
    return postRepository
        .findByUserIdInOrderByCreatedAtDesc(followerIds, pageable)
        .map(post -> buildPostResponse(post, currentUserId));
  }

  public Page<PostResponse> getUserPosts(String userId, Pageable pageable, String currentUserId) {
    return postRepository
        .findByUserIdOrderByCreatedAtDesc(userId, pageable)
        .map(post -> buildPostResponse(post, currentUserId));
  }

  public PostResponse getPostById(String postId, String currentUserId) {
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
    return buildPostResponse(post, currentUserId);
  }

  @Transactional
  public void deletePost(String postId, String userId) {
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
    if (!post.getUser().getId().equals(userId)) {
      throw new AppException(ErrorCode.UNAUTHORIZED_EXISTED);
    }
    postRepository.delete(post);
  }

  private PostResponse buildPostResponse(Post post, String currentUserId) {
    PostResponse response = postMapper.toPostResponse(post);
    response.setLikeCount(post.getLikeCount());
    response.setCommentCount(post.getCommentCount());
    response.setUserId(post.getUser().getId());
    response.setUsername(post.getUser().getUsername());
    response.setUserAvatar(post.getUser().getAvatarUrl());
    if (currentUserId != null) {
      response.setLikedByMe(
          postLikeRepository.existsByPostIdAndUserId(post.getId(), currentUserId));
      response.setSavedByMe(
          savedPostRepository.existsByUserIdAndPostId(currentUserId, post.getId()));
    }
    return response;
  }
}
