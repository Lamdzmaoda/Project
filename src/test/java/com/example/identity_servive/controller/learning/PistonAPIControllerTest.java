/* (C)2026 */
package com.example.identity_servive.controller.learning;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.identity_servive.dto.request.ai.CodeRequest;
import com.example.identity_servive.dto.response.ai.CodeResponse;
import com.example.identity_servive.enums.Status;
import com.example.identity_servive.service.system.Judge0APIService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Slf4j
@SpringBootTest
@TestPropertySource("/test.properties")
@AutoConfigureMockMvc
public class PistonAPIControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private Judge0APIService judge0APIService;

  @Test
  void runTest_success() throws Exception {
    CodeResponse response =
        CodeResponse.builder().status(Status.SUCCESS).output("Hello, World!").passed(true).build();

    Mockito.when(judge0APIService.executePythonCode(any(CodeRequest.class))).thenReturn(response);

    CodeRequest request = new CodeRequest();
    request.setInput("print('Hello, World!')");
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(request);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/code/run")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value(2000))
        .andExpect(jsonPath("result.output").value("Hello, World!"))
        .andExpect(jsonPath("result.passed").value(true));
  }
}
