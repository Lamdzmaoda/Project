/* (C)2026 */
package com.example.identity_servive.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.identity_servive.dto.request.AuthRequest.PermissionRequest;
import com.example.identity_servive.dto.response.authResponse.PermissionResponse;
import com.example.identity_servive.service.auth.PermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Slf4j
@SpringBootTest
@TestPropertySource("/test.properties")
@AutoConfigureMockMvc
public class PermissionControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private PermissionService permissionService;

  private PermissionRequest request;
  private PermissionResponse response;

  @BeforeEach
  void initData() {
    request =
        PermissionRequest.builder().name("READ_USER").description("Read user permission").build();

    response =
        PermissionResponse.builder().name("READ_USER").description("Read user permission").build();
  }

  @Test
  void create_success() throws Exception {
    Mockito.when(permissionService.create(ArgumentMatchers.any())).thenReturn(response);
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(request);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/permissions")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("result.name").value("READ_USER"));
  }

  @Test
  void getAll_success() throws Exception {
    Mockito.when(permissionService.getAll()).thenReturn(List.of(response));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/permissions").with(user("admin").roles("ADMIN")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("result[0].name").value("READ_USER"));
  }

  @Test
  void delete_success() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/permissions/READ_USER")
                .with(user("admin").roles("ADMIN")))
        .andExpect(status().isOk());
  }
}
