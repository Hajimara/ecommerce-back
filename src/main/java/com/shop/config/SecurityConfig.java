package com.shop.config;

import com.shop.config.jwt.JwtAuthenticationEntryPoint;
import com.shop.config.jwt.JwtFilter;
import com.shop.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${env.security.allowed_origin}") // 환경 변수에서 CORS 허용 도메인 가져오기
    private String allowedOrigins;

    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰을 생성 및 검증하는 Provider
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 인증 실패 처리 (401 Unauthorized)
//    private final JwtAccessDeniedHandler jwtAccessDeniedHandler; // 접근 거부 처리 (403 Forbidden)

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 해싱에 BCrypt 사용
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
        .csrf(AbstractHttpConfigurer::disable) // JWT 사용 시 CSRF 필요 없음 → 비활성화

        .cors(cors -> {
            CorsConfigurationSource source = request -> { // 요청이 들어오면 CORS 설정 적용
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowCredentials(true); // 쿠키 및 인증 정보 포함 허용
                config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용 (GET, POST, PUT, DELETE 등)
                config.addAllowedOrigin("*"); // 모든 도메인에서 요청 허용 (보안적으로 위험)
                config.setExposedHeaders(Arrays.asList("content-disposition")); // 파일 다운로드 관련 헤더 허용

                // allowedOrigins 값을 환경 변수에서 가져와서 허용할 도메인 추가
                for (String val : allowedOrigins.split(",")) {
                    config.addAllowedOrigin(val);
                }

                return config;
            };
            cors.configurationSource(source); // CORS 설정을 Security에 적용
        })

        .exceptionHandling(exceptionConfig ->
            exceptionConfig.authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 실패 시 (401)
//                .accessDeniedHandler(jwtAccessDeniedHandler) // 접근 거부 시 (403)
        )

        .headers(headerConfig ->
            headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // 같은 출처의 iframe 허용

        .sessionManagement(sessionConfig ->
            sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 기반 인증을 사용하므로 세션을 사용하지 않음
                
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers(HttpMethod.POST, "/customer").permitAll() // 회원가입 (POST /customer) 요청은 인증 없이 허용
            .requestMatchers(HttpMethod.POST, "/authenticate").permitAll() // 인증 (POST /authenticate) 요청은 인증 없이 허용
            .requestMatchers(HttpMethod.POST, "/refresh").permitAll() // 인증 갱신 (POST /refresh) 요청은 인증 없이 허용
            .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
        )

        // UsernamePasswordAuthenticationFilter는 Spring Security에서 기본적으로 제공하는 로그인(인증) 필터
        // 사용자가 **ID(Username) + 비밀번호(Password)**를 입력하면, 이를 처리하는 기본 인증 필터로 작동
        // JwtFilter를 UsernamePasswordAuthenticationFilter 앞에 배치하여, 모든 요청이 Security 필터를 통과하기 전에 JWT 검증을 수행하도록 설정
        .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build(); // 설정을 적용하여 SecurityFilterChain 객체 반환
    }
}
