package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.community.PostRequest;
import com.example.identity_servive.dto.response.community.PostResponse;
import com.example.identity_servive.entity.community.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toPost(PostRequest request);

    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "likedByMe", ignore = true)
    @Mapping(target = "savedByMe", ignore = true)
    PostResponse toPostResponse(Post post);
}
