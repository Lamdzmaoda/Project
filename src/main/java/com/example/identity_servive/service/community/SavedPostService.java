/* (C)2026 */
package com.example.identity_servive.service.community;

import com.example.identity_servive.dto.response.community.SavedPostResponse;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.community.Post;
import com.example.identity_servive.entity.community.SavedPost;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.community.PostRepository;
import com.example.identity_servive.repository.community.SavedPostRepository;
import java.util.List;
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
public class SavedPostService {

  SavedPostRepository savedPostRepository;
  PostRepository postRepository;
  UserRepository userRepository;
  PostService postService;

  @Transactional
  public SavedPostResponse savePost(String postId, String userId) {
    if (savedPostRepository.existsByUserIdAndPostId(userId, postId)) {
      throw new AppException(ErrorCode.ALREADY_EXISTED);
    }
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

    SavedPost savedPost = SavedPost.builder().user(user).post(post).build();
    savedPost = savedPostRepository.save(savedPost);

    return SavedPostResponse.builder()
        .id(savedPost.getId())
        .postId(postId)
        .post(postService.getPostById(postId, userId))
        .createdAt(savedPost.getSavedAt())
        .build();
  }

  @Transactional
  public void unsavePost(String postId, String userId) {
    SavedPost savedPost =
        savedPostRepository
            .findByUserIdAndPostId(userId, postId)
            .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
    savedPostRepository.delete(savedPost);
  }

  public List<SavedPostResponse> getSavedPosts(String userId) {
    return savedPostRepository.findByUserIdOrderBySavedAtDesc(userId).stream()
        .map(
            sp ->
                SavedPostResponse.builder()
                    .id(sp.getId())
                    .postId(sp.getPost().getId())
                    .post(postService.getPostById(sp.getPost().getId(), userId))
                    .createdAt(sp.getSavedAt())
                    .build())
        .toList();
  }

  public boolean isSaved(String postId, String userId) {
    return savedPostRepository.existsByUserIdAndPostId(userId, postId);
  }
}
