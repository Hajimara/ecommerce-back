package com.shop.service;

import com.shop.ExceptionNotFound;
import com.shop.constant.enums.ErrorCode;
import com.shop.dto.auth.request.LoginDto;
import com.shop.dto.auth.response.TokenDto;
import com.shop.entity.Customer;
import com.shop.entity.Token;
import com.shop.service.common.ServiceAuth;
import com.shop.service.common.ServiceJwt;
import com.shop.service.entity.ServiceCustomer;
import com.shop.service.entity.ServiceToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
