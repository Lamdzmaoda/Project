package com.example.identity_servive.repository.community;

import com.example.identity_servive.entity.community.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, String> {
    Optional<Follow> findByFollowerIdAndFolloweeId(String followerId, String followeeId);
    List<Follow> findByFollowerId(String followerId);    // Đang follow ai
    List<Follow> findByFolloweeId(String followeeId);    // Ai đang follow mình
    int countByFollowerId(String followerId);             // Đếm đang follow
    int countByFolloweeId(String followeeId);             // Đếm follower
    boolean existsByFollowerIdAndFolloweeId(String followerId, String followeeId);
}
