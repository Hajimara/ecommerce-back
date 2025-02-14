package com.shop.entity;

import com.shop.constant.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shop_order")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class ShopOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopOrderId;

    @ManyToOne(fetch = FetchType.LAZY) // 엔티티 조회 시 필요할때만 데이터를 가져옴
    @JoinColumn(
            name = "customer_id",
            foreignKey = @ForeignKey(
                    name = "FK_order_user"
            )
    )
    private Customer customer;

    @OneToMany(mappedBy = "shopOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShopOrderItem> shopOrderItems = new ArrayList<>();

    @Nationalized
    private String paymentMethod;

    private Long totalPrice;

    private LocalDateTime orderDate;

    // @JoinColumn은 외래 키(Foreign Key) 컬럼명을 정의하는데 사용됨.
    // shipping_address_id는 shop_order 테이블에서 address 테이블을 참조하는 외래 키(FK) 컬럼명임.
    // 데이터베이스에서 생성될 외래 키(Foreign Key) 제약 조건의 이름을 정하는 것
    @ManyToOne(fetch = FetchType.LAZY) // 배송 주소
    @JoinColumn(name = "shipping_address_id", nullable = false, foreignKey = @ForeignKey(name = "FK_order_address"))
    private Address shippingAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Builder
    public ShopOrder(Customer customer, List<ShopOrderItem> shopOrderItems, LocalDateTime orderDate, OrderStatus orderStatus, String paymentMethod, Long totalPrice, Address shippingAddress) {
        this.customer = customer;
        this.shopOrderItems = shopOrderItems;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.shippingAddress = shippingAddress;
    }
}
