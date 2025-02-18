package com.shop.dto.shop_order.response;

import com.shop.dto.address.response.AddressDto;
import com.shop.dto.customer.response.CustomerDto;
import com.shop.constant.enums.OrderStatus;
import com.shop.dto.shop_order_item.response.ShopOrderItemDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ShopOrderDto {
    private Long shopOrderId;
    private String paymentMethod;
    private Long totalPrice;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private CustomerDto customer;
    private AddressDto shippingAddress;
    private List<ShopOrderItemDto> shopOrderItems;
}
