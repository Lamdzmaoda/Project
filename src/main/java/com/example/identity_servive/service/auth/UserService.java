/* (C)2026 */
package com.example.identity_servive.service.auth;

import com.example.identity_servive.constant.PredefinedRole;
import com.example.identity_servive.dto.request.AuthRequest.UserCreationRequest;
import com.example.identity_servive.dto.request.AuthRequest.UserUpdatePasswordRequest;
import com.example.identity_servive.dto.request.AuthRequest.UserUpdateRequest;
import com.example.identity_servive.dto.response.authResponse.UserResponse;
import com.example.identity_servive.entity.auth.Role;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.mapper.UserMapper;
import com.example.identity_servive.repository.auth.RoleRepository;
import com.example.identity_servive.repository.auth.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
  UserSecurity userSecurity;

  /**
   * Nghiệp vụ: Tạo người dùng mới
   */
  public UserResponse createUser(UserCreationRequest request) {
    // 1. Kiểm tra username đã tồn tại trong DB chưa
    if (userRepository.existsByUsername(request.getUsername()))
      throw new AppException(ErrorCode.USER_EXISTED);
    else if (userRepository.existsByEmail(request.getEmail()))
      throw new AppException(ErrorCode.EMAIL_EXISTED);
    // 2. Chuyển đổi dữ liệu từ Request DTO sang Entity User
    User user = userMapper.toUser(request);

    // 3. Mã hóa mật khẩu (BCrypt) để đảm bảo an toàn nếu DB bị rò rỉ
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    Role userRole =
        roleRepository
            .findById(PredefinedRole.USER_ROLE)
            .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED_EXISTED));
    var roles = new HashSet<Role>();
    roles.add(userRole);
    user.setRoles(roles);

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
  @PostAuthorize("returnObject.username == authentication.name")
  public UserResponse getMyInfo() {
    var user = getCurrentUser();
    return userMapper.toUserResponse(user);
  }

  private User getCurrentUser() {
    var context = SecurityContextHolder.getContext();
    String name = Objects.requireNonNull(context.getAuthentication()).getName();
    return userRepository
        .findByUsername(name)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
  }

  /**
   * Nghiệp vụ: Cập nhật thông tin người dùng
   */
  @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#userId)")
  public UserResponse updateUser(UserUpdateRequest request, String userId) {
    // 1. Tìm user hiện tại, nếu không có ném lỗi
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    // 4. Tìm kiếm và cập nhật danh sách các Role mới cho User
    if (request.getRoles() == null) {
      throw new AppException(ErrorCode.NULL_POINTER);
    }
    // 2. Cập nhật các trường dữ liệu mới từ request vào entity hiện có thông qua Mapper
    userMapper.updateUser(request, user);

    var roles = roleRepository.findAllById(request.getRoles());
    user.setRoles(new HashSet<>(roles));
    user.getLevel();

    // 5. Lưu và trả về kết quả
    return userMapper.toUserResponse(userRepository.save(user));
  }

  public UserResponse updatePassword(UserUpdatePasswordRequest request) {
    if (request == null || request.getOldPassword() == null || request.getNewPassword() == null) {
      throw new AppException(ErrorCode.INVALID_REQUEST);
    }
    var user = getCurrentUser();
    // So khớp mật khẩu cũ (raw) với mật khẩu đã mã hóa trong DB
    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }
    // Không cho phép đặt lại bằng chính mật khẩu cũ
    if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
      throw new AppException(ErrorCode.INVALID_REQUEST);
    }
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    return userMapper.toUserResponse(userRepository.save(user));
  }

  /**
   * Nghiệp vụ: Lấy danh sách toàn bộ User (Chỉ dành cho ADMIN)
   * @PreAuthorize: Kiểm tra quyền 'ADMIN' TRƯỚC khi cho phép thực thi hàm
   */
  @PreAuthorize("hasRole('ADMIN')")
  public List<UserResponse> getUser() {
    log.info("Admin is fetching all users");
    return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
  }

  /**
   * Nghiệp vụ: Lấy thông tin chi tiết một User
   * @PostAuthorize: Kiểm tra SAU KHI hàm chạy xong.
   * Đảm bảo: Chỉ ADMIN hoặc CHÍNH CHỦ tài khoản đó mới được xem thông tin này.
   */
  @PreAuthorize("hasRole('ADMIN') or returnObject.username == authentication.name")
  public UserResponse getUserById(String id) {
    log.info("Fetching user detail for id: {}", id);
    return userMapper.toUserResponse(
        userRepository
            .findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
  }

  /**
   * Nghiệp vụ: Xóa người dùng
   */
  @PreAuthorize("hasRole('ADMIN')")
  public void deleteUserById(String userId) {
    userRepository.deleteById(userId);
  }

  public String updateAvatar(String avatarUrl) {
    User user = getCurrentUser();
    user.setAvatarUrl(avatarUrl);
    userRepository.save(user);
    return avatarUrl;
  }
}
