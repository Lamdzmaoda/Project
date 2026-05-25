/* (C)2026 */
package com.example.identity_servive.service.auth;

import com.example.identity_servive.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {
  @Autowired private UserRepository userRepository;

  public boolean isOwner(String id) {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    return userRepository
        .findById(id)
        .map(u -> u.getUsername().equals(auth.getName()))
        .orElse(false);
  }
}
