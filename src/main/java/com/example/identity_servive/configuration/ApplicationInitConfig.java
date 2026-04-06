/* (C)2026 */
package com.example.identity_servive.configuration;

import com.example.identity_servive.constant.PredefinedRole;
import com.example.identity_servive.entity.Role;
import com.example.identity_servive.entity.User;
import com.example.identity_servive.repository.RoleRepository;
import com.example.identity_servive.repository.UserRepository;
import java.util.HashSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Đánh dấu đây là lớp cấu hình, Spring sẽ quét và quản lý các Bean trong này
@RequiredArgsConstructor // Tự động tạo Constructor cho các biến final (Dependency Injection)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Tự động biến mọi field thành 'private final'
@Slf4j // Hỗ trợ ghi Log (in thông báo ra console) thay vì dùng System.out.println
public class ApplicationInitConfig {

    // Đối tượng dùng để mã hóa mật khẩu (ví dụ: BCrypt)
    PasswordEncoder passwordEncoder;

    // Khai báo tên đăng nhập mặc định cho Admin
    @NonFinal static final String ADMIN_USER_NAME = "admin";

    // Khai báo mật khẩu mặc định cho Admin
    @NonFinal static final String ADMIN_PASSWORD = "admin";

    @Bean // Đăng ký một Bean kiểu ApplicationRunner vào Spring Context
    ApplicationRunner applicationRunner(
            UserRepository userRepository, RoleRepository roleRepository) {

        log.info("Initializing application....."); // Ghi log thông báo bắt đầu khởi tạo

        return args -> {
            // Kiểm tra trong DB xem đã có user 'admin' chưa để tránh tạo trùng lặp
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {

                // 1. Tạo và lưu quyền USER vào bảng Role
                roleRepository.save(
                        Role.builder().name(PredefinedRole.USER_ROLE).description("User role").build());

                // 2. Tạo và lưu quyền ADMIN vào bảng Role
                Role adminRole =
                        roleRepository.save(
                                Role.builder().name(PredefinedRole.ADMIN_ROLE).description("Admin role").build());

                // Tạo một tập hợp (Set) chứa các quyền của Admin
                var roles = new HashSet<Role>();
                roles.add(adminRole);

                // 3. Khởi tạo đối tượng User Admin
                User user =
                        User.builder()
                                .username(ADMIN_USER_NAME) // Gán username = admin
                                .password(passwordEncoder.encode(ADMIN_PASSWORD)) // Mã hóa mật khẩu 'admin' trước khi gán
                                .roles(roles) // Gán danh sách quyền đã tạo ở trên
                                .build();
                // 4. Lưu User Admin xuống Database
                userRepository.save(user);

                // In cảnh báo nhắc nhở đổi mật khẩu vì mật khẩu 'admin' hiện đang là mặc định
                log.warn("admin user has been created with default password: admin, please change it");
            }

            log.info("Application initialization completed ....."); // Thông báo hoàn tất khởi tạo
        };
    }
}