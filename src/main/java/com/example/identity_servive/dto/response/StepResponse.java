package com.example.identity_servive.dto.response;

import com.example.identity_servive.enums.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang
// Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StepResponse {
    String id;
    String title;
    Type type;
    Mode mode;
    int orderIndex;
    ContentStatus status;
    IsLocked lockedStatus;
    IsCompleted completedStatus;
    double xp;
    boolean requiredToUnlockNext;
    LocalDateTime createAt;
    Object data;

}
