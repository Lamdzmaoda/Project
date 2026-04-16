package com.example.identity_servive.repository;

import com.example.identity_servive.entity.Chapter;
import com.example.identity_servive.entity.Lesson;
import com.example.identity_servive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, String> {
    Optional<Lesson> findById(String id);
    Optional<Lesson> findFirstByChapterOrderByOrderIndexAsc(Chapter chapter);
    Optional<Lesson> findFirstByChapterAndOrderIndexGreaterThanOrderByOrderIndexAsc(Chapter chapter, int orderIndex);
    List<Lesson> findByChapterId(String chapterId);

}
