/* (C)2026 */
package com.example.identity_servive.service;

import com.example.identity_servive.dto.request.RoleRequest;
import com.example.identity_servive.dto.response.RoleRespone;
import com.example.identity_servive.mapper.RoleMapper;
import com.example.identity_servive.repository.PermissionRepository;
import com.example.identity_servive.repository.RoleRepository;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j // Hỗ trợ ghi log theo dõi luồng xử lý
@Service // Đăng ký lớp này là một Service (Bean) do Spring quản lý
@RequiredArgsConstructor // Tự động tạo Constructor để tiêm (Inject) các Repository và Mapper
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Mặc định các field là 'private final'
public class RoleService {

    // Repository để thao tác với bảng Role trong Database
    RoleRepository roleRepository;

    // Repository để truy vấn danh sách Permission nhằm liên kết với Role
    PermissionRepository permissionRepository;

    // Bộ chuyển đổi dữ liệu Role (Mapper)
    RoleMapper roleMapper;

    /**
     * Nghiệp vụ: Tạo một Vai trò (Role) mới
     * @param roleRequest Chứa tên Role và danh sách các ID của Permission đi kèm
     */
    public RoleRespone create(RoleRequest roleRequest) {
        // 1. Chuyển đổi thông tin cơ bản (name, description) từ Request sang thực thể Role
        var role = roleMapper.toRole(roleRequest);

        // 2. Tìm kiếm tất cả các thực thể Permission trong Database dựa trên danh sách ID gửi lên
        var permissions = permissionRepository.findAllById(roleRequest.getPermissions());

        // 3. Gán danh sách quyền tìm được vào đối tượng Role dưới dạng HashSet (để tránh trùng lặp)
        role.setPermissions(new HashSet<>(permissions));

        // 4. Lưu thực thể Role (đã kèm Permissions) vào Database
        role = roleRepository.save(role);

        // 5. Chuyển đổi kết quả sang Response DTO để trả về cho Controller
        return roleMapper.toRoleResponse(role);
    }

    /**
     * Nghiệp vụ: Lấy danh sách toàn bộ các Vai trò
     */
    public List<RoleRespone> getAll() {
        // Truy vấn tất cả Role, sau đó dùng Stream API để chuyển đổi từng cái sang Response DTO
        return roleRepository.findAll().stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    /**
     * Nghiệp vụ: Xóa một Vai trò cụ thể
     * @param role Tên (hoặc ID) của vai trò cần xóa
     */
    public void delete(String role) {
        // Xóa bản ghi Role trong Database theo khóa chính (ID)
        roleRepository.deleteById(role);
    }
}