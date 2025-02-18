package com.shop.dto.address.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyAddressDto {
    @NotBlank
    private String zipCode;

    @NotBlank
    private String basicAddress;

    @NotBlank
    private String detailAddress;
}
