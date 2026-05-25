/* (C)2026 */
package com.example.identity_servive.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Báo Spring đây là class cấu hình
public class CloudinaryConfig {
  // @Value đọc giá trị từ application.yaml hoặc biến môi trường
  // "${cloudinary.cloud-name}" nghĩa là: tìm key "cloudinary.cloud-name" trong config
  @Value("${cloudinary.cloud-name}")
  private String cloudName;

  @Value("${cloudinary.api-key}")
  private String apiKey;

  @Value("${cloudinary.api-secret}")
  private String apiSecret;

  @Bean
  public Cloudinary cloudinary() {
    String url = "cloudinary://" + apiKey + ":" + apiSecret + "@" + cloudName;
    return new Cloudinary(url);
  }
}
