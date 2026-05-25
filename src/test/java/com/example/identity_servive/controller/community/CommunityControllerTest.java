/* (C)2026 */
package com.example.identity_servive.controller.community;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.identity_servive.dto.request.community.CommentRequest;
import com.example.identity_servive.dto.request.community.PostRequest;
import com.example.identity_servive.dto.response.community.*;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.service.cloudinary.CloudinaryService;
import com.example.identity_servive.service.community.*;
import com.example.identity_servive.service.learning.LearningProgressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Slf4j
@SpringBootTest
@TestPropertySource("/test.properties")
@AutoConfigureMockMvc
public class CommunityControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private PostService postService;
  @MockitoBean private CommentService commentService;
  @MockitoBean private PostLikeService postLikeService;
  @MockitoBean private FollowService followService;
  @MockitoBean private SavedPostService savedPostService;
  @MockitoBean private LearningProgressService learningProgressService;
  @MockitoBean private CloudinaryService cloudinaryService;

  private final String USER_ID = "user-123";
  private final String POST_ID = "post-456";
  private final String COMMENT_ID = "comment-789";
  private final String FOLLOWEE_ID = "followee-999";
  private final LocalDateTime NOW = LocalDateTime.now();

  private User mockUser;
  private PostRequest postRequest;
  private PostResponse postResponse;
  private CommentRequest commentRequest;
  private CommentResponse commentResponse;
  private FollowResponse followResponse;
  private PostLikeResponse likeResponse;
  private SavedPostResponse savedPostResponse;
  private Page<PostResponse> postPage;

  @BeforeEach
  void initData() {
    mockUser = User.builder().id(USER_ID).username("testuser").email("test@example.com").build();

    postRequest = PostRequest.builder().title("Test Title").content("Test content").build();

    postResponse =
        PostResponse.builder()
            .id(POST_ID)
            .userId(USER_ID)
            .username("testuser")
            .title("Test Title")
            .content("Test content")
            .likeCount(0)
            .commentCount(0)
            .likedByMe(false)
            .savedByMe(false)
            .createdAt(NOW)
            .updatedAt(NOW)
            .build();

    commentRequest = CommentRequest.builder().postId(POST_ID).content("Nice post!").build();

    commentResponse =
        CommentResponse.builder()
            .id(COMMENT_ID)
            .postId(POST_ID)
            .userId(USER_ID)
            .username("testuser")
            .content("Nice post!")
            .createdAt(NOW)
            .build();

    followResponse =
        FollowResponse.builder()
            .id("follow-1")
            .followerId(USER_ID)
            .followerName("testuser")
            .followeeId(FOLLOWEE_ID)
            .followeeName("otheruser")
            .createdAt(NOW)
            .build();

    likeResponse =
        PostLikeResponse.builder()
            .id("like-1")
            .postId(POST_ID)
            .userId(USER_ID)
            .username("testuser")
            .createdAt(NOW)
            .build();

    savedPostResponse =
        SavedPostResponse.builder()
            .id("saved-1")
            .postId(POST_ID)
            .post(postResponse)
            .createdAt(NOW)
            .build();

    postPage = new PageImpl<>(List.of(postResponse));

    Mockito.when(learningProgressService.getCurrentUser()).thenReturn(mockUser);
  }

  // ───── POSTS ─────

  @Test
  void createPost_success() throws Exception {
    Mockito.when(postService.createPost(any(PostRequest.class), eq(USER_ID)))
        .thenReturn(postResponse);

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(postRequest);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/community/posts")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.id").value(POST_ID))
        .andExpect(jsonPath("result.title").value("Test Title"));
  }

  @Test
  void getFeed_success() throws Exception {
    Mockito.when(postService.getFeed(any(Pageable.class), eq(USER_ID))).thenReturn(postPage);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/posts").with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.content[0].id").value(POST_ID));
  }

  @Test
  void getPost_success() throws Exception {
    Mockito.when(postService.getPostById(eq(POST_ID), eq(USER_ID))).thenReturn(postResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/posts/{postId}", POST_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.id").value(POST_ID));
  }

  @Test
  void getPost_notFound_fail() throws Exception {
    Mockito.when(postService.getPostById(eq(POST_ID), eq(USER_ID)))
        .thenThrow(new AppException(ErrorCode.ID_NOT_EXISTED));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/posts/{postId}", POST_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(1009))
        .andExpect(jsonPath("message").value("id not exists"));
  }

  @Test
  void getUserPosts_success() throws Exception {
    Mockito.when(postService.getUserPosts(eq(USER_ID), any(Pageable.class), eq(USER_ID)))
        .thenReturn(postPage);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/users/{userId}/posts", USER_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.content[0].id").value(POST_ID));
  }

  @Test
  void deletePost_success() throws Exception {
    Mockito.doNothing().when(postService).deletePost(eq(POST_ID), eq(USER_ID));

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/community/posts/{postId}", POST_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000));
  }

  @Test
  void deletePost_notOwner_fail() throws Exception {
    Mockito.doThrow(new AppException(ErrorCode.UNAUTHORIZED))
        .when(postService)
        .deletePost(eq(POST_ID), eq(USER_ID));

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/community/posts/{postId}", POST_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("code").value(1007));
  }

  // ───── COMMENTS ─────

  @Test
  void createComment_success() throws Exception {
    Mockito.when(commentService.createComment(any(CommentRequest.class), eq(USER_ID)))
        .thenReturn(commentResponse);

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(commentRequest);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/community/comments")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.id").value(COMMENT_ID))
        .andExpect(jsonPath("result.content").value("Nice post!"));
  }

  @Test
  void getComments_success() throws Exception {
    Mockito.when(commentService.getCommentsByPost(eq(POST_ID)))
        .thenReturn(List.of(commentResponse));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/posts/{postId}/comments", POST_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result[0].id").value(COMMENT_ID));
  }

  @Test
  void deleteComment_success() throws Exception {
    Mockito.doNothing().when(commentService).deleteComment(eq(COMMENT_ID), eq(USER_ID));

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/community/comments/{commentId}", COMMENT_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000));
  }

  // ───── LIKES ─────

  @Test
  void likePost_success() throws Exception {
    Mockito.when(postLikeService.likePost(eq(POST_ID), eq(USER_ID))).thenReturn(likeResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/community/posts/{postId}/like", POST_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.id").value("like-1"));
  }

  @Test
  void unlikePost_success() throws Exception {
    Mockito.doNothing().when(postLikeService).unlikePost(eq(POST_ID), eq(USER_ID));

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/community/posts/{postId}/like", POST_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000));
  }

  @Test
  void isLiked_true() throws Exception {
    Mockito.when(postLikeService.isLiked(eq(POST_ID), eq(USER_ID))).thenReturn(true);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/posts/{postId}/liked", POST_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result").value(true));
  }

  @Test
  void isLiked_false() throws Exception {
    Mockito.when(postLikeService.isLiked(eq(POST_ID), eq(USER_ID))).thenReturn(false);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/posts/{postId}/liked", POST_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result").value(false));
  }

  // ───── FOLLOWING FEED ─────

  @Test
  void getFollowingFeed_success() throws Exception {
    Mockito.when(postService.getFollowingFeed(any(Pageable.class), eq(USER_ID)))
        .thenReturn(postPage);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/feed/following")
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.content[0].id").value(POST_ID));
  }

  // ───── FOLLOWS ─────

  @Test
  void follow_success() throws Exception {
    Mockito.when(followService.follow(eq(FOLLOWEE_ID), eq(USER_ID))).thenReturn(followResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/community/follow/{followeeId}", FOLLOWEE_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.followeeId").value(FOLLOWEE_ID));
  }

  @Test
  void unfollow_success() throws Exception {
    Mockito.doNothing().when(followService).unfollow(eq(FOLLOWEE_ID), eq(USER_ID));

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/community/follow/{followeeId}", FOLLOWEE_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000));
  }

  @Test
  void getFollowing_success() throws Exception {
    Mockito.when(followService.getFollowing(eq(USER_ID))).thenReturn(List.of(followResponse));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/users/{userId}/following", USER_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result[0].followeeId").value(FOLLOWEE_ID));
  }

  @Test
  void getFollowers_success() throws Exception {
    Mockito.when(followService.getFollowers(eq(USER_ID))).thenReturn(List.of(followResponse));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/users/{userId}/followers", USER_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result[0].followerId").value(USER_ID));
  }

  @Test
  void getFollowingCount_success() throws Exception {
    Mockito.when(followService.getFollowingCount(eq(USER_ID))).thenReturn(5);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/users/{userId}/following/count", USER_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result").value(5));
  }

  @Test
  void getFollowerCount_success() throws Exception {
    Mockito.when(followService.getFollowerCount(eq(USER_ID))).thenReturn(10);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/users/{userId}/followers/count", USER_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result").value(10));
  }

  @Test
  void isFollowing_success() throws Exception {
    Mockito.when(followService.isFollowing(eq(USER_ID), eq(FOLLOWEE_ID))).thenReturn(true);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/follow/{followeeId}/check", FOLLOWEE_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result").value(true));
  }

  // ───── SAVED POSTS ─────

  @Test
  void savePost_success() throws Exception {
    Mockito.when(savedPostService.savePost(eq(POST_ID), eq(USER_ID))).thenReturn(savedPostResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/community/posts/{postId}/save", POST_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.postId").value(POST_ID));
  }

  @Test
  void unsavePost_success() throws Exception {
    Mockito.doNothing().when(savedPostService).unsavePost(eq(POST_ID), eq(USER_ID));

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/community/posts/{postId}/save", POST_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000));
  }

  @Test
  void getSavedPosts_success() throws Exception {
    Mockito.when(savedPostService.getSavedPosts(eq(USER_ID)))
        .thenReturn(List.of(savedPostResponse));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/saved-posts")
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result[0].postId").value(POST_ID));
  }

  @Test
  void isSaved_true() throws Exception {
    Mockito.when(savedPostService.isSaved(eq(POST_ID), eq(USER_ID))).thenReturn(true);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/posts/{postId}/saved", POST_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result").value(true));
  }

  @Test
  void isSaved_false() throws Exception {
    Mockito.when(savedPostService.isSaved(eq(POST_ID), eq(USER_ID))).thenReturn(false);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/community/posts/{postId}/saved", POST_ID)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result").value(false));
  }

  // ───── UPLOAD ─────

  @Test
  void uploadImage_success() throws Exception {
    String imageUrl = "https://res.cloudinary.com/...";
    Mockito.when(cloudinaryService.uploadFile(any(), eq("community/posts"))).thenReturn(imageUrl);

    MockMultipartFile file =
        new MockMultipartFile("file", "test.png", "image/png", "fake-image-bytes".getBytes());

    mockMvc
        .perform(
            MockMvcRequestBuilders.multipart("/api/community/upload-image")
                .file(file)
                .with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result").value(imageUrl));
  }
}
