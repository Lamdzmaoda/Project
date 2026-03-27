/* (C)2026 */
package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.PermissionRequest;
import com.example.identity_servive.dto.response.PermissionResponse;
import com.example.identity_servive.entity.Permission;
import org.mapstruct.Mapper;

/**
 * Interface giúp chuyển đổi dữ liệu tự động giữa Entity User và các DTO. @Mapper(componentModel =
 * "spring"): MapStruct sẽ tự sinh ra class Implementation và đăng ký nó như một Bean trong Spring
 * Context để bạn có thể @Autowired.
 */
@Mapper(componentModel = "spring")
public interface PermissionMapper {
  Permission toPermission(PermissionRequest request);

  PermissionResponse toPermissionResponse(Permission permission);
}
