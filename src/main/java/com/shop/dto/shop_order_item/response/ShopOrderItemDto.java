package com.shop.dto.shop_order_item.response;

import com.shop.dto.product.response.ProductDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopOrderItemDto {
    private Long shopOrderItemId;
    private ProductDto product;
    private Long quantity;
    private Long totalPrice;
}
