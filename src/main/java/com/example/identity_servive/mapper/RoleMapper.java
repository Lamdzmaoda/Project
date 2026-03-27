/* (C)2026 */
package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.RoleRequest;
import com.example.identity_servive.dto.response.RoleRespone;
import com.example.identity_servive.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interface giúp chuyển đổi dữ liệu tự động giữa Entity User và các DTO. @Mapper(componentModel =
 * "spring"): MapStruct sẽ tự sinh ra class Implementation và đăng ký nó như một Bean trong Spring
 * Context để bạn có thể @Autowired.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {
  @Mapping(target = "permissions", ignore = true)
  Role toRole(RoleRequest request);

  RoleRespone toRoleResponse(Role role);
}
