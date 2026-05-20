package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.community.CommentRequest;
import com.example.identity_servive.dto.response.community.CommentResponse;
import com.example.identity_servive.entity.community.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "post", ignore = true)
    Comment toComment(CommentRequest request);

    @Mapping(target = "replies", ignore = true)
    CommentResponse toCommentResponse(Comment comment);
}
