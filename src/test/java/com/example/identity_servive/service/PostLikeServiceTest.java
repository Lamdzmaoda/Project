/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.community.Post;
import com.example.identity_servive.entity.community.PostLike;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.community.PostLikeRepository;
import com.example.identity_servive.repository.community.PostRepository;
import com.example.identity_servive.service.community.PostLikeService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class PostLikeServiceTest {

  @Autowired private PostLikeService postLikeService;
  @MockitoBean private PostLikeRepository postLikeRepository;
  @MockitoBean private PostRepository postRepository;
  @MockitoBean private UserRepository userRepository;

  private User user;
  private Post post;

  @BeforeEach
  void initData() {
    user = User.builder().id("user-1").username("lamdzbodoi").email("lamdzbodoi@gmail.com").build();
    post = Post.builder().id("post-1").content("Test").likeCount(0).build();
  }

  @Test
  void likePost_success() {
    when(userRepository.findById(any())).thenReturn(Optional.of(user));
    when(postRepository.findById(any())).thenReturn(Optional.of(post));
    when(postLikeRepository.findByPostIdAndUserId(any(), any())).thenReturn(Optional.empty());
    when(postLikeRepository.save(any()))
        .thenReturn(PostLike.builder().id("like-1").user(user).post(post).build());
    when(postRepository.save(any())).thenReturn(post);

    var result = postLikeService.likePost("post-1", "user-1");
    assertThat(result).isNotNull();
  }

  @Test
  void likePost_alreadyLiked_fail() {
    when(userRepository.findById(any())).thenReturn(Optional.of(user));
    when(postRepository.findById(any())).thenReturn(Optional.of(post));
    when(postLikeRepository.findByPostIdAndUserId(any(), any()))
        .thenReturn(Optional.of(PostLike.builder().build()));
    assertThrows(AppException.class, () -> postLikeService.likePost("post-1", "user-1"));
  }

  @Test
  void unlikePost_notLiked_fail() {
    when(postLikeRepository.findByPostIdAndUserId(any(), any())).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> postLikeService.unlikePost("post-1", "user-1"));
  }
}
