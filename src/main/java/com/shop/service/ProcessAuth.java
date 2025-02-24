package com.shop.service;

import com.shop.ExceptionNotFound;
import com.shop.config.jwt.JwtUser;
import com.shop.constant.enums.ErrorCode;
import com.shop.dto.auth.request.LoginDto;
import com.shop.dto.auth.request.ModifyAccessTokenDto;
import com.shop.dto.auth.response.AccessTokenDto;
import com.shop.dto.auth.response.TokenDto;
import com.shop.entity.Customer;
import com.shop.entity.Token;
import com.shop.exception.ExceptionUnAuthorized;
import com.shop.service.common.ServiceAuth;
import com.shop.service.common.ServiceJwt;
import com.shop.service.entity.ServiceCustomer;
import com.shop.service.entity.ServiceToken;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProcessAuth {

    private final PasswordEncoder passwordEncoder;

    private final ServiceJwt serviceJwt;
    private final ServiceAuth serviceAuth;
    private final ServiceToken serviceToken;
    private final ServiceCustomer serviceCustomer;

    public TokenDto authorization(LoginDto loginDto) {
        TokenDto tokenDto = new TokenDto();

        Customer customer = serviceCustomer.getByEmailElseThrow(loginDto.getEmail());

        if (!passwordEncoder.matches(loginDto.getPassword(), customer.getPassword())) {
            throw new ExceptionNotFound(ErrorCode.MEMBER_NOT_FOUND);
        }

//        MemberStatusDto memberStatusDto = serviceMember.toMemberStatusDto(member);
//
//        if (memberStatusDto.getStatus() == MemberStatus.PENDING) {
//            throw new ExceptionStatus(ErrorCode.MEMBER_STATUS_PENDING);
//        } else if (memberStatusDto.getStatus() == MemberStatus.WITHDRAWAL) {
//            throw new ExceptionStatus(ErrorCode.MEMBER_STATUS_WITHDRAWAL);
//        }

        Authentication authentication = serviceAuth.getAuthentication(loginDto.getEmail(), loginDto.getPassword());

        String accessToken = serviceJwt.generateAccessToken(authentication);
        String refreshToken = serviceJwt.generateRefreshToken(authentication);

        Token existToken = serviceToken.findByEmail(loginDto.getEmail());
        Token.TokenBuilder tokenBuilder = Token
                .builder()
                .email(loginDto.getEmail())
                .accessToken(existToken == null ? null : accessToken)
                .refreshToken(existToken == null ? null : refreshToken);
        Token token = tokenBuilder.build();

        serviceToken.create(token);

        tokenDto.setAccessToken(accessToken);
        tokenDto.setRefreshToken(refreshToken);

        return tokenDto;
    }

    public AccessTokenDto modifyAccessToken(ModifyAccessTokenDto modifyAccessTokenDto) {
        // 새로운 액세스 토큰을 담을 DTO 생성
        AccessTokenDto accessTokenDto = new AccessTokenDto();

        // 1️⃣ 현재 액세스 토큰이 유효한지 검증 (만료된 경우 예외 발생)
        if (!serviceJwt.isValidateToken(modifyAccessTokenDto.getAccessToken())) {
            throw new ExceptionUnAuthorized(); // ❌ 유효하지 않은 토큰 → 인증 실패 예외 발생
        }

        // 2️⃣ 리프레시 토큰을 이용해 해당 유저의 tokenId 추출
        String tokenId = serviceJwt.getIdByToken(modifyAccessTokenDto.getRefreshToken());

        // 3️⃣ tokenId를 기반으로 JwtUser 객체 조회 (사용자 정보 가져오기)
        JwtUser user = serviceJwt.getJwtUser(tokenId);

        // 4️⃣ 사용자 권한 정보 조회 (해당 사용자의 역할(Role) 정보 가져오기)
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // 5️⃣ 기존 JwtUser 정보를 기반으로 새로운 JwtUser 객체 생성
        JwtUser jwtUser = serviceJwt.generateJwtUser(user, authorities, tokenId);

        // 6️⃣ 새로운 액세스 토큰을 생성 (권한 정보 포함)
        String newAccessToken = serviceJwt.generateAccessToken(jwtUser, authorities);

        // 7️⃣ 기존 저장된 토큰 객체 조회 (현재 사용 중인 액세스 토큰 & 리프레시 토큰을 기준으로 조회)
        Token token = serviceToken.getTokenElseNull(
                modifyAccessTokenDto.getAccessToken(),
                modifyAccessTokenDto.getRefreshToken()
        );

        // 8️⃣ 기존 토큰 객체의 액세스 토큰을 새로 발급된 토큰으로 업데이트
        token.updateAccessToken(newAccessToken);

        // 9️⃣ 새롭게 발급된 액세스 토큰을 DTO에 설정
        accessTokenDto.setAccessToken(newAccessToken);

        //  🔟 갱신된 액세스 토큰 DTO 반환
        return accessTokenDto;
    }
}
