/* (C)2026 */
package com.example.identity_servive.controller.auth;

import com.example.identity_servive.dto.response.ApiResponse;
import com.example.identity_servive.dto.request.AuthRequest.PermissionRequest;
import com.example.identity_servive.dto.response.authResponse.PermissionResponse;
import com.example.identity_servive.service.auth.PermissionService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j // Hỗ trợ ghi log để theo dõi luồng dữ liệu
@RestController // Đánh dấu là REST Controller, tự động chuyển kết quả trả về thành JSON
@RequestMapping("/permissions") // Định nghĩa đường dẫn gốc cho các API trong class này là /permissions
@RequiredArgsConstructor // Tự động tạo Constructor để Inject PermissionService
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Tự động biến các field thành 'private final'
public class PermissionController {

    // Tiêm Service xử lý nghiệp vụ liên quan đến Permission
    PermissionService permissionService;

    /**
     * API Tạo mới một Permission
     * URL: POST /permissions
     */
    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        // 1. Nhận dữ liệu từ Client, đẩy xuống Service để lưu vào DB
        // 2. Bọc kết quả trả về vào đối tượng ApiResponse chuẩn
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    /**
     * API Lấy danh sách tất cả các Permission hiện có
     * URL: GET /permissions
     */
    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        // Gọi Service lấy toàn bộ danh sách và trả về cho Client
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    /**
     * API Xóa một Permission dựa trên tên (ID)
     * URL: DELETE /permissions/{permission}
     */
    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String permission) {
        // 1. Lấy tên permission từ đường dẫn (URL Path)
        // 2. Gọi Service thực hiện lệnh xóa
        permissionService.delete(permission);

        // 3. Trả về phản hồi trống (chỉ báo thành công)
        return ApiResponse.<Void>builder().build();
    }
}