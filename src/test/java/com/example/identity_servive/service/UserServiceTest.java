/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
// Hoặc nếu không dùng static import:
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.identity_servive.dto.request.AuthRequest.UserCreationRequest;
import com.example.identity_servive.dto.response.authResponse.UserResponse;
import com.example.identity_servive.entity.auth.Role;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.mapper.UserMapper;
import com.example.identity_servive.repository.auth.RoleRepository;
import com.example.identity_servive.repository.auth.UserRepository;
import java.time.LocalDate;
import java.util.Optional;

import com.example.identity_servive.service.auth.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
  @Autowired private UserService userService;

  @MockitoBean private UserRepository userRepository;
    @MockitoBean private UserMapper userMapper;          // ← THÊM
    @MockitoBean private RoleRepository roleRepository;  // ← THÊM
    @MockitoBean private PasswordEncoder passwordEncoder;// ← THÊM

  private UserCreationRequest request;
  private UserResponse userResponse;
  private User user;
    private Role role;          // ← THÊM DÒNG NÀY
  private LocalDate dob;

  @BeforeEach
  void initData() {
    dob = LocalDate.of(2005, 10, 6);
      role = Role.builder()
              .name("USER")
              .description("User role")
              .build();
    request =
        UserCreationRequest.builder()
            .username("lamdzbodoi")
            .password("lamdzb")
                .email("lamdzbodoi@gmail.com")
            .build();

    userResponse =
        UserResponse.builder()
            .id("9900193e-1262-451e-b17b-b9be5f62af789")
            .username("lamdzbodoi")
                .displayName("lamdzbodoi")
                .email("lamdzbodoi@gmail.com")
            .birthDate(dob)
            .build();
    user =
        User.builder()
            .id("9900193e-1262-451e-b17b-b9be5f62af789")
            .username("lamdzbodoi")
            .displayName("hehehehhe")
                .email("lamdzbodoi@gmail.com")
            .birthDate(dob)
            .build();
  }

  @Test
  void CreateUser_validRequest_success() throws Exception {
    // GIVEN
    when(userRepository.existsByUsername(anyString())).thenReturn(false);
    when(userRepository.save(any())).thenReturn(user);
      when(userMapper.toUser(any())).thenReturn(user);                          // ← THÊM
      when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");  // ← THÊM
      when(roleRepository.findById(any())).thenReturn(Optional.of(role));       // ← THÊM
      when(userRepository.save(any())).thenReturn(user);
      when(userMapper.toUserResponse(any())).thenReturn(userResponse);          // ← THÊM
    // When
    var response = userService.createUser(request);

    // THEN
    Assertions.assertEquals("9900193e-1262-451e-b17b-b9be5f62af789", response.getId());
    Assertions.assertEquals("lamdzbodoi", response.getUsername());
    Assertions.assertEquals("lamdzbodoi@gmail.com", response.getEmail());
  }

  @Test
  void CreateUser_userExisted_fail() throws Exception {
    // GIVEN
    when(userRepository.existsByUsername(anyString())).thenReturn(true);

    // When
    var exception = assertThrows(AppException.class, () -> userService.createUser(request));

    assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
  }

  @Test
  @WithMockUser(username = "lamdzbodoi")
  void getMyInfo_valid_success() throws Exception {
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
      when(userMapper.toUserResponse(any())).thenReturn(userResponse);  // ← THÊM DÒNG NÀY
    var response = userService.getMyInfo();

    Assertions.assertEquals(response.getId(), "9900193e-1262-451e-b17b-b9be5f62af789");
    Assertions.assertEquals(response.getUsername(), "lamdzbodoi");
  }

  @Test
  @WithMockUser(username = "lamdzbodoi")
  void getMyInfo_userNotFound_error() throws Exception {
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));
      when(userMapper.toUserResponse(any())).thenReturn(userResponse);  // ← THÊM DÒNG NÀY
    var exception = assertThrows(AppException.class, () -> userService.getMyInfo());

    assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);
  }
}
