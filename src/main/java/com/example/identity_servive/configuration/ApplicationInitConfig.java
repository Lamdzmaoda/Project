package com.example.identity_servive.configuration;

import com.example.identity_servive.entity.User; // Đảm bảo import Entity của bạn, KHÔNG PHẢI của Security
import com.example.identity_servive.enums.Role;
import com.example.identity_servive.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    // Thêm final để Lombok tạo Constructor injection
    PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver"
    )
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUserName("admin").isEmpty()) {
                // Giả sử Entity User của bạn có field roles là Set<String> hoặc Set<Role>
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());

                // Sử dụng Builder của Entity User bạn tự tạo
                User user = User.builder()
                        .userName("admin") // Kiểm tra lại tên field là userName hay username
                        .password(passwordEncoder.encode("admin"))
                        //.roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("Admin has been created");
            }
        };
    }
}