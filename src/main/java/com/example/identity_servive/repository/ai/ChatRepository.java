package com.example.identity_servive.repository.ai;

import com.example.identity_servive.entity.AI.OpenAI;
import com.example.identity_servive.entity.learning.Step;
import com.example.identity_servive.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<OpenAI, String> {
    Optional<OpenAI> findFirstByUserAndStepOrderByCreateAtDesc(User user, Step step);

    List<OpenAI> findByUser(User user);
}