/* (C)2026 */
package com.example.identity_servive.repository.community;

import com.example.identity_servive.entity.community.SavedPost;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, String> {
  List<SavedPost> findByUserIdOrderBySavedAtDesc(String userId);

  Optional<SavedPost> findByUserIdAndPostId(String userId, String postId);

  boolean existsByUserIdAndPostId(String userId, String postId);
}
