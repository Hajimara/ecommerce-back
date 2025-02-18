package com.shop.dto.address.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    @NotBlank
    private String zipCode;

    @NotBlank
    private String basicAddress;

    @NotBlank
    private String detailAddress;
}
