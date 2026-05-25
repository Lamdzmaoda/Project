/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.identity_servive.dto.request.AuthRequest.RoleRequest;
import com.example.identity_servive.dto.response.authResponse.PermissionResponse;
import com.example.identity_servive.dto.response.authResponse.RoleRespone;
import com.example.identity_servive.entity.auth.Permission;
import com.example.identity_servive.entity.auth.Role;
import com.example.identity_servive.mapper.RoleMapper;
import com.example.identity_servive.repository.auth.PermissionRepository;
import com.example.identity_servive.repository.auth.RoleRepository;
import com.example.identity_servive.service.auth.RoleService;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class RoleServiceTest {

  @Autowired private RoleService roleService;

  @MockitoBean private RoleRepository roleRepository;
  @MockitoBean private PermissionRepository permissionRepository;
  @MockitoBean private RoleMapper roleMapper;

  private RoleRequest roleRequest;
  private Role role;
  private RoleRespone roleResponse;
  private Permission permission;
  private PermissionResponse permissionResponse;

  @BeforeEach
  void initData() {
    permissionResponse =
        PermissionResponse.builder().name("READ_USER").description("Read user permission").build();
    permission = Permission.builder().name("READ_USER").description("Read user permission").build();
    roleRequest =
        RoleRequest.builder()
            .name("MODERATOR")
            .description("Moderator role")
            .permissions(Set.of("READ_USER"))
            .build();

    role =
        Role.builder()
            .name("MODERATOR")
            .description("Moderator role")
            .permissions(Set.of(permission))
            .build();

    roleResponse =
        RoleRespone.builder()
            .name("MODERATOR")
            .description("Moderator role")
            .permissions(Set.of(permissionResponse))
            .build();
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void create_success() {
    when(roleMapper.toRole(any())).thenReturn(role);
    when(permissionRepository.findAllById(any())).thenReturn(List.of(permission));
    when(roleRepository.save(any())).thenReturn(role);
    when(roleMapper.toRoleResponse(any())).thenReturn(roleResponse);

    var result = roleService.create(roleRequest);

    assertThat(result.getName()).isEqualTo("MODERATOR");
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getAll_success() {
    when(roleRepository.findAll()).thenReturn(List.of(role));
    when(roleMapper.toRoleResponse(any())).thenReturn(roleResponse);

    var result = roleService.getAll();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("MODERATOR");
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void delete_success() {
    assertDoesNotThrow(() -> roleService.delete("MODERATOR"));
  }
}
