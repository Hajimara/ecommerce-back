package com.shop.controller;

import com.shop.dto.auth.request.LoginDto;
import com.shop.dto.auth.response.TokenDto;
import com.shop.dto.common.ApiResult;
import com.shop.service.ProcessAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ControllerAuth {

    private final ProcessAuth processAuth;

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResult> authorization(@Valid @RequestBody LoginDto loginDto) {
        log.info("🔵 [POST] /authenticate 요청 받음: username={}, password={}", loginDto.getEmail(), loginDto.getPassword());

        ApiResult apiResult = new ApiResult();

        TokenDto tokenDto = processAuth.authorization(loginDto);

        apiResult.setData(tokenDto);

        return ResponseEntity.ok().body(apiResult);
    }
}
