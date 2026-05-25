/* (C)2026 */
package com.example.identity_servive.enums;

public enum ContentStatus {
  ACTIVE, // Nội dung đang hoạt động và hiển thị cho người dùng
  HIDDEN, // Nội dung bị ẩn (ví dụ: Admin ẩn tạm thời)
  DELETED // Nội dung đã bị "xóa mềm" (không hiển thị, có thể khôi phục)
}
