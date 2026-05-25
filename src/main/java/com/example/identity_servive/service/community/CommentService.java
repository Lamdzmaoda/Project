/* (C)2026 */
package com.example.identity_servive.service.community;

import com.example.identity_servive.dto.request.community.CommentRequest;
import com.example.identity_servive.dto.response.community.CommentResponse;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.community.Comment;
import com.example.identity_servive.entity.community.Post;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.mapper.CommentMapper;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.community.CommentRepository;
import com.example.identity_servive.repository.community.PostRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {

  CommentRepository commentRepository;
  PostRepository postRepository;
  CommentMapper commentMapper;
  UserRepository userRepository;

  @Transactional
  public CommentResponse createComment(CommentRequest request, String userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    Post post =
        postRepository
            .findById(request.getPostId())
            .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

    Comment comment = commentMapper.toComment(request);
    comment.setUser(user);
    comment.setPost(post);

    if (request.getParentId() != null) {
      Comment parent =
          commentRepository
              .findById(request.getParentId())
              .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
      comment.setParent(parent);
    }

    comment = commentRepository.save(comment);

    post.setCommentCount(post.getCommentCount() + 1);
    postRepository.save(post);

    return buildCommentResponse(comment);
  }

  public List<CommentResponse> getCommentsByPost(String postId) {
    List<Comment> all = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
    List<CommentResponse> topLevel = new ArrayList<>();
    for (Comment c : all) {
      if (c.getParent() == null) {
        CommentResponse res = buildCommentResponse(c);
        res.setReplies(buildReplies(c.getId(), all));
        topLevel.add(res);
      }
    }
    return topLevel;
  }

  @Transactional
  public void deleteComment(String commentId, String userId) {
    Comment comment =
        commentRepository
            .findById(commentId)
            .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
    if (!comment.getUser().getId().equals(userId)) {
      throw new AppException(ErrorCode.UNAUTHORIZED_EXISTED);
    }
    commentRepository.delete(comment);

    Post post = comment.getPost();
    post.setCommentCount(Math.max(0, post.getCommentCount() - 1));
    postRepository.save(post);
  }

  private List<CommentResponse> buildReplies(String parentId, List<Comment> all) {
    return all.stream()
        .filter(c -> c.getParent() != null && c.getParent().getId().equals(parentId))
        .map(this::buildCommentResponse)
        .toList();
  }

  private CommentResponse buildCommentResponse(Comment comment) {
    CommentResponse res = commentMapper.toCommentResponse(comment);
    res.setUserId(comment.getUser().getId());
    res.setUsername(comment.getUser().getUsername());
    res.setUserAvatar(comment.getUser().getAvatarUrl());
    res.setPostId(comment.getPost().getId());
    if (comment.getParent() != null) {
      res.setParentId(comment.getParent().getId());
    }
    return res;
  }
}
