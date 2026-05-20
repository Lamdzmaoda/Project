package com.example.identity_servive.repository.learning;

import com.example.identity_servive.entity.learning.Chapter;
import com.example.identity_servive.entity.learning.Problem;
import com.example.identity_servive.enums.ContentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, String> {
    List<Problem> findByLessonId(String lessonId);
    Optional<Problem> findByIdAndStatus(String problemId, ContentStatus status);
    List<Problem> findAllByLessonIdAndStatusOrderByOrderIndexAsc(String lessonId, ContentStatus status);
    List<Problem> findAllByLessonIdOrderByOrderIndexAsc(String lessonId);
}
