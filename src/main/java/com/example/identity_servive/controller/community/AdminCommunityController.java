package com.example.identity_servive.controller.community;

import com.example.identity_servive.dto.response.ApiResponse;
import com.example.identity_servive.repository.community.CommentRepository;
import com.example.identity_servive.repository.community.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/community")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCommunityController {

    PostRepository postRepository;
    CommentRepository commentRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/posts/{postId}")
    ApiResponse<Void> adminDeletePost(@PathVariable String postId) {
        postRepository.deleteById(postId);
        return ApiResponse.<Void>builder().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/comments/{commentId}")
    ApiResponse<Void> adminDeleteComment(@PathVariable String commentId) {
        commentRepository.deleteById(commentId);
        return ApiResponse.<Void>builder().build();
    }
}
