package com.example.identity_servive.dto.response;

import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
import com.example.identity_servive.enums.Mode;
import com.example.identity_servive.enums.Type;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@Builder // Hỗ trợ khởi tạo đối tượng nhanh theo pattern Builder (tiện cho việc viết Unit Test)
@NoArgsConstructor // Tạo constructor không tham số (Bắt buộc để Jackson có thể chuyển đổi JSON sang
// Object này)
@AllArgsConstructor // Tạo constructor chứa tất cả các tham số
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyResponse {
    boolean isCorrect;
    double earnedXp;
    String message;
    Object correctAnswer;


}
