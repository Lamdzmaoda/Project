package com.example.identity_servive.service;

import com.example.identity_servive.dto.request.UserCreationRequest;
import com.example.identity_servive.dto.request.UserUpdateRequest;
import com.example.identity_servive.dto.response.UserResponse;
import com.example.identity_servive.enums.Role;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.mapper.UserMapper;
import com.example.identity_servive.repository.RoleRepository;
import com.example.identity_servive.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.identity_servive.entity.User;

import java.util.HashSet;
import java.util.List;

/**
 * Service quản lý toàn bộ nghiệp vụ liên quan đến User.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository; // Truy cập DB
    UserMapper userMapper;         // Chuyển đổi DTO <-> Entity
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    /**
     * Tạo người dùng mới
     */
    public UserResponse createUser(UserCreationRequest request) {
        // 1. Kiểm tra trùng lặp username
        if (userRepository.existsByUserName(request.getUserName()))
            throw new AppException(ErrorCode.USER_EXISTED);

        // 2. Map dữ liệu từ Request DTO sang Entity User
        User user = userMapper.toUser(request);

        // 3. Mã hóa mật khẩu trước khi lưu (Bắt buộc để bảo mật!)
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        //user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUserName(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }
    /**
     * Cập nhật thông tin người dùng
     */
    public UserResponse updateUser(UserUpdateRequest request, String userId) {
        // 1. Tìm user cũ trong DB, nếu không có ném lỗi
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));

        // 2. MapStruct tự động cập nhật các field từ request vào đối tượng user hiện tại
        userMapper.updateUser(request, user);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        // 3. Lưu lại và map sang UserResponse để trả về (ẩn các trường nhạy cảm)
        return userMapper.toUserResponse(userRepository.save(user));
    }

    /**
     * Lấy toàn bộ danh sách User (Lưu ý: nên dùng UserResponse thay vì User Entity)
     */
    @PreAuthorize("hasAuthority('UPDATE_DATA')")
    public List<UserResponse> getUser() {
        log.info("In method getUser");
        // 1. Lấy danh sách Entity từ Database
        var users = userRepository.findAll();

        // 2. Chuyển đổi List<User> sang List<UserResponse>
        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    /**
     * Lấy thông tin chi tiết User qua ID
     */
    @PostAuthorize("returnObject.userName == authentication.name")
    public UserResponse getUserById(String id) {
        log.info("In method getUserById");
        return userMapper.toUserResponse(
                userRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED))
        );
    }

    /**
     * Xóa người dùng theo ID
     */
    public void deleteUserById(String userId) {
        userRepository.deleteById(userId);
    }
}