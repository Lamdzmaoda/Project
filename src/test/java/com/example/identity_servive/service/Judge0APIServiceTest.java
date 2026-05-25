/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.identity_servive.dto.request.ai.CodeRequest;
import com.example.identity_servive.dto.response.ai.CodeResponse;
import com.example.identity_servive.entity.System.Code;
import com.example.identity_servive.enums.Status;
import com.example.identity_servive.mapper.CodeMapper;
import com.example.identity_servive.repository.learning.CodeRepository;
import com.example.identity_servive.service.system.Judge0APIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class Judge0APIServiceTest {

  @Autowired private Judge0APIService judge0APIService;
  @MockitoBean private CodeRepository codeRepository;
  @MockitoBean private CodeMapper codeMapper;

  private Code codeEntity;
  private CodeResponse codeResponse;

  @BeforeEach
  void initData() {
    codeEntity = new Code();
    codeEntity.setInput("cHJpbnQoImhlbGxvIik=");
    codeResponse = CodeResponse.builder().status(Status.ERROR).output("Lỗi hệ thống").build();
  }

  @Test
  void executePythonCode_error_whenNoServer() {
    when(codeMapper.toCode(any())).thenReturn(codeEntity);
    when(codeMapper.toCodeResponse(any())).thenReturn(codeResponse);

    CodeRequest request = new CodeRequest();
    request.setInput("print('hello')");

    var result = judge0APIService.executePythonCode(request);
    assertThat(result).isNotNull();
    assertThat(result.getStatus()).isEqualTo(Status.ERROR);
  }
}
