package com.example.identity_servive.repository;

import com.example.identity_servive.entity.Chapter;
import com.example.identity_servive.entity.Language;
import com.example.identity_servive.entity.Lesson;
import com.example.identity_servive.enums.ContentStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {
    @EntityGraph(attributePaths = {"lessons"})
    Optional<Chapter> findByIdAndStatus(String id, ContentStatus status);
    Optional<Chapter> findFirstByLanguageAndStatusOrderByOrderIndexAsc(Language language, ContentStatus status);
    Optional<Chapter> findFirstByLanguageAndStatusAndOrderIndexGreaterThanOrderByOrderIndexAsc(Language Language,ContentStatus status, int orderIndex);

    // Sửa đổi các phương thức tìm kiếm theo Lesson để chỉ lấy Step ACTIVE
    @EntityGraph(attributePaths = {"lessons"})
    List<Chapter> findAllByLanguageNameAndStatusOrderByOrderIndexAsc(String languageName, ContentStatus status);

    // Thêm phương thức để tìm tất cả Step ACTIVE
    List<Chapter> findAllByStatus(ContentStatus status);
    List<Chapter> findAllByLanguageNameOrderByOrderIndexAsc(String languageName);
}
