/* (C)2026 */
package com.example.identity_servive.repository.Progress;

import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Language;
import com.example.identity_servive.entity.progress.Enrollment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {
  Optional<Enrollment> findByUserAndLanguage(User user, Language language);

  boolean existsByUserAndLanguage(User user, Language language);

  List<Enrollment> findAllByUser(User user);
}
