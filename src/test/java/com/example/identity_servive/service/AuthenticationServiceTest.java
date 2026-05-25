/* (C)2026 */
package com.example.identity_servive.service;

// Hoặc nếu không dùng static import:
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.identity_servive.configuration.CloudinaryConfig;
import com.example.identity_servive.dto.request.AuthRequest.AuthenticationRequest;
import com.example.identity_servive.dto.request.AuthRequest.IntrospectRequest;
import com.example.identity_servive.dto.request.AuthRequest.LogoutRequest;
import com.example.identity_servive.dto.request.AuthRequest.RefreshRequest;
import com.example.identity_servive.dto.response.authResponse.ExchangeTokenResponse;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.mapper.UserMapper;
import com.example.identity_servive.repository.auth.InvalidatedTokenRepository;
import com.example.identity_servive.repository.auth.OutboundIdentityClient;
import com.example.identity_servive.repository.auth.RoleRepository;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.service.auth.AuthenticationService;
import com.example.identity_servive.service.cloudinary.CloudinaryService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class AuthenticationServiceTest {
  @Autowired private AuthenticationService authenticationService;
  @MockitoBean private UserRepository userRepository;
  @MockitoBean private InvalidatedTokenRepository invalidatedTokenRepository;
  @MockitoBean private OutboundIdentityClient outboundIdentityClient;
  @MockitoBean private PasswordEncoder passwordEncoder;
  @MockitoBean private CloudinaryService cloudinaryService;
  @MockitoBean private CloudinaryConfig cloudinaryConfig;
  @MockitoBean private UserMapper userMapper; // ← THÊM
  @MockitoBean private RoleRepository roleRepository; // ← THÊM

  @Value("${jwt.signerKey}")
  private String SIGNER_KEY;

  String fakeToken =
      "eyJhbGciOiJIUzUxMiJ9" // header: {"alg":"HS512"}
          + ".eyJzdWIiOiJ0ZXN0IiwiZXhwIjoyNTI0NjAwMDAwMH0" // payload:
          // {"sub":"test","exp":2524600000000}
          + ".invalidsignature";
  private AuthenticationRequest request;
  private User user;

  @BeforeEach
  void initData() {
    request =
        AuthenticationRequest.builder()
            .username("lamdzbodoi")
            .email("lamdzbodoi@gmail.com")
            .password("lamdzbodoi")
            .build();
    user =
        User.builder()
            .id("9900193e-1262-451e-b17b-b9be5f62af789")
            .username("lamdzbodoi")
            .email("lamdzbodoi@gmail.com")
            .password("encodedPassword")
            .build();
  }

  @Test
  void authenticate_userNotFound_fail() {
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    var exception =
        assertThrows(AppException.class, () -> authenticationService.authenticate(request));

    assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);
  }

  @Test
  void authenticate_passwordWrong_fail() {
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

    var exception =
        assertThrows(AppException.class, () -> authenticationService.authenticate(request));

    assertThat(exception.getErrorCode().getCode()).isEqualTo(1006);
  }

  @Test
  void authenticate_validRequest_success() {
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

    var response = authenticationService.authenticate(request);

    assertThat(response.isAuthenticated()).isTrue();
    assertThat(response.getToken()).isNotNull();
  }

  @Test
  void introspect_validToken_success() throws JOSEException, ParseException {
    String validToken = generateTestToken();
    var request = new IntrospectRequest(validToken);

    when(invalidatedTokenRepository.existsById(anyString())).thenReturn(false);

    var response = authenticationService.introspect(request);

    assertThat(response.isValid()).isTrue();
  }

  @Test
  void introspect_invalidToken_fail() throws JOSEException, ParseException {
    String invalidToken = generateTestToken();
    String fakeToken = invalidToken.substring(0, invalidToken.length() - 1) + "invalid";

    var request = new IntrospectRequest(fakeToken);

    var response = authenticationService.introspect(request);

    assertThat(response.isValid()).isFalse();
  }

  @Test
  void logout_success() throws JOSEException {
    String validToken = generateTestToken();
    var request = new LogoutRequest(validToken);
    assertDoesNotThrow(() -> authenticationService.logout(request));
  }

  @Test
  void refresh_token_success() throws JOSEException, ParseException {
    String validToken = generateTestToken();
    var request = new RefreshRequest(validToken);
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    var response = authenticationService.refreshToken(request);

    assertThat(response.isAuthenticated()).isTrue();
    assertThat(response.getToken()).isNotNull();
  }

  @Test
  void outboundAuthenticate_success() {
    var exchangeResponse =
        ExchangeTokenResponse.builder().accessToken("google-access-token").build();
    when(outboundIdentityClient.exchangeToken(any())).thenReturn(exchangeResponse);

    var response = authenticationService.outboundAuthenticate("auth-code");

    assertThat(response.getToken()).isEqualTo("google-access-token");
  }

  private String generateTestToken() throws JOSEException {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
    JWTClaimsSet claimsSet =
        new JWTClaimsSet.Builder()
            .subject("lamdzbodoi")
            .issuer("lamdzbodoi.com")
            .issueTime(new Date())
            .expirationTime(new Date(Instant.now().plus(3600, ChronoUnit.SECONDS).toEpochMilli()))
            .jwtID(UUID.randomUUID().toString())
            .build();
    Payload payload = new Payload(claimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(header, payload);
    jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
    return jwsObject.serialize();
  }
}
