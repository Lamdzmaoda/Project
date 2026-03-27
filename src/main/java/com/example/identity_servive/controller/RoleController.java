/* (C)2026 */
package com.example.identity_servive.controller;

import com.example.identity_servive.dto.request.ApiResponse;
import com.example.identity_servive.dto.request.RoleRequest;
import com.example.identity_servive.dto.response.RoleRespone;
import com.example.identity_servive.service.RoleService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j // Hỗ trợ ghi log để theo dõi lịch sử gọi API
@RestController // Đánh dấu là REST API Controller
@RequestMapping("/roles") // Tất cả các API trong class này sẽ bắt đầu bằng /roles
@RequiredArgsConstructor // Tự động tạo Constructor để Inject RoleService
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Tự động thêm 'private final' cho các field
public class RoleController {

    // Tiêm RoleService để thực hiện logic nghiệp vụ lưu trữ vai trò
    RoleService roleService;

    /**
     * API Tạo mới một vai trò (Role)
     * URL: POST /roles
     */
    @PostMapping
    ApiResponse<RoleRespone> create(@RequestBody RoleRequest request) {
        // 1. Nhận thông tin Role từ Client (bao gồm tên role và danh sách permissions kèm theo)
        // 2. Chuyển cho Service xử lý và trả về kết quả qua ApiResponse
        return ApiResponse.<RoleRespone>builder()
                .result(roleService.create(request))
                .build();
    }

    /**
     * API Lấy danh sách tất cả các vai trò hiện có
     * URL: GET /roles
     */
    @GetMapping
    ApiResponse<List<RoleRespone>> getAll() {
        // Gọi Service lấy toàn bộ danh sách vai trò từ Database
        return ApiResponse.<List<RoleRespone>>builder()
                .result(roleService.getAll())
                .build();
    }

    /**
     * API Xóa một vai trò cụ thể
     * URL: DELETE /roles/{role}
     */
    @DeleteMapping("/{role}") // Lưu ý: Tham số path variable khớp với biến 'role' trong hàm
    ApiResponse<Void> delete(@PathVariable String role) {
        // 1. Nhận tên Role cần xóa từ URL
        // 2. Gọi lệnh xóa từ Service
        roleService.delete(role);

        // 3. Trả về phản hồi trống báo hiệu đã xóa thành công
        return ApiResponse.<Void>builder().build();
    }
}