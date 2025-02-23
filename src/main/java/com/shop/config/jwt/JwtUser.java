package com.shop.config.jwt;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Objects;

/**
 * JWT 기반 인증을 위한 사용자 정보를 확장한 클래스.
 * Spring Security의 User 클래스를 상속받아 추가적인 사용자 정보를 포함함.
 */
@Getter
public class JwtUser extends User {

    private final Long customerId;

    /**
     * JwtUser 생성자
     * @param username  사용자 아이디 (Spring Security 기본 필드)
     * @param password  사용자 비밀번호 (Spring Security 기본 필드)
     * @param authorities 사용자 권한 목록 (Spring Security 기본 필드)
     * @param customerId  사용자 고유 ID
     */
    public JwtUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long customerId) {
        super(username, password, authorities);
        this.customerId = customerId;
    }

    /**
     * 동일한 객체인지 비교하는 equals 메서드 오버라이딩
     * - 같은 객체이면 true 반환
     * - 객체 타입이 다르면 false 반환
     * - 부모 클래스(User)의 equals 비교 후, companyId가 동일한지 비교
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JwtUser that)) return false;
        if (!super.equals(o)) return false;

        return Objects.equals(customerId, that.customerId);
    }

    /**
     * 객체의 해시코드를 생성하는 hashCode 메서드 오버라이딩
     * - 부모 클래스(User)의 해시코드와 companyId를 기반으로 생성
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customerId);
    }
}
