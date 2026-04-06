package com.example.identity_servive.dto.request;

import com.example.identity_servive.enums.IsLocked;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang
// Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChapterRequest {
    String languageName;
    String title;
    int orderIndex;
    IsLocked lockedStatus;
    List<String> lessons;
}
