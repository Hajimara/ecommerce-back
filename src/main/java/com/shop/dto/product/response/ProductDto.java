package com.shop.dto.product.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Long productId;
    private String productName;
    private Long price;
    private Long stockQuantity;
}
