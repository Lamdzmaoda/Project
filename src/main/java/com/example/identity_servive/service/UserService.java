/* (C)2026 */
package com.example.identity_servive.service;

import com.example.identity_servive.dto.request.UserCreationRequest;
import com.example.identity_servive.dto.request.UserUpdateRequest;
import com.example.identity_servive.dto.response.UserResponse;
import com.example.identity_servive.entity.User;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.mapper.UserMapper;
import com.example.identity_servive.repository.RoleRepository;
import com.example.identity_servive.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // Đánh dấu là tầng Service, nơi xử lý logic nghiệp vụ
@RequiredArgsConstructor // Tự động Inject các Repository, Mapper, Encoder qua Constructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    /**
     * Nghiệp vụ: Tạo người dùng mới
     */
    public UserResponse createUser(UserCreationRequest request) {
        // 1. Kiểm tra username đã tồn tại trong DB chưa
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        // 2. Chuyển đổi dữ liệu từ Request DTO sang Entity User
        User user = userMapper.toUser(request);

        // 3. Mã hóa mật khẩu (BCrypt) để đảm bảo an toàn nếu DB bị rò rỉ
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        try {
            // 4. Lưu User vào cơ sở dữ liệu
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            // Bắt lỗi vi phạm ràng buộc dữ liệu từ DB (như trùng Unique key)
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // 5. Trả về thông tin user đã tạo (ẩn password)
        return userMapper.toUserResponse(user);
    }

    /**
     * Nghiệp vụ: Lấy thông tin cá nhân của người dùng đang đăng nhập
     */
    public UserResponse getMyInfo() {
        // 1. Lấy thông tin xác thực từ Security Context (lấy từ Token gửi kèm)
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        // 2. Truy vấn User từ DB dựa trên username trong Token
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    /**
     * Nghiệp vụ: Cập nhật thông tin người dùng
     */
    public UserResponse updateUser(UserUpdateRequest request, String userId) {
        // 1. Tìm user hiện tại, nếu không có ném lỗi
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));

        // 2. Cập nhật các trường dữ liệu mới từ request vào entity hiện có thông qua Mapper
        userMapper.updateUser(request, user);

        // 3. Mã hóa lại mật khẩu mới
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 4. Tìm kiếm và cập nhật danh sách các Role mới cho User
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        // 5. Lưu và trả về kết quả
        return userMapper.toUserResponse(userRepository.save(user));
    }

    /**
     * Nghiệp vụ: Lấy danh sách toàn bộ User (Chỉ dành cho ADMIN)
     * @PreAuthorize: Kiểm tra quyền 'ADMIN' TRƯỚC khi cho phép thực thi hàm
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUser() {
        log.info("Admin is fetching all users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    /**
     * Nghiệp vụ: Lấy thông tin chi tiết một User
     * @PostAuthorize: Kiểm tra SAU KHI hàm chạy xong.
     * Đảm bảo: Chỉ ADMIN hoặc CHÍNH CHỦ tài khoản đó mới được xem thông tin này.
     */
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(String id) {
        log.info("Fetching user detail for id: {}", id);
        return userMapper.toUserResponse(
                userRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    /**
     * Nghiệp vụ: Xóa người dùng
     */
    public void deleteUserById(String userId) {
        userRepository.deleteById(userId);
    }
}