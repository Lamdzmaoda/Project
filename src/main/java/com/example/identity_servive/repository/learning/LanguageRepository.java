/* (C)2026 */
package com.example.identity_servive.repository.learning;

import com.example.identity_servive.entity.learning.Language;
import com.example.identity_servive.enums.ContentStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {
  @EntityGraph(attributePaths = {"chapters"})
  Optional<Language> findByNameAndStatus(String name, ContentStatus status);

  @EntityGraph(attributePaths = {"chapters"})
  Optional<Language> findById(String id);

  boolean existsByName(String name);
}
