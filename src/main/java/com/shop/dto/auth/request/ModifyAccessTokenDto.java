package com.shop.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyAccessTokenDto {
    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;
}
