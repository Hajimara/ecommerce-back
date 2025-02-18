package com.shop.dto.customer.response;


import com.shop.constant.enums.MembershipLevel;
import com.shop.dto.address.response.AddressDto;
import com.shop.dto.shop_order.response.ShopOrderDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerDto {
    private Long customerId;
    private String email;
    private String name;
    private String phoneNumber;
    private String birthDate;
    private MembershipLevel membershipLevel;
    private String regDate;
    private List<ShopOrderDto> orderList;
    private List<AddressDto> addressList;
}