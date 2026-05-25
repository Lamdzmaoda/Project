/* (C)2026 */
package com.example.identity_servive.repository.community;

import com.example.identity_servive.entity.community.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
  List<Comment> findByPostIdOrderByCreatedAtDesc(String postId);

  int countByPostId(String postId);
}
