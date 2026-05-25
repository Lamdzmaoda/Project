package com.example.democode3.features.community.api.response;

import com.example.democode3.features.community.model.Post;

import java.util.List;

public class PostResponse {

    public int code;

    public String message;

    public Result result;

    public static class Result {

        public List<Post> content;
    }
}