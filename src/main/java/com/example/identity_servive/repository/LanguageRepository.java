package com.example.identity_servive.repository;

import com.example.identity_servive.entity.Language;
import com.example.identity_servive.entity.Lesson;
import com.example.identity_servive.enums.ContentStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {
    @EntityGraph(attributePaths = {"chapters"})
    Optional<Language> findByNameAndStatus(String name,ContentStatus status);
    @EntityGraph(attributePaths = {"chapters"})
    Optional<Language> findById(String id);
    boolean existsByName(String name);
}
