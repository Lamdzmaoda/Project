
package com.example.identity_servive.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration // Đánh dấu lớp cấu hình
@EnableWebSecurity // Kích hoạt tính năng bảo mật Web của Spring Security
@EnableMethodSecurity // Cho phép dùng @PreAuthorize trên các hàm trong Controller/Service để phân quyền chi tiết

public class SecurityConfig {

    // Danh sách các API công khai, không cần đăng nhập cũng vào được (VD: Đăng ký, Đăng nhập)
    private final String[] PUBLIC_ENDPOINTS = {
            // ✅ THÊM /identity prefix cho tất cả!
            "/users", "/auth/token", "/auth/introspect", "/auth/logout", "/auth/refresh","/piston",     // Cho phép chính xác /piston
            "/piston/**",
            // 🔥 SWAGGER - KHÔNG CẦN prefix vì context-path tự handle
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/v3/api-docs/swagger-config",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/swagger-resources/configuration/ui",
            "/swagger-resources/configuration/security",
            "/webjars/**",
            "/webjars/springfox-swagger-ui/**"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // 1. Cấu hình CORS với các thiết lập mặc định (đã định nghĩa ở hàm corsFilter bên dưới)
        httpSecurity.cors(Customizer.withDefaults());

        // 2. Cấu hình phân quyền yêu cầu HTTP
        httpSecurity.authorizeHttpRequests(
                request ->
                        request
                                .requestMatchers(PUBLIC_ENDPOINTS ) // Cho phép gọi POST tới các API công khai
                                .permitAll() // Cho phép tất cả
                                .anyRequest() // Các yêu cầu khác (GET, PUT, DELETE hoặc endpoint khác)
                                .authenticated()); // Bắt buộc phải đăng nhập (có Token hợp lệ)

        // 3. Cấu hình ứng dụng đóng vai trò Resource Server (Xác thực qua JWT)
        httpSecurity.oauth2ResourceServer(
                oauth2 ->
                        oauth2
                                .jwt(
                                        jwtConfigurer ->
                                                jwtConfigurer
                                                        .decoder(customJwtDecoder()) // Sử dụng bộ giải mã CustomJwtDecoder của mình
                                                        .jwtAuthenticationConverter(jwtAuthenticationConverter())) // Chuyển đổi thông tin quyền hạn
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())); // Xử lý lỗi khi Token sai/thiếu

        // 4. Tắt CSRF vì ứng dụng này là REST API dùng Token, không dùng Session/Cookie nên không cần bảo vệ CSRF
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build(); // Xây dựng chuỗi lọc bảo mật
    }


    // Hàm tùy chỉnh cách đọc quyền từ Token JWT
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
                new JwtGrantedAuthoritiesConverter();
        // Xóa tiền tố "SCOPE_" mặc định của Spring để dùng trực tiếp tên Role (VD: ADMIN thay vì SCOPE_ADMIN)
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    // Cấu hình CORS để phía Frontend (React/Vue) có thể gọi API
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("http://localhost:3000"); // Cho phép domain này truy cập
        corsConfiguration.addAllowedMethod("*"); // Cho phép tất cả phương thức (GET, POST, PUT...)
        corsConfiguration.addAllowedHeader("*"); // Cho phép tất cả các Header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Áp dụng cấu hình cho toàn bộ API

        return new CorsFilter(source);
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(
                        PathPatternRequestMatcher.withDefaults().matcher("/v3/api-docs"),
                        PathPatternRequestMatcher.withDefaults().matcher("/v3/api-docs/**"),
                        PathPatternRequestMatcher.withDefaults().matcher("/swagger-ui/**"),
                        PathPatternRequestMatcher.withDefaults().matcher("/swagger-ui.html")
                );
    }
    // Khai báo Bean mã hóa mật khẩu
    @Bean
    PasswordEncoder passwordEncoder() {
        // Sử dụng thuật toán BCrypt với độ mạnh (strength) là 10
        return new BCryptPasswordEncoder(10);
    }
    @Bean
    public CustomJwtDecoder customJwtDecoder() {
        return new CustomJwtDecoder();
    }
}