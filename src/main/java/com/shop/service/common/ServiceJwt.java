package com.shop.service.common;

import com.shop.config.jwt.JwtTokenProvider;
import com.shop.config.jwt.JwtUser;
import com.shop.config.jwt.JwtUserDetailsService;
import com.shop.dto.common.TokenData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * JWT 관련 로직을 처리하는 서비스 클래스
 * - JWT 토큰 생성, 검증 및 사용자 정보 추출 기능을 수행함
 */
@Slf4j // 로깅을 위한 Lombok 어노테이션
@Service // Spring의 서비스 계층 빈으로 등록
@Transactional // 트랜잭션 관리를 위한 설정 (DB 작업이 있는 경우 사용됨)
@RequiredArgsConstructor // final 필드에 대한 생성자를 Lombok이 자동 생성
public class ServiceJwt {

    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰을 생성 및 검증하는 Provider
    private final JwtUserDetailsService jwtUserDetailsService; // 사용자 정보를 로드하는 서비스

    /**
     * 주어진 JWT 토큰에서 사용자 ID를 추출하는 메서드
     *
     * @param token JWT 토큰 문자열
     * @return 토큰에서 추출한 사용자 ID
     */
    public String getIdByToken(String token) {
        return jwtTokenProvider.getIdByToken(token); // JWT 토큰에서 ID(subject) 값 추출
    }

    /**
     * 주어진 사용자 ID를 기반으로 JwtUser 객체를 생성하는 메서드
     *
     * @param tokenId 사용자 ID (JWT의 subject 값)
     * @return JwtUser 객체
     */
    public JwtUser getJwtUser(String tokenId) {
        return (JwtUser) jwtUserDetailsService.loadUserByUsername(tokenId); // ID를 기반으로 사용자 정보 조회
    }

    /**
     * HTTP 요청에서 JWT 토큰을 추출하고, 토큰 데이터를 반환하는 메서드
     *
     * @param httpServletRequest HTTP 요청 객체
     * @return TokenData 객체 (사용자 정보 포함)
     */
    public TokenData getTokenData(HttpServletRequest httpServletRequest) {
        return jwtTokenProvider.getTokenData(httpServletRequest); // 요청 헤더에서 JWT 토큰을 추출하고 데이터 반환
    }

    /**
     * 주어진 Authentication 객체를 기반으로 JWT 액세스 토큰을 생성하는 메서드
     *
     * @param authentication 인증 객체 (Spring Security Authentication)
     * @return 생성된 JWT 액세스 토큰
     */
    public String generateAccessToken(Authentication authentication) {
        return jwtTokenProvider.generateAccessToken(authentication); // 인증 객체를 기반으로 JWT 생성
    }

    /**
     * 사용자 정보와 권한 정보를 기반으로 JWT 액세스 토큰을 생성하는 메서드
     *
     * @param jwtUser    사용자 정보 객체 (JwtUser)
     * @param authorities 사용자 권한 정보 (GrantedAuthority 리스트)
     * @return 생성된 JWT 액세스 토큰
     */
    public String generateAccessToken(
            JwtUser jwtUser,
            Collection<? extends GrantedAuthority> authorities
    ) {
        // 인증 객체를 생성한 후 JWT 액세스 토큰을 생성
        return jwtTokenProvider.generateAccessToken(new UsernamePasswordAuthenticationToken(jwtUser, null, authorities));
    }

    /**
     * 주어진 Authentication 객체를 기반으로 JWT 리프레시 토큰을 생성하는 메서드
     *
     * @param authentication 인증 객체 (Spring Security Authentication)
     * @return 생성된 JWT 리프레시 토큰
     */
    public String generateRefreshToken(Authentication authentication) {
        return jwtTokenProvider.generateRefreshToken(authentication); // 인증 객체를 기반으로 리프레시 토큰 생성
    }

    /**
     * 새로운 JwtUser 객체를 생성하는 메서드
     *
     * @param jwtUser    기존 JwtUser 객체
     * @param authorities 사용자 권한 정보 (GrantedAuthority 리스트)
     * @param tokenId    새로운 사용자 ID (토큰 ID)
     * @return 새로운 JwtUser 객체
     */
    public JwtUser generateJwtUser(
            JwtUser jwtUser,
            Collection<? extends GrantedAuthority> authorities,
            String tokenId
    ) {
        return new JwtUser(tokenId, "", authorities, jwtUser.getCustomerId()); // 새로운 JwtUser 객체 생성 및 반환
    }

    /**
     * 주어진 JWT 토큰의 유효성을 검증하는 메서드
     *
     * @param token 검증할 JWT 토큰 문자열
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean isValidateToken(String token) {
        return jwtTokenProvider.validateToken(token); // JWT의 서명 및 만료 여부 검증
    }
}
