package com.example.identity_servive.dto.request.learningRequest;

import com.example.identity_servive.enums.ContentStatus;
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
public class ChapterUpdateRequest {
    String languageName;
    String title;
    int orderIndex;
    ContentStatus status;
    String slug;           // Đường dẫn URL
    String description;  // Mô tả chương
    List<String> lessons;
}
