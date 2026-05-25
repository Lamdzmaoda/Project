/* (C)2026 */
package com.example.identity_servive.controller.community;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.identity_servive.repository.community.CommentRepository;
import com.example.identity_servive.repository.community.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Slf4j
@SpringBootTest
@TestPropertySource("/test.properties")
@AutoConfigureMockMvc
public class AdminCommunityControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private PostRepository postRepository;
  @MockitoBean private CommentRepository commentRepository;

  private final String POST_ID = "post-456";
  private final String COMMENT_ID = "comment-789";

  @Test
  void adminDeletePost_success() throws Exception {
    Mockito.doNothing().when(postRepository).deleteById(POST_ID);

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/admin/community/posts/{postId}", POST_ID)
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000));
  }

  @Test
  void adminDeleteComment_success() throws Exception {
    Mockito.doNothing().when(commentRepository).deleteById(COMMENT_ID);

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/admin/community/comments/{commentId}", COMMENT_ID)
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000));
  }

  @Test
  void adminDeletePost_forbidden_whenNotAdmin() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/admin/community/posts/{postId}", POST_ID)
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void adminDeleteComment_forbidden_whenNotAdmin() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/admin/community/comments/{commentId}", COMMENT_ID)
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }
}
