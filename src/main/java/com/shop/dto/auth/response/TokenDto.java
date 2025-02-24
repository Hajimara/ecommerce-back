package com.shop.dto.auth.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}
