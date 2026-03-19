package com.example.identity_servive.controller;

import com.example.identity_servive.dto.request.ApiResponse;
import com.example.identity_servive.dto.request.UserCreationRequest;
import com.example.identity_servive.dto.request.UserUpdateRequest;
import com.example.identity_servive.dto.response.UserResponse;
import com.example.identity_servive.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.identity_servive.entity.User;

import java.util.List;

/**
 * Controller quản lý thông tin người dùng.
 * Cung cấp các API để tạo, sửa, xóa và lấy danh sách User.
 */
@Slf4j
@RestController // Khai báo đây là REST API Controller
@RequestMapping("/users") // Tất cả các API trong này đều bắt đầu bằng /users
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserControler {

    @Autowired // Tự động kết nối với Bean UserService để sử dụng các hàm nghiệp vụ
    UserService userService;

    /**
     * API Tạo người dùng mới
     * @param request Dữ liệu đầu vào từ Client (đã được @Valid kiểm tra các ràng buộc dữ liệu)
     * @return ApiResponse chứa thông tin User vừa tạo
     */
    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();

        // Gọi service xử lý logic tạo user và gán vào kết quả (result)
        response.setResult(userService.createUser(request));
        response.setCode(1000); // Mã code 1000 thường quy ước là thành công (Success)
        return response;
    }

    /**
     * API Lấy toàn bộ danh sách người dùng
     * @return Danh sách các đối tượng User
     */
    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info( grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder() // Điền List<UserResponse> vào đây
                .result(userService.getUser())
                .build();
    }

    /**
     * API Lấy thông tin chi tiết một người dùng theo ID
     * @param userId ID của người dùng truyền từ URL (ví dụ: /users/123)
     * @return Đối tượng UserResponse (Dữ liệu đã được lọc qua Mapper)
     */
    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable("userId") String userId) {
        return userService.getUserById(userId);
    }
    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    /**
     * API Cập nhật thông tin người dùng
     * @param request Chứa thông tin cần cập nhật (password, name, v.v.)
     * @param userId ID của người dùng cần sửa
     * @return Thông tin người dùng sau khi đã cập nhật
     */
    @PutMapping("/{userId}")
    UserResponse updateUser(@RequestBody UserUpdateRequest request, @PathVariable("userId") String userId) {
        return userService.updateUser(request, userId);
    }

    /**
     * API Xóa người dùng
     * @param userId ID của người dùng cần xóa
     * @return Chuỗi thông báo kết quả
     */
    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUserById(userId);
        return "success"; // Trả về thông báo xóa thành công
    }
}