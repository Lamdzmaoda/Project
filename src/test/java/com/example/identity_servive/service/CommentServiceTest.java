/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.identity_servive.dto.request.community.CommentRequest;
import com.example.identity_servive.dto.response.community.CommentResponse;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.community.Comment;
import com.example.identity_servive.entity.community.Post;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.mapper.CommentMapper;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.community.CommentRepository;
import com.example.identity_servive.repository.community.PostRepository;
import com.example.identity_servive.service.community.CommentService;
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
public class CommentServiceTest {

  @Autowired private CommentService commentService;
  @MockitoBean private CommentRepository commentRepository;
  @MockitoBean private PostRepository postRepository;
  @MockitoBean private CommentMapper commentMapper;
  @MockitoBean private UserRepository userRepository;

  private User user;
  private Post post;
  private Comment comment;
  private CommentResponse commentResponse;
  private CommentRequest request;

  @BeforeEach
  void initData() {
    user = User.builder().id("user-1").username("testuser").email("test@test.com").build();
    post = Post.builder().id("post-1").content("Test post").commentCount(0).build();
    comment = Comment.builder().id("comment-1").content("Nice!").user(user).post(post).build();
    commentResponse = CommentResponse.builder().id("comment-1").content("Nice!").build();
    request = CommentRequest.builder().postId("post-1").content("Nice!").build();
  }

  @Test
  void createComment_success() {
    when(userRepository.findById(any())).thenReturn(Optional.of(user));
    when(postRepository.findById(any())).thenReturn(Optional.of(post));
    when(commentMapper.toComment(any())).thenReturn(comment);
    when(commentRepository.save(any())).thenReturn(comment);
    when(commentMapper.toCommentResponse(any())).thenReturn(commentResponse);

    var result = commentService.createComment(request, "user-1");
    assertThat(result).isNotNull();
  }

  @Test
  void createComment_postNotFound_fail() {
    when(userRepository.findById(any())).thenReturn(Optional.of(user));
    when(postRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(AppException.class, () -> commentService.createComment(request, "user-1"));
  }

  @Test
  void createComment_userNotFound_fail() {
    when(userRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(AppException.class, () -> commentService.createComment(request, "user-1"));
  }

  @Test
  void getCommentsByPost_success() {
    when(commentRepository.findByPostIdOrderByCreatedAtDesc(any())).thenReturn(List.of(comment));
    when(commentMapper.toCommentResponse(any())).thenReturn(commentResponse);

    var result = commentService.getCommentsByPost("post-1");
    assertThat(result).isNotEmpty();
  }

  @Test
  void deleteComment_success() {
    when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

    commentService.deleteComment("comment-1", "user-1");
  }

  @Test
  void deleteComment_notOwner_fail() {
    User otherUser = User.builder().id("other").username("other").email("other@test.com").build();
    comment.setUser(otherUser);
    when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

    assertThrows(AppException.class, () -> commentService.deleteComment("comment-1", "user-1"));
  }

  @Test
  void deleteComment_notFound_fail() {
    when(commentRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(AppException.class, () -> commentService.deleteComment("invalid", "user-1"));
  }
}
