/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.identity_servive.dto.request.AuthRequest.PermissionRequest;
import com.example.identity_servive.dto.response.authResponse.PermissionResponse;
import com.example.identity_servive.entity.auth.Permission;
import com.example.identity_servive.mapper.PermissionMapper;
import com.example.identity_servive.repository.auth.PermissionRepository;
import com.example.identity_servive.service.auth.PermissionService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class PermissionServiceTest {

  @Autowired private PermissionService permissionService;

  @MockitoBean private PermissionRepository permissionRepository;
  @MockitoBean private PermissionMapper permissionMapper;

  private PermissionRequest request;
  private Permission permission;
  private PermissionResponse response;

  @BeforeEach
  void initData() {
    request =
        PermissionRequest.builder().name("READ_USER").description("Read user permission").build();

    permission = Permission.builder().name("READ_USER").description("Read user permission").build();

    response =
        PermissionResponse.builder().name("READ_USER").description("Read user permission").build();
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void create_success() {
    when(permissionMapper.toPermission(any())).thenReturn(permission);
    when(permissionRepository.save(any())).thenReturn(permission);
    when(permissionMapper.toPermissionResponse(any())).thenReturn(response);

    var result = permissionService.create(request);

    assertThat(result.getName()).isEqualTo("READ_USER");
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getAll_success() {
    when(permissionRepository.findAll()).thenReturn(List.of(permission));
    when(permissionMapper.toPermissionResponse(any())).thenReturn(response);

    var result = permissionService.getAll();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("READ_USER");
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void delete_success() {
    assertDoesNotThrow(() -> permissionService.delete("READ_USER"));
  }
}
