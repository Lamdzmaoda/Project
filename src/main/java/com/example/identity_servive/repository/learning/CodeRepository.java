/* (C)2026 */
package com.example.identity_servive.repository.learning;

import com.example.identity_servive.entity.System.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends JpaRepository<Code, String> {}
