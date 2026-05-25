/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.community.Follow;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.community.FollowRepository;
import com.example.identity_servive.service.community.FollowService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class FollowServiceTest {

  @Autowired private FollowService followService;
  @MockitoBean private FollowRepository followRepository;
  @MockitoBean private UserRepository userRepository;

  private User follower;
  private User followee;
  private Follow follow;

  @BeforeEach
  void initData() {
    follower = User.builder().id("user-1").username("follower").email("follower@test.com").build();
    followee = User.builder().id("user-2").username("followee").email("followee@test.com").build();
    follow = Follow.builder().id("follow-1").follower(follower).followee(followee).build();
  }

  @Test
  void follow_self_fail() {
    assertThrows(AppException.class, () -> followService.follow("user-1", "user-1"));
  }

  @Test
  void follow_alreadyExists_fail() {
    when(userRepository.findById(any())).thenReturn(Optional.of(follower), Optional.of(followee));
    when(followRepository.existsByFollowerIdAndFolloweeId(any(), any())).thenReturn(true);
    assertThrows(AppException.class, () -> followService.follow("user-2", "user-1"));
  }

  @Test
  void follow_success() {
    when(userRepository.findById(any())).thenReturn(Optional.of(follower), Optional.of(followee));
    when(followRepository.existsByFollowerIdAndFolloweeId(any(), any())).thenReturn(false);
    when(followRepository.save(any())).thenReturn(follow);
    var result = followService.follow("user-2", "user-1");
    assertThat(result).isNotNull();
  }
}
