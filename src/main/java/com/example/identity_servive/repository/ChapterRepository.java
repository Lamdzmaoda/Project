package com.example.identity_servive.repository;

import com.example.identity_servive.entity.Chapter;
import com.example.identity_servive.entity.Language;
import com.example.identity_servive.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {
    Optional<Chapter> findById(String id);
    Optional<Chapter> findFirstByLanguageOrderByOrderIndexAsc(Language language);
    Optional<Chapter> findFirstByLanguageAndOrderIndexGreaterThanOrderByOrderIndexAsc(Language language, int orderIndex);
    List<Chapter> findByLanguageName(String languageName);
}
