/* (C)2026 */
package com.example.identity_servive.service.community;

import com.example.identity_servive.dto.response.community.PostLikeResponse;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.community.Post;
import com.example.identity_servive.entity.community.PostLike;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.community.PostLikeRepository;
import com.example.identity_servive.repository.community.PostRepository;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostLikeService {

  PostLikeRepository postLikeRepository;
  PostRepository postRepository;
  UserRepository userRepository;

  @Transactional
  public PostLikeResponse likePost(String postId, String userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

    Optional<PostLike> existing = postLikeRepository.findByPostIdAndUserId(postId, userId);
    if (existing.isPresent()) {
      throw new AppException(ErrorCode.ALREADY_EXISTED);
    }

    PostLike postLike = PostLike.builder().user(user).post(post).build();
    postLike = postLikeRepository.save(postLike);

    post.setLikeCount(post.getLikeCount() + 1);
    postRepository.save(post);

    return PostLikeResponse.builder()
        .id(postLike.getId())
        .postId(postId)
        .userId(userId)
        .username(user.getUsername())
        .createdAt(postLike.getCreatedAt())
        .build();
  }

  @Transactional
  public void unlikePost(String postId, String userId) {
    PostLike postLike =
        postLikeRepository
            .findByPostIdAndUserId(postId, userId)
            .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
    postLikeRepository.delete(postLike);

    Post post = postLike.getPost();
    post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
    postRepository.save(post);
  }

  public boolean isLiked(String postId, String userId) {
    return postLikeRepository.existsByPostIdAndUserId(postId, userId);
  }
}
