/* (C)2026 */
package com.example.identity_servive.controller.auth;

import com.example.identity_servive.dto.response.ApiResponse;
import com.example.identity_servive.dto.request.AuthRequest.UserCreationRequest;
import com.example.identity_servive.dto.request.AuthRequest.UserUpdateRequest;
import com.example.identity_servive.dto.response.authResponse.UserResponse;
import com.example.identity_servive.service.auth.UserService;
import com.example.identity_servive.service.cloudinary.CloudinaryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller quản lý thông tin người dùng.
 */
@Slf4j // Hỗ trợ ghi log ra console
@RestController // Đánh dấu là REST API Controller trả về JSON
@RequestMapping("/users") // Đường dẫn gốc là /users
@RequiredArgsConstructor // Tự động tạo Constructor cho các biến final
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserControler {

    @Autowired // Tiêm lớp UserService để xử lý logic nghiệp vụ
    UserService userService;
    CloudinaryService cloudinaryService;
    /**
     * API Tạo người dùng mới (Đăng ký)
     * URL: POST /users
     */
    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        // @Valid: Tự động kiểm tra các ràng buộc dữ liệu (Validation) định nghĩa trong Request DTO
        ApiResponse<UserResponse> response = new ApiResponse<>();

        // 1. Gọi service thực hiện tạo user
        response.setResult(userService.createUser(request));
        // 2. Trả về mã thành công 1000
        response.setCode(1000);
        return response;
    }

    /**
     * API Lấy danh sách tất cả người dùng
     * URL: GET /users
     */
    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        // Lấy thông tin xác thực hiện tại từ bộ nhớ Security của Spring
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        // In log tên người dùng và các quyền (Role) họ đang có để kiểm tra
        assert authentication != null;
        log.info("Username: {}", authentication.getName());
        authentication
                .getAuthorities()
                .forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        // Gọi Service lấy danh sách và bọc trong ApiResponse
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUser())
                .build();
    }

    /**
     * API Lấy chi tiết một người dùng theo ID
     * URL: GET /users/{userId}
     */
    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable("userId") String userId) {
        // PathVariable: Lấy ID từ trên thanh địa chỉ truyền xuống
        return userService.getUserById(userId);
    }

    /**
     * API Lấy thông tin cá nhân của chính người dùng đang đăng nhập
     * URL: GET /users/myInfo
     */
    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        // Service sẽ tự lấy thông tin từ Token người dùng gửi kèm để tìm kiếm
        return ApiResponse.<UserResponse>builder().result(userService.getMyInfo()).build();
    }

    /**
     * API Cập nhật thông tin người dùng
     * URL: PUT /users/{userId}
     */
    @PutMapping("/{userId}")
    UserResponse updateUser(
            @RequestBody UserUpdateRequest request, @PathVariable("userId") String userId) {
        // Nhận dữ liệu cần sửa và ID người dùng để thực hiện cập nhật
        return userService.updateUser(request, userId);
    }
    @PostMapping(value = "/upload-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String url = cloudinaryService.uploadFile(file, "avatars");
        userService.updateAvatar(url);
        return ApiResponse.<String>builder().result(url).build();
    }
    /**
     * API Xóa người dùng khỏi hệ thống
     * URL: DELETE /users/{userId}
     */
    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable("userId") String userId) {
        // Gọi Service thực hiện lệnh xóa trong Database
        userService.deleteUserById(userId);
        return "success"; // Trả về thông báo thành công dạng chuỗi
    }
}