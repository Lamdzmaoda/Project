package com.example.identity_servive.repository;

import com.example.identity_servive.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLessonProgressRepository extends JpaRepository<UserLessonProgress, String> {
    Optional<UserLessonProgress> findByUserAndLesson(User user, Lesson lesson);

    boolean existsByUserAndLesson(User user, Lesson lesson);

    void deleteAllByLessonId(String lessonId);
}
