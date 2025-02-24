package com.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "token")
@NoArgsConstructor
@Getter
public class Token {
    @Id
    @Nationalized
    private String email;

    @Column(length = 1000)
    @Nationalized
    private String accessToken;

    @Column(length = 1000)
    @Nationalized
    private String refreshToken;

    public void updateAccessToken(String newAccessToken) { this.accessToken = newAccessToken; }
    public void updateRefreshToken(String newRefreshToken) { this.refreshToken = newRefreshToken; }

    @Builder
    public Token(String email, String accessToken, String refreshToken) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
