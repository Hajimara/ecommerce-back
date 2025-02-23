package com.shop.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.constant.enums.ApiStatus;
import com.shop.constant.enums.ErrorCode;
import com.shop.dto.common.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ApiResult apiResult = new ApiResult(ApiStatus.UNAUTHORIZED);
        apiResult.setErrorCode(ErrorCode.AUTHENTICATION_FAILED.getCode());
        apiResult.setErrorMessage(ErrorCode.AUTHENTICATION_FAILED.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(apiResult);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }

}
