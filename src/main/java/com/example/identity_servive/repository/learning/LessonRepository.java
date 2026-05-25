/* (C)2026 */
package com.example.identity_servive.repository.learning;

import com.example.identity_servive.entity.learning.Chapter;
import com.example.identity_servive.entity.learning.Language;
import com.example.identity_servive.entity.learning.Lesson;
import com.example.identity_servive.enums.ContentStatus;
import com.example.identity_servive.enums.LessonType;
import feign.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, String> {
  @EntityGraph(attributePaths = {"steps"})
  Optional<Lesson> findByIdAndStatus(String id, ContentStatus status);

  Optional<Lesson> findFirstByChapterAndStatusOrderByOrderIndexAsc(
      Chapter chapter, ContentStatus status);

  Optional<Lesson> findFirstByChapterAndStatusAndOrderIndexGreaterThanOrderByOrderIndexAsc(
      Chapter chapterId, ContentStatus status, int orderIndex);

  // Sửa đổi các phương thức tìm kiếm theo Lesson để chỉ lấy Step ACTIVE
  List<Lesson> findAllByChapterIdAndStatusOrderByOrderIndexAsc(
      String chapterId, ContentStatus status);

  List<Lesson> findAllByStatus(ContentStatus status);

  List<Lesson> findAllByChapterIdOrderByOrderIndexAsc(String chapterId);

  int countByChapterLanguageAndStatus(Language language, ContentStatus status);

  int countByChapterIdAndStatus(String chapterId, ContentStatus status);

  Optional<Lesson> findByIdAndLessonType(String lessonId, LessonType lessonType);

  @Query(
      "SELECT COALESCE(SUM(l.xp), 0) FROM Lesson l WHERE l.chapter.id = :chapterId AND l.status ="
          + " :status")
  long sumXpByChapterId(
      @Param("chapterId") String chapterId, @Param("status") ContentStatus status);

  @Query(
      "SELECT COALESCE(SUM(l.xp), 0) FROM Lesson l "
          + "JOIN l.chapter c JOIN c.language lang "
          + "WHERE lang.name = :languageName AND l.status = :status")
  long sumXpByLanguageName(
      @Param("languageName") String languageName, @Param("status") ContentStatus status);
}
