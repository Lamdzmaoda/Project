package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.CodeRequest;
import com.example.identity_servive.dto.response.CodeResponse;
import com.example.identity_servive.entity.Code;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CodeMapper {
    Code toCode(CodeRequest codeRequest);
    CodeResponse toCodeResponse(Code code);
}
