/* (C)2026 */
package com.example.identity_servive.repository.Progress;

import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Chapter;
import com.example.identity_servive.entity.learning.Language;
import com.example.identity_servive.entity.learning.Lesson;
import com.example.identity_servive.entity.progress.UserLessonProgress;
import com.example.identity_servive.enums.IsCompleted;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLessonProgressRepository extends JpaRepository<UserLessonProgress, String> {
  Optional<UserLessonProgress> findByUserAndLesson(User user, Lesson lesson);

  boolean existsByUserAndLesson(User user, Lesson lesson);

  // UserLessonProgressRepository.java
  int countByUserAndLesson_ChapterAndCompletedStatus(
      User user, Chapter chapter, IsCompleted completedStatus);

  void deleteAllByLessonId(String lessonId);

  int countByUserAndLesson_Chapter_LanguageAndCompletedStatus(
      User user, Language language, IsCompleted status);

  void deleteAllByUserAndLesson_Chapter_Language(User user, Language language);
}
