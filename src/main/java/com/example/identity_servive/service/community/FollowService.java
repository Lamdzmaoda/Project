package com.example.identity_servive.service.community;

import com.example.identity_servive.dto.response.community.FollowResponse;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.community.Follow;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.community.FollowRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FollowService {

    FollowRepository followRepository;
    UserRepository userRepository;

    @Transactional
    public FollowResponse follow(String followeeId, String followerId) {
        if (followerId.equals(followeeId)) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            throw new AppException(ErrorCode.ALREADY_EXISTED);
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .followee(followee)
                .build();
        follow = followRepository.save(follow);

        return buildFollowResponse(follow);
    }

    @Transactional
    public void unfollow(String followeeId, String followerId) {
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        followRepository.delete(follow);
    }

    public List<FollowResponse> getFollowing(String userId) {
        return followRepository.findByFollowerId(userId).stream()
                .map(this::buildFollowResponse)
                .toList();
    }

    public List<FollowResponse> getFollowers(String userId) {
        return followRepository.findByFolloweeId(userId).stream()
                .map(this::buildFollowResponse)
                .toList();
    }

    public int getFollowingCount(String userId) {
        return followRepository.countByFollowerId(userId);
    }

    public int getFollowerCount(String userId) {
        return followRepository.countByFolloweeId(userId);
    }

    public boolean isFollowing(String followerId, String followeeId) {
        return followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    private FollowResponse buildFollowResponse(Follow follow) {
        return FollowResponse.builder()
                .id(follow.getId())
                .followerId(follow.getFollower().getId())
                .followerName(follow.getFollower().getUsername())
                .followerAvatar(follow.getFollower().getAvatarUrl())
                .followeeId(follow.getFollowee().getId())
                .followeeName(follow.getFollowee().getUsername())
                .followeeAvatar(follow.getFollowee().getAvatarUrl())
                .createdAt(follow.getCreatedAt())
                .build();
    }
}
