/* (C)2026 */
package com.example.identity_servive.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.identity_servive.dto.request.AuthRequest.UserCreationRequest;
import com.example.identity_servive.dto.request.AuthRequest.UserUpdateRequest;
import com.example.identity_servive.dto.response.authResponse.UserResponse;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.service.auth.AuthenticationService;
import com.example.identity_servive.service.auth.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@SpringBootTest
@TestPropertySource("/test.properties")
@AutoConfigureMockMvc
public class UserControllerTest {
  @MockitoBean private AuthenticationService authenticationService;
  @Autowired private MockMvc mockMvc;
  @MockitoBean private UserService userService;
  private UserCreationRequest request;
  private UserResponse userResponse;
  private LocalDate dob;
  private UserUpdateRequest updateRequest;

  @BeforeEach
  void initData() {
    dob = LocalDate.of(2005, 10, 6);

    request = UserCreationRequest.builder().username("lamdzbodoi").password("lamdzbodoi").build();
    updateRequest =
        UserUpdateRequest.builder().displayName("New Name").roles(List.of("USER")).build();
    userResponse =
        UserResponse.builder()
            .id("9900193e-1262-451e-b17b-b9be5f62af78")
            .username("lamdzbodoi")
            .displayName("lamdzbodoi")
            .birthDate(dob)
            .build();
  }

  @Test
  void CreateUser_validRequest_success() throws Exception {
    // GIVEN
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(request);

    Mockito.when(userService.createUser(any())).thenReturn(userResponse);
    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(1000))
        .andExpect(jsonPath("result.id").value("9900193e-1262-451e-b17b-b9be5f62af78"));

    // THEN
  }

  @Test
  void CreateUser_usernameInvalid_fail() throws Exception {
    // GIVEN
    request.setUsername("lam");
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(request);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(1002))
        .andExpect(
            MockMvcResultMatchers.jsonPath("message")
                .value("username must be at least 4 characters"));
  }

  @Test
  void CreateUser_usernameTolong_fail() throws Exception {
    // GIVEN
    request.setUsername("lamdzbodoihhehehehhahfbàibs á vmá vàbàhjà");
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(request);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(1027))
        .andExpect(
            MockMvcResultMatchers.jsonPath("message")
                .value("username must be at most 20 characters"));
  }

  @Test
  void CreateUser_usernameIsnull_fail() throws Exception {
    // GIVEN
    request.setUsername("    ");
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(request);

    // WHEN
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(1029))
        .andExpect(MockMvcResultMatchers.jsonPath("message").value("username is required"));
  }

  @Test
  void CreateUser_passwordInvalid_fail() throws Exception {
    request.setPassword("lamdz");
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(request);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(1003))
        .andExpect(
            MockMvcResultMatchers.jsonPath("message")
                .value("password must be at least 8 characters"));
  }

  @Test
  void CreateUser_passwordTolong_fail() throws Exception {
    request.setPassword("lamdzjiwfbkfb2817y472491649161956583658319796193516513eywquỷ73");
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(request);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(1028))
        .andExpect(
            MockMvcResultMatchers.jsonPath("message")
                .value("password must be at most 20 characters"));
  }

  @Test
  void CreateUser_passwordIsNull_fail() throws Exception {
    request.setPassword("  ");
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(request);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(1030))
        .andExpect(MockMvcResultMatchers.jsonPath("message").value("password is required"));
  }

  @Test
  void CreateUser_userExisted_fail() throws Exception {
    request.setUsername("lamdzbodoi");
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(request);
    Mockito.when(userService.createUser(any()))
        .thenThrow(new AppException((ErrorCode.USER_EXISTED)));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(1001))
        .andExpect(MockMvcResultMatchers.jsonPath("message").value("user exists"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void GetUsers_validRequest_success() throws Exception {

    Mockito.when(userService.getUser()).thenReturn(List.of(userResponse));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result[0].username").value("lamdzbodoi"));
  }

  @Test
  void GetMyInfo_validRequest_success() throws Exception {
    Mockito.when(userService.getMyInfo()).thenReturn(userResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/users/myInfo")
                .with(user("lamdzbodoi"))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.username").value("lamdzbodoi"));
  }

  @Test
  void GetUserById_validRequest_success() throws Exception {
    Mockito.when(userService.getUserById(ArgumentMatchers.anyString())).thenReturn(userResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/users/" + userResponse.getId())
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.username").value("lamdzbodoi"));
  }

  @Test
  void GetUserById_userNotFound_fail() throws Exception {
    Mockito.when(userService.getUserById(ArgumentMatchers.anyString()))
        .thenThrow(new AppException((ErrorCode.USER_NOT_EXISTED)));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/users/" + userResponse.getId())
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("code").value(1005))
        .andExpect(MockMvcResultMatchers.jsonPath("message").value("user not exists"));
  }

  @Test
  void UpdateUser_validRequest_success() throws Exception {
    Mockito.when(userService.updateUser(any(), anyString())).thenReturn(userResponse);
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(updateRequest);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/users/9900193e-1262-451e-b17b-b9be5f62af78")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("lamdzbodoi").roles("ADMIN"))
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.username").value("lamdzbodoi"))
        .andExpect(jsonPath("result.displayName").value("lamdzbodoi"));
  }

  @Test
  void GetMyInfo_userNotFound_fail() throws Exception {
    Mockito.when(userService.getMyInfo()).thenThrow(new AppException(ErrorCode.USER_NOT_EXISTED));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/users/myInfo")
                .with(user("lamdzbodoi"))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("code").value(1005))
        .andExpect(jsonPath("message").value("user not exists"));
  }

  @Test
  void UpdateUser_userNotFound_fail() throws Exception {
    Mockito.when(userService.updateUser(any(), anyString()))
        .thenThrow(new AppException(ErrorCode.USER_NOT_EXISTED));
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(updateRequest);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/users/9900193e-1262-451e-b17b-b9be5f62af78")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("admin").roles("ADMIN"))
                .content(content))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("code").value(1005));
  }

  @Test
  void UpdateUser_notOwner_fail() throws Exception {
    Mockito.when(userService.updateUser(any(), anyString()))
        .thenThrow(new AppException(ErrorCode.UNAUTHORIZED));
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(updateRequest);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/users/9900193e-1262-451e-b17b-b9be5f62af78")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("lamdzbodoi"))
                .content(content))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("code").value(1007));
  }

  @Test
  void DeleteUser_success() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/users/9900193e-1262-451e-b17b-b9be5f62af78")
                .with(user("admin").roles("ADMIN")))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("success"));
  }
}
