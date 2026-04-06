package com.example.identity_servive.repository;


import com.example.identity_servive.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<Code,String> {
}
