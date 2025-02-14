package com.shop.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "product")
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Nationalized
    private String productName;

    private Long price;

    private Long stockQuantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShopOrderItem> shopOrderItemList = new ArrayList<>();

    @Builder
    public Product(Long productId, String productName, Long price, Long stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.shopOrderItemList = new ArrayList<>();
    }
}
