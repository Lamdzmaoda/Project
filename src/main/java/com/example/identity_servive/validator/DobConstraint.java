/* (C)2026 */
package com.example.identity_servive.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

// @Target: Xác định Annotation này chỉ được đặt trên các trường (FIELD) của Class
@Target({ElementType.FIELD})
// @Retention: Xác định Annotation sẽ tồn tại cho đến lúc chương trình chạy (RUNTIME) để thực hiện kiểm tra
@Retention(RetentionPolicy.RUNTIME)
// @Constraint: Chỉ định Class sẽ thực hiện logic kiểm tra cho Annotation này (DobValidator)
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {

    // Thông báo lỗi mặc định nếu dữ liệu không hợp lệ
    String message() default "{Invalid date of birth}";

    // Thuộc tính tùy chỉnh: Độ tuổi tối thiểu (Ví dụ: 18)
    int min();

    // Các nhóm ràng buộc (thường để mặc định)
    Class<?>[] groups() default {};

    // Payload dùng để gắn các thông tin bổ trợ khi có lỗi (thường để mặc định)
    Class<? extends Payload>[] payload() default {};
}