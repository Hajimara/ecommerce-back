package com.shop.dto.customer.request;

import com.shop.constant.annotation.Password;
import com.shop.constant.enums.MembershipLevel;
import com.shop.dto.address.response.AddressDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RegisterCustomerDto {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    private String phoneNumber;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Password
    private String password;

    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    private LocalDate birthDate;

    private MembershipLevel membershipLevel;

    private List<AddressDto> addressList;
}
