package com.example.identity_servive.repository;

import com.example.identity_servive.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StepRepository extends JpaRepository<Step, String> {
    Optional<Step> findById(String id);

}
