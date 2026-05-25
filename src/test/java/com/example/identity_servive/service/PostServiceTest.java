/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.identity_servive.dto.request.community.PostRequest;
import com.example.identity_servive.dto.response.community.PostResponse;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.community.Post;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.mapper.PostMapper;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.community.FollowRepository;
import com.example.identity_servive.repository.community.PostLikeRepository;
import com.example.identity_servive.repository.community.PostRepository;
import com.example.identity_servive.repository.community.SavedPostRepository;
import com.example.identity_servive.service.community.PostService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class PostServiceTest {

  @Autowired private PostService postService;
  @MockitoBean private PostRepository postRepository;
  @MockitoBean private PostMapper postMapper;
  @MockitoBean private PostLikeRepository postLikeRepository;
  @MockitoBean private SavedPostRepository savedPostRepository;
  @MockitoBean private FollowRepository followRepository;
  @MockitoBean private UserRepository userRepository;

  private PostRequest request;
  private Post post;
  private PostResponse response;
  private User user;

  @BeforeEach
  void initData() {
    user = User.builder().id("user-1").username("lamdzbodoi").email("lamdzbodoi@gmail.com").build();
    request = PostRequest.builder().content("Test post").title("Test").build();
    post = Post.builder().id("post-1").content("Test post").user(user).build();
    response =
        PostResponse.builder().id("post-1").content("Test post").username("lamdzbodoi").build();
  }

  @Test
  void createPost_success() {
    when(userRepository.findById(any())).thenReturn(Optional.of(user));
    when(postMapper.toPost(any())).thenReturn(post);
    when(postRepository.save(any())).thenReturn(post);
    when(postMapper.toPostResponse(any())).thenReturn(response);

    var result = postService.createPost(request, "user-1");
    assertThat(result).isNotNull();
  }

  @Test
  void getPostById_notFound_fail() {
    when(postRepository.findById(any())).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> postService.getPostById("invalid-id", "user-1"));
  }

  @Test
  void deletePost_notOwner_fail() {
    User otherUser =
        User.builder().id("other-user").username("other").email("lamdzbodoi@gmail.com").build();
    post.setUser(otherUser);
    when(postRepository.findById(any())).thenReturn(Optional.of(post));
    assertThrows(AppException.class, () -> postService.deletePost("post-1", "user-1"));
  }
}
