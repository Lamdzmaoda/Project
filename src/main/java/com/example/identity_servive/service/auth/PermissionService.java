/* (C)2026 */
package com.example.identity_servive.service.auth;

import com.example.identity_servive.dto.request.AuthRequest.PermissionRequest;
import com.example.identity_servive.dto.response.authResponse.PermissionResponse;
import com.example.identity_servive.entity.auth.Permission;
import com.example.identity_servive.mapper.PermissionMapper;
import com.example.identity_servive.repository.auth.PermissionRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Slf4j // Hỗ trợ ghi lại lịch sử hoạt động (Logging)
@Service // Đăng ký lớp này là một Service do Spring quản lý (Bean)
@RequiredArgsConstructor // Tự động tạo Constructor để tiêm (Inject) các Repository và Mapper vào
@FieldDefaults(
    level = AccessLevel.PRIVATE,
    makeFinal = true) // Mặc định mọi field là 'private final'
@PreAuthorize("hasRole('ADMIN')")
public class PermissionService {

  // Tiêm Repository để thao tác trực tiếp với bảng Permission trong Database
  PermissionRepository permissionRepository;

  // Tiêm Mapper để chuyển đổi qua lại giữa đối tượng Request/Response và Entity
  PermissionMapper permissionMapper;

  /**
   * Nghiệp vụ: Tạo một quyền hạn mới
   * @param request Chứa tên và mô tả quyền từ Client
   */
  public PermissionResponse create(PermissionRequest request) {
    // 1. Chuyển đổi dữ liệu từ Request DTO sang Entity (Permission)
    Permission permission = permissionMapper.toPermission(request);

    // 2. Lưu thực thể Permission vào Database
    permission = permissionRepository.save(permission);

    // 3. Chuyển đổi thực thể vừa lưu ngược lại thành Response DTO để trả về cho Controller
    return permissionMapper.toPermissionResponse(permission);
  }

  /**
   * Nghiệp vụ: Lấy danh sách tất cả các quyền
   */
  public List<PermissionResponse> getAll() {
    // 1. Lấy toàn bộ danh sách Permission từ Database
    var permissions = permissionRepository.findAll();

    // 2. Duyệt qua danh sách, chuyển từng Entity thành Response DTO và trả về dưới dạng List
    return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
  }

  /**
   * Nghiệp vụ: Xóa một quyền hạn
   * @param permission Tên (hoặc ID) của quyền cần xóa
   */
  public void delete(String permission) {
    // Thực hiện xóa bản ghi trong DB dựa trên ID được truyền vào
    permissionRepository.deleteById(permission);
  }
}
