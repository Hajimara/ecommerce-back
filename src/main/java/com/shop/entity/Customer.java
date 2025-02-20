package com.shop.entity;

import com.shop.constant.enums.MembershipLevel;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class Customer {
    @Id // ID값 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //increment
    private Long customerId;

    @Column(unique = true)
    @Nationalized
    private String email;

    @Nationalized // NVARCHAR 적용
    private String name;

    @Nationalized
    private String phoneNumber;

    @Nationalized
    private String password;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Nationalized
    private MembershipLevel membershipLevel = MembershipLevel.BASIC;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate;

    // ShopOrder 엔티티의 customer 필드와 매핑됨
    // cascade = CascadeType.ALL → Customer가 삭제되면 해당 ShopOrder들도 함께 삭제됨
    // orphanRemoval = true → Customer가 주문을 제거하면, ShopOrder도 자동으로 DB에서 삭제됨
    // 만약 orphanRemoval >> customer.getOrders().remove(order);를 실행하면? ✅ 해당 주문(ShopOrder)이 DB에서도 자동으로 삭제됨
    // SQL 실행유무
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShopOrder> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addressList = new ArrayList<>();

    public void updateAddress(List<Address> addressList) {
        this.addressList.addAll(addressList);
    }

    @Builder
    public Customer(
            Long customerId,
            String name,
            String phoneNumber,
            String password,
            String email,
            LocalDate birthDate,
            MembershipLevel membershipLevel
    ) {
        this.customerId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.membershipLevel = membershipLevel != null ? membershipLevel : MembershipLevel.BASIC;
        this.orderList = new ArrayList<>();
        this.addressList = new ArrayList<>();
    }
}
