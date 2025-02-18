package com.shop.dto.product.response;

import com.shop.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Long productId;
    private String productName;
    private Long price;
    private Long stockQuantity;

    public ProductDto(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
    }
}
