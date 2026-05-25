/* (C)2026 */
package com.example.identity_servive.repository.community;

import com.example.identity_servive.entity.community.PostLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, String> {
  Optional<PostLike> findByPostIdAndUserId(String postId, String userId);

  int countByPostId(String postId);

  boolean existsByPostIdAndUserId(String postId, String userId);
}
