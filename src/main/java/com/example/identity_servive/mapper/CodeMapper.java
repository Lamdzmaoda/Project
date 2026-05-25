/* (C)2026 */
package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.ai.CodeRequest;
import com.example.identity_servive.dto.response.ai.CodeResponse;
import com.example.identity_servive.entity.System.Code;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CodeMapper {
  Code toCode(CodeRequest codeRequest);

  CodeResponse toCodeResponse(Code code);
}
