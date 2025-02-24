package com.shop.service.entity;

import com.shop.entity.Token;
import com.shop.repository.RepositoryToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ServiceToken {

    private final RepositoryToken repositoryToken;

    public void create(Token token) {
        repositoryToken.save(token);
    }

    public Token findByEmail(String email) {
        return repositoryToken.findByEmail(email).orElse(null);
    }

    public Token getTokenElseNull(String accessToken,String refreshToken) {
        return repositoryToken
            .findByAccessTokenAndRefreshToken(accessToken, refreshToken)
            .orElse(null);
    }
}
