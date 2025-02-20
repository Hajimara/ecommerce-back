package com.shop.dto.customer.response;

import com.shop.constant.annotation.OnlyNumber;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerIdDto {
    @OnlyNumber
    private Long CustomerId;
}
