/* (C)2026 */
package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.AuthRequest.UserCreationRequest;
import com.example.identity_servive.dto.request.AuthRequest.UserUpdateRequest;
import com.example.identity_servive.dto.response.authResponse.UserResponse;
import com.example.identity_servive.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Interface giúp chuyển đổi dữ liệu tự động giữa Entity User và các DTO. @Mapper(componentModel =
 * "spring"): MapStruct sẽ tự sinh ra class Implementation và đăng ký nó như một Bean trong Spring
 * Context để bạn có thể @Autowired.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

  /**
   * Chuyển đổi từ dữ liệu đăng ký (DTO) sang thực thể (Entity) để lưu vào Database.
   *
   * @param request Chứa username, password, firstName...
   * @return Đối tượng User mới với các trường tương ứng đã được ánh xạ.
   */
  User toUser(UserCreationRequest request);

  /**
   * Chuyển đổi từ thực thể (Entity) sang dữ liệu phản hồi (DTO) gửi về cho Client. @Mapping(target
   * = "lastName", ignore = true): Chỉ định MapStruct bỏ qua trường lastName. Khi trả về Client,
   * trường lastName trong UserResponse sẽ luôn là null.
   */
  UserResponse toUserResponse(User user);

  /**
   * Cập nhật thông tin người dùng hiện có từ dữ liệu yêu cầu (DTO).
   *
   * @param request Chứa các thông tin mới cần cập nhật.
   * @param user Đối tượng User gốc lấy từ DB. @MappingTarget: Đánh dấu đây là đối tượng đích sẽ
   *     được ghi đè dữ liệu lên.
   */
  @Mapping(target = "roles", ignore = true)
  void updateUser(UserUpdateRequest request, @MappingTarget User user);
}
