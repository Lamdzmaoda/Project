/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.identity_servive.dto.response.community.PostResponse;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.community.Post;
import com.example.identity_servive.entity.community.SavedPost;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.community.PostRepository;
import com.example.identity_servive.repository.community.SavedPostRepository;
import com.example.identity_servive.service.community.PostService;
import com.example.identity_servive.service.community.SavedPostService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class SavedPostServiceTest {

  @Autowired private SavedPostService savedPostService;
  @MockitoBean private SavedPostRepository savedPostRepository;
  @MockitoBean private PostRepository postRepository;
  @MockitoBean private UserRepository userRepository;
  @MockitoBean private PostService postService;

  private User user;
  private Post post;
  private SavedPost savedPost;
  private PostResponse postResponse;

  @BeforeEach
  void initData() {
    user = User.builder().id("user-1").username("testuser").email("test@test.com").build();
    post = Post.builder().id("post-1").content("Test").build();
    savedPost =
        SavedPost.builder()
            .id("saved-1")
            .user(user)
            .post(post)
            .savedAt(LocalDateTime.now())
            .build();
    postResponse = PostResponse.builder().id("post-1").content("Test").build();
  }

  @Test
  void savePost_success() {
    when(savedPostRepository.existsByUserIdAndPostId(any(), any())).thenReturn(false);
    when(userRepository.findById(any())).thenReturn(Optional.of(user));
    when(postRepository.findById(any())).thenReturn(Optional.of(post));
    when(savedPostRepository.save(any())).thenReturn(savedPost);
    when(postService.getPostById(any(), any())).thenReturn(postResponse);

    var result = savedPostService.savePost("post-1", "user-1");
    assertThat(result).isNotNull();
    assertThat(result.getPostId()).isEqualTo("post-1");
  }

  @Test
  void savePost_alreadyExists_fail() {
    when(savedPostRepository.existsByUserIdAndPostId(any(), any())).thenReturn(true);

    assertThrows(AppException.class, () -> savedPostService.savePost("post-1", "user-1"));
  }

  @Test
  void unsavePost_success() {
    when(savedPostRepository.findByUserIdAndPostId(any(), any()))
        .thenReturn(Optional.of(savedPost));

    savedPostService.unsavePost("post-1", "user-1");
  }

  @Test
  void unsavePost_notFound_fail() {
    when(savedPostRepository.findByUserIdAndPostId(any(), any())).thenReturn(Optional.empty());

    assertThrows(AppException.class, () -> savedPostService.unsavePost("post-1", "user-1"));
  }

  @Test
  void getSavedPosts_success() {
    when(savedPostRepository.findByUserIdOrderBySavedAtDesc(any())).thenReturn(List.of(savedPost));
    when(postService.getPostById(any(), any())).thenReturn(postResponse);

    var result = savedPostService.getSavedPosts("user-1");
    assertThat(result).isNotEmpty();
  }

  @Test
  void isSaved_true() {
    when(savedPostRepository.existsByUserIdAndPostId(any(), any())).thenReturn(true);

    var result = savedPostService.isSaved("post-1", "user-1");
    assertThat(result).isTrue();
  }

  @Test
  void isSaved_false() {
    when(savedPostRepository.existsByUserIdAndPostId(any(), any())).thenReturn(false);

    var result = savedPostService.isSaved("post-1", "user-1");
    assertThat(result).isFalse();
  }
}
