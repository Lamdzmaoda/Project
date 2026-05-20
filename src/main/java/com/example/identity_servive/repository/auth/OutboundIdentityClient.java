package com.example.identity_servive.repository.auth;

import com.example.identity_servive.dto.request.AuthRequest.ExchangeTokenRequest;
import com.example.identity_servive.dto.response.authResponse.ExchangeTokenResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "oubound-identity" ,url ="https://oauth2.googleapis.com")
@EnableFeignClients
public interface OutboundIdentityClient {
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenRequest request);
}
