/* (C)2026 */
package com.example.identity_servive.repository.auth;

import com.example.identity_servive.entity.auth.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
