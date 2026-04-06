package com.example.identity_servive.dto.request;

import com.example.identity_servive.entity.Chapter;
import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
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
public class LessonRequest {
    String chapterId;
    String title;
    int orderIndex;
    double xp;
    double progress;
    IsLocked lockedStatus;
    IsCompleted completedStatus;
    String contentMarkdown;
    List<String> steps;
}
