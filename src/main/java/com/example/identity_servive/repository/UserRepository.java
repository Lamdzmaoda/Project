package com.example.identity_servive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.identity_servive.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface cung cấp các phương thức thao tác với bảng 'user' trong Database.
 * Kế thừa JpaRepository giúp bạn có sẵn các hàm CRUD (save, delete, findById, findAll...)
 * mà không cần viết code triển khai.
 */
@Repository // Đánh dấu đây là một Repository Bean của Spring
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Kiểm tra xem tên đăng nhập đã tồn tại trong hệ thống chưa.
     * @param userName Tên đăng nhập cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     * (Cực kỳ hữu ích trong logic Đăng ký người dùng)
     */
    boolean existsByUserName(String userName);

    /**
     * Tìm kiếm người dùng dựa trên tên đăng nhập.
     * @param userName Tên đăng nhập
     * @return Một Optional chứa User (nếu tìm thấy) hoặc rỗng (nếu không thấy).
     * (Sử dụng Optional giúp tránh lỗi NullPointerException khi xử lý logic)
     */
    Optional<User> findByUserName(String userName);
}