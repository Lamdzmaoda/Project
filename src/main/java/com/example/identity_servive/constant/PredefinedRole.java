/* (C)2026 */
package com.example.identity_servive.constant;

// Class định nghĩa các hằng số về quyền hạn người dùng trong hệ thống
public class PredefinedRole {

  // Hằng số cho quyền người dùng bình thường (USER)
  public static final String USER_ROLE = "USER";

  // Hằng số cho quyền quản trị viên (ADMIN)
  public static final String ADMIN_ROLE = "ADMIN";

  // Constructor được để ở private để ngăn không cho tạo đối tượng từ class này
  // Vì đây là class tiện ích chỉ chứa hằng số (Static constants only)
  private PredefinedRole() {}
}
