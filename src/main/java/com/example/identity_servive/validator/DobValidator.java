/* (C)2026 */
package com.example.identity_servive.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

// Class này thực thi interface ConstraintValidator để xử lý logic cho @DobConstraint
// Nó nhận vào kiểu dữ liệu là LocalDate (ngày sinh của user)
public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

  private int min; // Biến lưu trữ độ tuổi tối thiểu

  /**
   * Bước 1: Khởi tạo Validator
   * Lấy giá trị 'min' từ Annotation (ví dụ: @DobConstraint(min = 18))
   */
  @Override
  public void initialize(DobConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    // Gán giá trị min từ Annotation vào biến local để sử dụng ở hàm isValid
    this.min = constraintAnnotation.min();
  }

  /**
   * Bước 2: Kiểm tra tính hợp lệ (Logic chính)
   * @param localDate Giá trị ngày sinh mà người dùng gửi lên
   */
  @Override
  public boolean isValid(
      LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {

    // Nếu ngày sinh trống, mặc định là hợp lệ (để @NotNull hoặc các rule khác xử lý)
    if (Objects.isNull(localDate)) return true;

    // 1. Tính toán số năm (tuổi) giữa ngày sinh và ngày hiện tại (LocalDate.now())
    long years = ChronoUnit.YEARS.between(localDate, LocalDate.now());

    // 2. So sánh: Nếu tuổi >= tuổi tối thiểu quy định thì trả về true, ngược lại false
    return years >= min;
  }
}
