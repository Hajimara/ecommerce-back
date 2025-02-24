package com.shop.config.jwt;

import com.shop.entity.Customer;
import com.shop.repository.RepositoryCustomer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security의 UserDetailsService 구현체
 * - 사용자의 로그인 요청이 들어오면 DB에서 사용자 정보를 조회하는 역할을 수행
 * - JWT 기반 인증 방식에서 사용됨
 */
@Component("userDetailsService") // Spring Security에서 사용할 UserDetailsService 빈으로 등록
@RequiredArgsConstructor // final 필드를 매개변수로 하는 생성자를 Lombok이 자동 생성
public class JwtUserDetailsService implements UserDetailsService {

    private final RepositoryCustomer repositoryCustomer;

    /**
     * 사용자 아이디(ID)로 사용자 정보를 조회하는 메서드
     * - Spring Security에서 로그인 시 자동으로 호출됨
     * - 사용자 정보가 존재하면 UserDetails 객체를 반환하고, 없으면 예외 발생
     *
     * @param email 사용자 ID (로그인 시 입력한 값)
     * @return UserDetails 객체 (Spring Security에서 사용하는 인증 정보)
     */
    @Override
    @Transactional // 트랜잭션을 보장하여 데이터 정합성 유지
    public UserDetails loadUserByUsername(final String email) {
        return repositoryCustomer.findByEmail(email)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException(email + " NOT FOUND"));
    }

    /**
     * Member 엔티티를 Spring Security의 User 객체로 변환하는 메서드
     *
     * @param customer DB에서 조회한 사용자 정보
     * @return JwtUser (Spring Security에서 사용하는 사용자 정보 객체)
     */
    private org.springframework.security.core.userdetails.User createUser(Customer customer) {
        // 사용자의 직무(job) 및 역할(roleList) 정보를 SimpleGrantedAuthority로 변환
//        Collection<? extends GrantedAuthority> authorities = Stream.concat(
//                Stream.of(new SimpleGrantedAuthority(member.getJob().getJobCode())), // 사용자의 메인 직무(Job)
//                member.getRoleList().stream().map(job -> new SimpleGrantedAuthority(job.getJobCode())) // 사용자의 추가 역할(Role)
//        ).toList();

//        // 사용자의 부가 직무(Role) 중 가장 높은 직급(jobLevel) 찾기
//        int subJobMaxLevel = member.getRoleList().stream()
//                .mapToInt(Job::getJobLevel)
//                .max() // 가장 높은 jobLevel 찾기
//                .orElse(0); // 추가 역할이 없으면 0 반환
//
//        // 사용자의 메인 직무와 부가 직무 중 가장 높은 직급(Level) 선택
//        int maxJobLevel = Math.max(member.getJob().getJobLevel(), subJobMaxLevel);

        // 가짜 데이터
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        // JwtUser 객체 생성하여 반환
        return new JwtUser(
                customer.getEmail(), // 사용자 ID
                customer.getPassword(), // 비밀번호 (해싱된 상태)
                authorities, // 권한 목록
                customer.getCustomerId() // 사용자 고유 ID (DB의 PK)
        );
    }
}
