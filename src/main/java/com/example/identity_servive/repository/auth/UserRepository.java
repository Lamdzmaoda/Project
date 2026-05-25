/* (C)2026 */
package com.example.identity_servive.repository.auth;

import com.example.identity_servive.entity.auth.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface cung cấp các phương thức thao tác với bảng 'user' trong Database. Kế thừa JpaRepository
 * giúp bạn có sẵn các hàm CRUD (save, delete, findById, findAll...) mà không cần viết code triển
 * khai.
 */
@Repository // Đánh dấu đây là một Repository Bean của Spring
public interface UserRepository extends JpaRepository<User, String> {

  /**
   * Kiểm tra xem tên đăng nhập đã tồn tại trong hệ thống chưa.
   *
   * @param username Tên đăng nhập cần kiểm tra
   * @return true nếu đã tồn tại, false nếu chưa (Cực kỳ hữu ích trong logic Đăng ký người dùng)
   */
  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);
}
