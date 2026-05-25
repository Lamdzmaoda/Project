/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.identity_servive.dto.request.learningRequest.EnrollmentRequest;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Language;
import com.example.identity_servive.entity.progress.Enrollment;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.mapper.EnrollmentMapper;
import com.example.identity_servive.repository.Progress.EnrollmentRepository;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.learning.LanguageRepository;
import com.example.identity_servive.service.learning.EnrollmentService;
import com.example.identity_servive.service.learning.LearningProgressService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class EnrollmentServiceTest {

  @Autowired private EnrollmentService enrollmentService;
  @MockitoBean private EnrollmentRepository enrollmentRepository;
  @MockitoBean private LanguageRepository languageRepository;
  @MockitoBean private UserRepository userRepository;
  @MockitoBean private EnrollmentMapper enrollmentMapper;
  @MockitoBean private LearningProgressService learningProgressService;

  private User user;
  private Language language;
  private Enrollment enrollment;

  @BeforeEach
  void initData() {
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("testuser", "password", null));
    user = User.builder().id("user-1").username("testuser").email("test@test.com").build();
    language = Language.builder().name("java").description("Java").build();
    enrollment = Enrollment.builder().id("enroll-1").user(user).language(language).build();
  }

  @Test
  void enrollCourse_success() {
    when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
    when(languageRepository.findById(any())).thenReturn(Optional.of(language));
    when(enrollmentRepository.findByUserAndLanguage(any(), any())).thenReturn(Optional.empty());
    when(enrollmentRepository.save(any())).thenReturn(enrollment);
    when(enrollmentMapper.toEnrollmentResponse(any()))
        .thenReturn(
            com.example.identity_servive.dto.response.progress.EnrollmentResponse.builder()
                .id("enroll-1")
                .userName("testuser")
                .build());

    var result = enrollmentService.enrollCourse(new EnrollmentRequest("java"));
    assertThat(result).isNotNull();
    assertThat(result.getUserName()).isEqualTo("testuser");
  }

  @Test
  void enrollCourse_languageNotFound_fail() {
    when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
    when(languageRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(
        AppException.class, () -> enrollmentService.enrollCourse(new EnrollmentRequest("invalid")));
  }

  @Test
  void enrollCourse_userNotFound_fail() {
    when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

    assertThrows(
        AppException.class, () -> enrollmentService.enrollCourse(new EnrollmentRequest("java")));
  }
}
