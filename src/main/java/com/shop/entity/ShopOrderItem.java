package com.shop.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shop_order_item")
@NoArgsConstructor
@Getter
public class ShopOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopOrderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_order_id", nullable = false)
    private ShopOrder shopOrder; // 주문

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // 상품

    private Long quantity; // 주문한 상품 개수

    private Long totalPrice; // 해당 상품 * 갯수 = 총 가격

    @Builder
    public ShopOrderItem(
            ShopOrder shopOrder,
            Product product,
            Long quantity,
            Long totalPrice) {
        this.shopOrder = shopOrder;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}