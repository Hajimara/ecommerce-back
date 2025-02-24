package com.shop.service.common;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * 인증(Authentication) 관련 서비스를 제공하는 클래스
 * - 사용자 인증을 처리하고, SecurityContextHolder에 인증 정보를 저장함
 */
@Slf4j // 로깅(Log) 기능 추가
@Service // Spring의 서비스 계층 빈으로 등록
@Transactional // 메서드 실행 중 트랜잭션을 유지하도록 설정
@RequiredArgsConstructor // final 필드에 대한 생성자를 Lombok이 자동 생성
public class ServiceAuth {

    private final AuthenticationManagerBuilder authenticationManagerBuilder; // Spring Security의 인증 관리자 빌더

    /**
     * 사용자의 ID와 비밀번호를 기반으로 인증(Authentication) 객체를 생성하는 메서드
     * - ID와 비밀번호를 사용하여 UsernamePasswordAuthenticationToken을 생성
     * - AuthenticationManager를 통해 실제 인증을 수행
     * - 인증이 성공하면 SecurityContextHolder에 저장하여 이후 요청에서도 인증된 사용자로 인식됨
     *
     * @param email       사용자 ID (또는 email)
     * @param password 사용자 비밀번호
     * @return 인증된 Authentication 객체 (성공 시)
     */
    public Authentication getAuthentication(
            String email,
            String password
    ) {
        // ✅ 사용자의 ID와 비밀번호를 기반으로 UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        // ✅ AuthenticationManager를 통해 실제 인증 수행
        Authentication authentication = authenticationManagerBuilder
                .getObject() // AuthenticationManager 객체 가져오기
                .authenticate(usernamePasswordAuthenticationToken); // 사용자 인증 실행

        // ✅ 인증된 사용자 정보를 SecurityContextHolder에 저장하여 이후 요청에서도 인증 유지
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ✅ 인증된 Authentication 객체 반환 (사용자의 인증 정보 포함)
        return authentication;
    }
}
