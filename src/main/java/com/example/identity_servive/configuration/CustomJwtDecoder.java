/* (C)2026 */
package com.example.identity_servive.configuration;

import com.example.identity_servive.dto.request.AuthRequest.IntrospectRequest;
import com.example.identity_servive.service.auth.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration // Đánh dấu đây là một Bean để Spring Security có thể sử dụng cho quá trình xác thực
public class CustomJwtDecoder implements JwtDecoder {

  @Value("${jwt.signerKey}") // Lấy giá trị khóa bí mật từ file cấu hình application.yaml/properties
  private String signerKey;

  @Autowired
  private AuthenticationService authenticationService; // Tiêm Service để gọi logic kiểm tra Token

  private NimbusJwtDecoder nimbusJwtDecoder =
      null; // Đối tượng dùng để giải mã JWT theo chuẩn Nimbus

  @Override
  public Jwt decode(String token) throws JwtException {

    try {
      if (token == null || token.isBlank()) {
        throw new JwtException("Token missing");
      }
      // 1. Gọi đến AuthenticationService để kiểm tra Token (Introspection)
      // Bước này giúp kiểm tra xem token đã bị Logout hoặc hết hạn chưa
      var response =
          authenticationService.introspect(IntrospectRequest.builder().token(token).build());

      // Nếu Service trả về kết quả không hợp lệ, ném ra lỗi xác thực ngay lập tức
      if (!response.isValid()) throw new JwtException("Token invalid");

    } catch (JOSEException | ParseException e) {
      // Xử lý các lỗi liên quan đến cấu trúc Token hoặc phân tích dữ liệu
      throw new JwtException(e.getMessage());
    }

    // 2. Khởi tạo NimbusJwtDecoder nếu nó đang bị null (Singleton-like pattern)
    if (Objects.isNull(nimbusJwtDecoder)) {
      // Tạo SecretKeySpec từ signerKey với thuật toán mã hóa HS512
      SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");

      // Xây dựng bộ giải mã với Key và thuật toán tương ứng
      nimbusJwtDecoder =
          NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }

    // 3. Thực hiện giải mã Token và trả về đối tượng Jwt chứa các thông tin (Claims) của người dùng
    return nimbusJwtDecoder.decode(token);
  }
}
