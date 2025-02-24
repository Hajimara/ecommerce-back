package com.shop.repository;

import com.shop.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryToken extends JpaRepository<Token, String> {

    Optional<Token> findByAccessTokenAndRefreshToken(String accessToken, String refreshToken);

    Optional<Token> findByEmail(String email);
}
