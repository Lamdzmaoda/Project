/* (C)2026 */
package com.example.identity_servive.repository.learning;

import com.example.identity_servive.entity.learning.Step;
import com.example.identity_servive.enums.ContentStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends JpaRepository<Step, String> {
  Optional<Step> findByIdAndStatus(String id, ContentStatus status);

  Optional<Step> findFirstByLessonIdAndStatusOrderByOrderIndexAsc(
      String lessonId, ContentStatus status);

  // Sửa đổi các phương thức tìm kiếm theo Lesson để chỉ lấy Step ACTIVE
  List<Step> findAllByLessonIdAndStatusOrderByOrderIndexAsc(String lessonId, ContentStatus status);

  // Thêm phương thức để tìm tất cả Step ACTIVE
  List<Step> findAllByStatus(ContentStatus status);

  List<Step> findAllByLessonIdOrderByOrderIndexAsc(String lessonId);
}
