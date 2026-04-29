package com.example.identity_servive.repository;

import com.example.identity_servive.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStepProgressRepository extends JpaRepository<UserStepProgress, String> {
    Optional<UserStepProgress> findByUserAndStep(User user, Step step);
    int countByUserAndStep_LessonAndCompletedStatusTrue(User user, Lesson lesson);
    boolean existsByUserAndStep(User user, Step step);

    void deleteAllByStepId(String stepId);
}
