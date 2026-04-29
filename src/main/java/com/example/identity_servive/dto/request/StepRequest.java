package com.example.identity_servive.dto.request;

import com.example.identity_servive.enums.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang
// Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StepRequest {
    String lessonId;
    String title;
    Type type;
    Mode mode;
    int orderIndex;
    IsCompleted completedStatus;
    IsLocked lockedStatus;
    double xp;
    boolean requiredToUnlockNext;
    Object data;
}
