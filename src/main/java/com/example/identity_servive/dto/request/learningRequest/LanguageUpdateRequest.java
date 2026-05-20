package com.example.identity_servive.dto.request.learningRequest;


import com.example.identity_servive.enums.ContentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang
// Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LanguageUpdateRequest {
    String name;
    String description;
    String icon;
    String slug;           // Đường dẫn URL (VD: java -> java-slug)
    ContentStatus status;
    LocalDateTime durationDays;
    List<String> chapters;
}
