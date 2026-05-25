/* (C)2026 */
package com.example.identity_servive.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.identity_servive.dto.request.AuthRequest.AuthenticationRequest;
import com.example.identity_servive.dto.request.AuthRequest.IntrospectRequest;
import com.example.identity_servive.dto.request.AuthRequest.LogoutRequest;
import com.example.identity_servive.dto.request.AuthRequest.RefreshRequest;
import com.example.identity_servive.dto.response.authResponse.AuthenticationResponse;
import com.example.identity_servive.dto.response.authResponse.IntrospectResponse;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.service.auth.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
public class AuthenticationControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private AuthenticationService authenticationService;

  private AuthenticationRequest authRequest;
  private AuthenticationResponse authResponse;
  private IntrospectRequest introspectRequest;
  private IntrospectResponse introspectResponse;
  private RefreshRequest refreshRequest;
  private LogoutRequest logoutRequest;

  @BeforeEach
  void initData() {
    authRequest =
        AuthenticationRequest.builder().username("lamdzbodoi").password("lamdzbodoi").build();

    authResponse =
        AuthenticationResponse.builder().token("fake-jwt-token").authenticated(true).build();
    refreshRequest = new RefreshRequest("fake-jwt-token");
    logoutRequest = new LogoutRequest("fake-jwt-token");
    introspectRequest = new IntrospectRequest("fake-jwt-token");
    introspectResponse = IntrospectResponse.builder().valid(true).build();
  }

  @Test
  void authenticate_success() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String content = objectMapper.writeValueAsString(authRequest);

    Mockito.when(
            authenticationService.authenticate(ArgumentMatchers.any(AuthenticationRequest.class)))
        .thenReturn(authResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("result.token").value(authResponse.getToken()))
        .andExpect(jsonPath("result.authenticated").value(true));
  }

  @Test
  void introspect_success() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String content = objectMapper.writeValueAsString(introspectRequest);

    Mockito.when(authenticationService.introspect(ArgumentMatchers.any(IntrospectRequest.class)))
        .thenReturn(introspectResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/auth/introspect")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.valid").value(true));
  }

  @Test
  void refreshToken_success() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String content = objectMapper.writeValueAsString(refreshRequest);

    Mockito.when(authenticationService.refreshToken(ArgumentMatchers.any(RefreshRequest.class)))
        .thenReturn(authResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("result.token").value(authResponse.getToken()))
        .andExpect(jsonPath("result.authenticated").value(true));
  }

  @Test
  void logout_success() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(logoutRequest);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk());
  }

  @Test
  void authenticate_userNotFound_fail() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    String content = objectMapper.writeValueAsString(authRequest);

    Mockito.when(authenticationService.authenticate(ArgumentMatchers.any()))
        .thenThrow(new AppException(ErrorCode.USER_NOT_EXISTED));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("code").value(1005));
  }

  @Test
  void authenticate_wrongPassword_fail() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    String content = objectMapper.writeValueAsString(authRequest);

    Mockito.when(authenticationService.authenticate(ArgumentMatchers.any()))
        .thenThrow(new AppException(ErrorCode.UNAUTHENTICATED));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("code").value(1006));
  }

  @Test
  void introspect_invalidToken_fail() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    String content = objectMapper.writeValueAsString(introspectRequest);

    Mockito.when(authenticationService.introspect(ArgumentMatchers.any()))
        .thenReturn(IntrospectResponse.builder().valid(false).build());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/auth/introspect")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("result.valid").value(false));
  }

  @Test
  void refreshToken_expired_fail() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    String content = objectMapper.writeValueAsString(refreshRequest);

    Mockito.when(authenticationService.refreshToken(ArgumentMatchers.any()))
        .thenThrow(new AppException(ErrorCode.UNAUTHORIZED));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("code").value(1007));
  }

  @Test
  void outboundAuthenticate_success() throws Exception {
    Mockito.when(authenticationService.outboundAuthenticate(anyString())).thenReturn(authResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/auth/outbound/authentication")
                .param("code", "google-auth-code"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("result.token").value("fake-jwt-token"));
  }
}
