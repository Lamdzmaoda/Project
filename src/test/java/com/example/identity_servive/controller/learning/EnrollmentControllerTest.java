/* (C)2026 */
package com.example.identity_servive.controller.learning;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.identity_servive.dto.request.learningRequest.EnrollmentRequest;
import com.example.identity_servive.dto.response.progress.EnrollmentResponse;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.service.learning.EnrollmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class EnrollmentControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private EnrollmentService enrollmentService;

  @Test
  void enrollCourse_success() throws Exception {
    EnrollmentResponse response =
        EnrollmentResponse.builder().id("enroll-1").currentXp(0).userName("testuser").build();

    Mockito.when(enrollmentService.enrollCourse(any(EnrollmentRequest.class))).thenReturn(response);

    EnrollmentRequest request = new EnrollmentRequest("java");
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(request);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/enrollment")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.id").value("enroll-1"))
        .andExpect(jsonPath("result.userName").value("testuser"));
  }

  @Test
  void enrollCourse_languageNotFound_fail() throws Exception {
    Mockito.when(enrollmentService.enrollCourse(any(EnrollmentRequest.class)))
        .thenThrow(new AppException(ErrorCode.ID_NOT_EXISTED));

    EnrollmentRequest request = new EnrollmentRequest("invalid-language");
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(request);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/enrollment")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(1009));
  }
}
