/* (C)2026 */
package com.example.identity_servive.service;

import com.example.identity_servive.dto.request.AuthenticationRequest;
import com.example.identity_servive.dto.request.IntrospectRequest;
import com.example.identity_servive.dto.request.LogoutRequest;
import com.example.identity_servive.dto.request.RefreshRequest;
import com.example.identity_servive.dto.response.AuthenticationResponse;
import com.example.identity_servive.dto.response.IntrospectResponse;
import com.example.identity_servive.entity.InvalidatedToken;
import com.example.identity_servive.entity.User;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.repository.InvalidatedTokenRepository;
import com.example.identity_servive.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository; // Repository quản lý Token đã bị vô hiệu hóa

    @NonFinal
    @Value("${jwt.signerKey}") // Khóa bí mật dùng để ký tên lên Token
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}") // Thời gian sống của Access Token
    protected long VALIDATION_DURATION;

    @NonFinal
    @Value("${jwt.valid-duration}") // Thời gian tối đa có thể Refresh Token
    protected long REFRESHABLE_DURATION;

    /**
     * 1. Kiểm tra Token còn hiệu lực hay không (Introspect)
     */
    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        try {
            verifyToken(token, false); // Thử xác thực Token
            return IntrospectResponse.builder().valid(true).build();
        } catch (AppException e) {
            return IntrospectResponse.builder().valid(false).build();
        }
    }

    /**
     * 2. Xử lý Đăng nhập (Authenticate)
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Tìm user, nếu không có ném lỗi 404 nghiệp vụ
        var user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Dùng BCrypt để so khớp mật khẩu thuần và mật khẩu đã băm trong DB
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        // Nếu đúng pass, tiến hành tạo Token mới trả về cho Client
        var token = generateToken(user);
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    /**
     * 3. Xử lý Đăng xuất (Logout)
     */
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            // Xác thực Token trước khi cho phép logout
            var signToken = verifyToken(request.getToken(), false);

            // Lấy ID duy nhất (JTI) và thời gian hết hạn của Token
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            // Lưu Token này vào "Danh sách đen" (Database) để nó không bao giờ được dùng lại
            InvalidatedToken invalidatedToken = InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException e) {
            log.info("Token already expired or invalid");
        }
    }

    /**
     * 4. Hàm bổ trợ: Xác thực và kiểm tra tính toàn vẹn của Token
     */
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Tính toán thời gian hết hạn tùy thuộc vào việc đây là kiểm tra để dùng hay để Refresh
        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier); // Kiểm tra chữ ký có đúng với SIGNER_KEY không

        // Nếu chữ ký sai hoặc Token đã hết hạn, ném lỗi 401
        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHORIZED);

        // Kiểm tra xem Token này đã nằm trong bảng "Đã đăng xuất" chưa
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHORIZED);

        return signedJWT;
    }

    /**
     * 5. Hàm bổ trợ: Tạo chuỗi JWT Token
     */
    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512); // Sử dụng thuật toán ký HS512

        // Thiết lập các thông tin chứa trong Token (Payload)
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName()) // Chủ thể của token là username
                .issuer("lamdzbodoi.com") // Người phát hành
                .issueTime(new Date()) // Thời điểm tạo
                .expirationTime(new Date(Instant.now().plus(VALIDATION_DURATION, ChronoUnit.SECONDS).toEpochMilli())) // Thời điểm hết hạn
                .jwtID(UUID.randomUUID().toString()) // Cấp ID duy nhất cho mỗi Token (phục vụ logout)
                .claim("scope", buildScope(user)) // Gán quyền (Roles/Permissions) vào payload
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes())); // Thực hiện ký tên bằng khóa bí mật
            return jwsObject.serialize(); // Chuyển đối tượng JWT thành chuỗi String
        } catch (JOSEException e) {
            throw new RuntimeException("Cannot create Token", e);
        }
    }

    /**
     * 6. Làm mới Token (Refresh Token)
     */
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        // 1. Kiểm tra Token cũ (vẫn cho phép nếu nó vừa mới hết hạn Access nhưng còn hạn Refresh)
        var signJWT = verifyToken(request.getToken(), true);

        // 2. Vô hiệu hóa Token cũ ngay lập tức (không cho dùng lại để lấy thêm Token nữa)
        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
        invalidatedTokenRepository.save(InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build());

        // 3. Tạo một Token hoàn toàn mới cho người dùng
        var username = signJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUserName(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        return AuthenticationResponse.builder().token(generateToken(user)).authenticated(true).build();
    }

    /**
     * 7. Hàm bổ trợ: Gộp Role và Permission thành chuỗi Scope (Ví dụ: "ROLE_ADMIN CAN_DELETE")
     */
    private String buildScope(User user) {
        // 1. Tạo StringJoiner với delimiter là " " (khoảng trắng)
        StringJoiner stringJoiner = new StringJoiner(" ");

        // 2. Kiểm tra user có roles không
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            // Duyệt qua từng role
            user.getRoles().forEach(role -> {
                // 2.1. Thêm "ROLE_" + tên_role (ví dụ: "ROLE_ADMIN")
                stringJoiner.add("ROLE_" + role.getName());

                // 2.2. Nếu role có permissions thì thêm tất cả permissions
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission ->
                            stringJoiner.add(permission.getName())  // ví dụ: "read:user", "write:product"
                    );
            });
        }

        // 3. Trả về chuỗi hoàn chỉnh
        return stringJoiner.toString();
    }
}