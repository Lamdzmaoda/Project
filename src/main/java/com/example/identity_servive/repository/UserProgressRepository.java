package com.example.identity_servive.repository;

import com.example.identity_servive.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, String> {
    Optional<UserProgress> findByUserIDAndStepId(String userId, String stepId);

}
