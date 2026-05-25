/* (C)2026 */
package com.example.identity_servive.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.identity_servive.dto.request.AuthRequest.RoleRequest;
import com.example.identity_servive.dto.response.authResponse.RoleRespone;
import com.example.identity_servive.service.auth.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
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
public class RoleControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private RoleService roleService;

  private RoleRequest roleRequest;
  private RoleRespone roleResponse;

  @BeforeEach
  void initData() {
    roleRequest =
        RoleRequest.builder()
            .name("MODERATOR")
            .description("Moderator role")
            .permissions(Set.of("READ_USER"))
            .build();

    roleResponse = RoleRespone.builder().name("MODERATOR").description("Moderator role").build();
  }

  @Test
  void create_success() throws Exception {
    Mockito.when(roleService.create(ArgumentMatchers.any())).thenReturn(roleResponse);
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(roleRequest);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/roles")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("result.name").value("MODERATOR"));
  }

  @Test
  void getAll_success() throws Exception {
    Mockito.when(roleService.getAll()).thenReturn(List.of(roleResponse));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/roles").with(user("admin").roles("ADMIN")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("result[0].name").value("MODERATOR"));
  }

  @Test
  void delete_success() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/roles/MODERATOR").with(user("admin").roles("ADMIN")))
        .andExpect(status().isOk());
  }
}
