/* (C)2026 */
package com.example.identity_servive.controller;

import com.example.identity_servive.dto.request.UserCreationRequest;
import com.example.identity_servive.dto.response.UserResponse;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

  @Container static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0");

  @Autowired private MockMvc mockMvc;
  private UserCreationRequest request;
  private UserResponse userResponse;
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
            .id("9900193e-1262-451e-b17b-b9be5f62af78")
            .userName("lamdzbodoi")
            .firstName("lamdzbodoi")
            .lastName("dzbodoi")
            .birthDate(dob)
            .build();
  }

  @Test
  void CreateUser_validRequest_success() throws Exception {
    // GIVEN
    ObjectMapper mapper = new ObjectMapper();
    mapper.registeredModules();
    String content = mapper.writeValueAsString(request);

    // WHEN
    //    mockMvc
    //        .perform(
    //            MockMvcRequestBuilders.post("/users")
    //                .contentType(MediaType.APPLICATION_JSON_VALUE)
    //                .content(content))
    //        .andExpect(status().isOk())
    //        .andExpect(jsonPath("code").value(1000))
    //        .andExpect(
    //            MockMvcResultMatchers.jsonPath("result.id")
    //                .value("9900193e-1262-451e-b17b-b9be5f62af78"));

    // THEN
  }
}
