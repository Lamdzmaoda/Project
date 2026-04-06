/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
// Hoặc nếu không dùng static import:
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.identity_servive.dto.response.UserResponse;
import com.example.identity_servive.entity.User;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.repository.UserRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
  @Autowired private UserService userService;

  @MockitoBean private UserRepository userRepository;

  private UserCreationRequest request;
  private UserResponse userResponse;
  private User user;
  private LocalDate dob;

  @BeforeEach
  void initData() {
    dob = LocalDate.of(2005, 10, 6);
    request =
        UserCreationRequest.builder()
            .userName("lamdzbodoi")
            .password("lamdzbodoi")

            .build();

    userResponse =
        UserResponse.builder()
            .id("9900193e-1262-451e-b17b-b9be5f62af789")
            .userName("lamdzbodoi")
            .firstName("lamdzbodoi")
            .lastName("dzbodoi")
            .birthDate(dob)
            .build();
    user =
        User.builder()
            .id("9900193e-1262-451e-b17b-b9be5f62af789")
            .userName("lamdzbodoi")
            .firstName("lamdzbodoi")
            .lastName("dzbodoi")
            .birthDate(dob)
            .build();
  }

  @Test
  void CreateUser_validRequest_success() throws Exception {
    // GIVEN
    when(userRepository.existsByUserName(anyString())).thenReturn(false);
    when(userRepository.save(any())).thenReturn(user);

    // When
    var response = userService.createUser(request);

    // THEN
    Assertions.assertEquals("9900193e-1262-451e-b17b-b9be5f62af789", response.getId());
    Assertions.assertEquals("lamdzbodoi", response.getUserName());
  }

  @Test
  void CreateUser_userExisted_fail() throws Exception {
    // GIVEN
    when(userRepository.existsByUserName(anyString())).thenReturn(true);

    // When
    var exception = assertThrows(AppException.class, () -> userService.createUser(request));

    assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
  }

  @Test
  @WithMockUser(username = "lamdzbodoi")
  void getMyInfo_valid_success() throws Exception {
    when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(user));

    var response = userService.getMyInfo();

    Assertions.assertEquals(response.getId(), "9900193e-1262-451e-b17b-b9be5f62af789");
    Assertions.assertEquals(response.getUserName(), "lamdzbodoi");
  }

  @Test
  @WithMockUser(username = "lamdzbodoi")
  void getMyInfo_userNotFound_error() throws Exception {
    when(userRepository.findByUserName(anyString())).thenReturn(Optional.ofNullable(null));

    var exception = assertThrows(AppException.class, () -> userService.getMyInfo());

    assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);
  }
}
