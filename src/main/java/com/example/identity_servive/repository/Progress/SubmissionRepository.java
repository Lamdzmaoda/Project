/* (C)2026 */
package com.example.identity_servive.repository.Progress;

import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Language;
import com.example.identity_servive.entity.learning.Problem;
import com.example.identity_servive.entity.progress.Submission;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {
  Optional<Submission> findFirstByUserAndProblemOrderByCreatedAtDesc(User user, Problem problem);

  void deleteAllByUserAndLesson_Chapter_Language(User user, Language language);
}
