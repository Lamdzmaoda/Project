package com.example.identity_servive.repository;

import com.example.identity_servive.entity.Chapter;
import com.example.identity_servive.entity.Lesson;
import com.example.identity_servive.entity.Step;
import com.example.identity_servive.entity.User;
import com.example.identity_servive.enums.ContentStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, String> {
    @EntityGraph(attributePaths = {"steps"})
    Optional<Lesson> findByIdAndStatus(String id, ContentStatus status);
    Optional<Lesson> findFirstByChapterAndStatusOrderByOrderIndexAsc(Chapter chapter, ContentStatus status);
    Optional<Lesson> findFirstByChapterAndStatusAndOrderIndexGreaterThanOrderByOrderIndexAsc(Chapter chapterId,ContentStatus status, int orderIndex);
    // Sửa đổi các phương thức tìm kiếm theo Lesson để chỉ lấy Step ACTIVE
    List<Lesson> findAllByChapterIdAndStatusOrderByOrderIndexAsc(String chapterId, ContentStatus status);
    // Thêm phương thức để tìm tất cả Step ACTIVE
    List<Lesson> findAllByStatus(ContentStatus status);
    List<Lesson> findAllByChapterIdOrderByOrderIndexAsc(String chapterId);
}
