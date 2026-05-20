/* (C)2026 */
package com.example.identity_servive.controller.auth;

import com.example.identity_servive.dto.request.AuthRequest.AuthenticationRequest;
import com.example.identity_servive.dto.request.AuthRequest.IntrospectRequest;
import com.example.identity_servive.dto.request.AuthRequest.LogoutRequest;
import com.example.identity_servive.dto.request.AuthRequest.RefreshRequest;
import com.example.identity_servive.dto.response.ApiResponse;
import com.example.identity_servive.dto.response.authResponse.AuthenticationResponse;
import com.example.identity_servive.dto.response.authResponse.IntrospectResponse;
import com.example.identity_servive.service.auth.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

/** Controller xử lý các yêu cầu liên quan đến xác thực (đăng nhập, logout, verify token). */
@RestController // Đánh dấu class này là một REST Controller, chuyên trả về dữ liệu JSON
@RequestMapping("/auth") // Tất cả các API trong class này sẽ bắt đầu bằng đường dẫn /auth
@RequiredArgsConstructor // Tự động tạo Constructor để Spring tiêm (Inject) các Service vào
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Tự động biến các biến khai báo thành 'private final'
public class AuthenticationController {

    // Tiêm AuthenticationService để xử lý các logic nghiệp vụ xác thực phức tạp bên dưới
    AuthenticationService authenticationService;

    /**
     * API Đăng nhập và lấy Token
     * URL: POST /auth/token
     */
    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        // 1. Gọi Service để kiểm tra user/pass. Nếu đúng, Service trả về Token JWT.
        var result = authenticationService.authenticate(request);

        // 2. Trả về kết quả kèm mã code thành công (2000) và dữ liệu Token
        return ApiResponse.<AuthenticationResponse>builder().code(2000).result(result).build();
    }

    /**
     * API Kiểm tra Token còn hiệu lực hay không
     * URL: POST /auth/introspect
     */
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        // 1. Gọi Service để kiểm tra Token gửi lên có hợp lệ và còn hạn không
        var result = authenticationService.introspect(request);

        // 2. Trả về kết quả kiểm tra (true/false) kèm mã code (2001)
        return ApiResponse.<IntrospectResponse>builder().code(2000).result(result).build();
    }

    /**
     * API Làm mới Token (Dùng Refresh Token để lấy Access Token mới)
     * URL: POST /auth/refresh
     */
    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        // 1. Gọi Service để cấp lại Token mới cho người dùng mà không cần bắt họ đăng nhập lại
        var result = authenticationService.refreshToken(request);

        // 2. Trả về Token mới cho Client
        return ApiResponse.<AuthenticationResponse>builder().code(2000).result(result).build();
    }

    /**
     * API Đăng xuất (Vô hiệu hóa Token hiện tại)
     * URL: POST /auth/logout
     */
    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        // 1. Gọi Service để đưa Token hiện tại vào danh sách bị vô hiệu hóa (Blacklist)
        authenticationService.logout(request);

        // 2. Trả về thông báo thành công (trống phần result vì không cần trả dữ liệu)
        return ApiResponse.<Void>builder().build();
    }
    @PostMapping("/outbound/authentication")
    ApiResponse<AuthenticationResponse> outboundAuthentication(@RequestParam("code") String code){
        var result = authenticationService.outboundAuthenticate(code);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();

    }

}