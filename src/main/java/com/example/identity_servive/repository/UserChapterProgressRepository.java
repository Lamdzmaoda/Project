package com.example.identity_servive.repository;

import com.example.identity_servive.entity.Chapter;
import com.example.identity_servive.entity.Lesson;
import com.example.identity_servive.entity.User;
import com.example.identity_servive.entity.UserChapterProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserChapterProgressRepository extends JpaRepository<UserChapterProgress, String> {
    Optional<UserChapterProgress> findByUserAndChapter(User user, Chapter chapter);

    boolean existsByUserAndChapter(User user, Chapter chapter);
}
