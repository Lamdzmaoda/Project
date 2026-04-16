package com.example.identity_servive.repository;

import com.example.identity_servive.entity.Enrollment;
import com.example.identity_servive.entity.Language;
import com.example.identity_servive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {
    Optional<Enrollment> findByUserAndLanguage(User user, Language language);
    boolean existsByUserAndLanguage(User user, Language language);
    List<Enrollment> findAllByUser(User user);
}
