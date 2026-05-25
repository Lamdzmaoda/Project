/* (C)2026 */
package com.example.identity_servive.repository.Progress;

import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Chapter;
import com.example.identity_servive.entity.learning.Language;
import com.example.identity_servive.entity.progress.UserChapterProgress;
import com.example.identity_servive.enums.IsCompleted;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChapterProgressRepository extends JpaRepository<UserChapterProgress, String> {
  Optional<UserChapterProgress> findByUserAndChapter(User user, Chapter chapter);

  boolean existsByUserAndChapter(User user, Chapter chapter);

  int countByUserAndChapter_LanguageAndCompletedStatus(
      User user, Language language, IsCompleted status);

  void deleteAllByUserAndChapter_Language(User user, Language language);
}
