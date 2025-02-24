package com.shop.config.jwt;

import com.shop.constant.ConstantString;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * 1. 클라이언트가 JWT를 포함하여 API 요청을 보냄.
 * 2. JwtFilter가 요청을 가로채서 Authorization 헤더에서 JWT를 추출.
 * 3. jwtTokenProvider.validateToken(jwt)로 JWT 유효성 검사를 수행.
 * 4. 인증이 유효하면 SecurityContextHolder에 인증 정보를 저장.
 * 5. 이후 컨트롤러에서 SecurityContextHolder.getContext().getAuthentication()을 통해 인증된 사용자 정보를 사용할 수 있음.
 * 6. 필터 체인(filterChain.doFilter)을 통해 다음 필터나 요청 처리 로직으로 진행.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 필터링 로직을 수행하는 메서드
     * @param servletRequest HTTP 요청 객체
     * @param servletResponse HTTP 응답 객체
     * @param filterChain 다음 필터로 요청을 전달하는 체인
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest; // HTTP 요청 객체로 캐스팅
        String jwt = extractToken(httpServletRequest); // 요청에서 JWT 토큰을 추출
        String requestURI = httpServletRequest.getRequestURI(); // 현재 요청의 URI

        // (1) JWT가 존재하고 유효한 경우 인증을 설정함
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt); // JWT를 통해 Authentication 객체 생성

            if (authentication != null) {
                // Spring Security의 SecurityContext에 현재 인증된 사용자 정보 저장
                // SecurityContextHolder는 스레드 로컬(ThreadLocal)을 사용하여 현재 요청에 대한 인증 정보를 유지함
                // 이유: Spring Security는 SecurityContextHolder를 통해 인증 정보를 관리하며,
                //      이후 컨트롤러나 서비스 계층에서 SecurityContextHolder.getContext().getAuthentication()을 통해 인증된 사용자 정보를 가져올 수 있음.
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 인증된 사용자의 이름과 요청 URI를 로그로 남김
                log.debug("Save authentication : '{}', uri: {}", authentication.getName(), requestURI);
            } else {
                // 인증 객체를 생성할 수 없는 경우 로그를 남김
                log.debug("No authentication, uri: {}", requestURI);
            }
        } else {
            // JWT가 없거나 유효하지 않은 경우 로그를 남김
            log.debug("No JWT token, uri: {}", requestURI);
        }

        // (2) 다음 필터로 요청 전달
        // Spring Security의 필터 체인은 여러 개의 보안 필터들로 구성되어 있음.
        // - 이 필터(JwtFilter)는 요청에서 JWT를 확인하고 유효하면 인증 정보를 설정함.
        // - 인증이 설정되었든 안 되었든 다음 필터로 요청을 넘겨야 함. (ex. UsernamePasswordAuthenticationFilter 등)
        // - 만약 여기서 요청을 중단하면, 이후의 인증 및 권한 필터가 실행되지 않아 요청이 정상적으로 처리되지 않음.
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * HTTP 요청에서 JWT 토큰을 추출하는 메서드
     * @param request HTTP 요청 객체
     * @return JWT 토큰 (Bearer 제거 후 반환), 없으면 null 반환
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(ConstantString.AUTHORIZATION); // Authorization 헤더에서 토큰 추출

        // 토큰이 존재하고 "Bearer "로 시작하는 경우 "Bearer " 부분 제거 후 반환
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null; // 유효한 JWT 토큰이 없는 경우 null 반환
    }
}
