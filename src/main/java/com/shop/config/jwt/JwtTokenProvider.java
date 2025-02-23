package com.shop.config.jwt;

import com.shop.constant.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;

/**
 * InitializingBean은 일반적으로 JWT의 시크릿 키(SECRET_KEY)를 초기화하기 위해 사용함.
 */
@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {
    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    private Key key;

    private static final String AUTHORITIES_KEY = "auth";

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds
    ) {
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
    }

    /**
     *  🔹 Spring이 Bean을 초기화한 후 실행됨
     *  1️⃣ 시크릿 키는 안전하게 관리되어야 함
     *  JWT의 서명을 검증하려면 **비밀 키(Secret Key)**가 필요함.
     *  보통 application.yml이나 **환경 변수(.env)**에 secret을 Base64 인코딩해서 저장해둠.
     *  JWT에서 사용하려면 이 값을 디코딩(Base64 → 바이트 배열) 후, SecretKey 객체로 변환해야 함.
     *  2️⃣ 시크릿 키는 Key 타입으로 변환해야 함
     *  Keys.hmacShaKeyFor(byte[])는 HMAC-SHA 알고리즘에서 사용할 SecretKey를 생성함.
     *  이렇게 변환하면 JWT 서명(Signature)과 검증(Validation)에 사용할 수 있음.
     */
    @Override
    public void afterPropertiesSet() {
        // 환경 변수에서 가져온 secret 문자열(Base64 인코딩된 값)을 디코딩
        byte[] keyBytes = Decoders.BASE64.decode(secret);

        // keyBytes를 사용하여 HMAC-SHA 알고리즘을 위한 키 생성
        // JWT를 생성할 때 이 key를 사용하여 서명(Signature) 처리함.
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 토큰을 기반으로 사용자의 인증 정보를 생성하는 메서드
     * @param token 클라이언트가 전송한 JWT 토큰
     * @return UsernamePasswordAuthenticationToken (Spring Security에서 사용하는 인증 객체)
     */
    public Authentication getAuthentication(String token) {
        // (1) JWT 토큰을 파싱하여 Claims(토큰에 저장된 데이터) 객체를 가져옴
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // 서명 키를 설정하여 토큰을 검증
                .build()
                .parseClaimsJws(token) // 토큰을 파싱하고 검증함 (유효하지 않으면 예외 발생)
                .getBody(); // JWT 본문(Claims) 추출

        // (2) 토큰에 필수 데이터가 포함되어 있는지 검증 (누락된 경우 인증 실패)
//        if (claims.get(AUTHORITIES_KEY) == null ||
//                claims.get(Constants.MEMBER_ID) == null ||
//                claims.get(Constants.COMPANY_ID) == null ||
//                claims.get(Constants.MAX_JOB_LEVEL) == null
//        ) {
//            return null; // 필수 정보가 없으면 인증 실패 (null 반환)
//        }

        // (3) 사용자의 권한(authorities) 정보를 추출하여 GrantedAuthority 리스트로 변환
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")) // 권한 문자열을 ',' 기준으로 분리
                .map(SimpleGrantedAuthority::new) // 권한 문자열을 SimpleGrantedAuthority 객체로 변환
                .toList(); // 리스트로 변환

        // (4) 토큰에서 사용자 관련 정보를 추출 (회원 ID, 회사 ID, 직급 레벨)
        Long customerId = Long.parseLong(claims.get(Constants.CUSTOMER_ID).toString());

        // (5) JwtUser 객체를 생성 (사용자 정보와 권한을 포함한 커스텀 유저 객체)
        JwtUser principal = new JwtUser(claims.getSubject(), "", authorities, customerId);

        // (6) Spring Security의 UsernamePasswordAuthenticationToken을 생성하여 반환
        // - 첫 번째 파라미터: principal (인증된 사용자 객체)
        // - 두 번째 파라미터: credentials (비밀번호, JWT 인증이므로 빈 문자열 사용)
        // - 세 번째 파라미터: authorities (사용자의 권한 리스트)
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * JWT 토큰의 유효성을 검증하는 메서드
     * @param token 클라이언트가 제공한 JWT 토큰
     * @return 유효한 토큰이면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            // (1) 토큰을 파싱하여 유효성을 검증 (유효하지 않으면 예외 발생)
            Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 키를 설정하여 서명 검증
                    .build()
                    .parseClaimsJws(token); // JWT 파싱 및 서명 검증

            return true; // 검증 성공 시 true 반환
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            // (2) JWT의 서명이 올바르지 않거나, 형식이 손상된 경우
            log.info("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            // (3) JWT의 유효기간이 만료된 경우
            log.debug("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            // (4) 지원되지 않는 형식의 JWT인 경우
            log.info("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            // (5) JWT가 null이거나 빈 문자열인 경우
            log.info("JWT token is invalid");
        }

        return false; // 검증 실패 시 false 반환
    }
}
