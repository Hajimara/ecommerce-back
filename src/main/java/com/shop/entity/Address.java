package com.shop.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

@Entity
@Getter
@Table(name = "address")
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "FK_address_customer"))
    private Customer customer;

    @Nationalized
    private String zipCode;

    @Nationalized
    private String basicAddress;

    @Nationalized
    private String detailAddress;

    @Builder
    public Address(
            Long addressId,
            Customer customer,
            String zipCode,
            String basicAddress,
            String detailAddress
    ) {
        this.addressId = addressId;
        this.customer = customer;
        this.zipCode = zipCode;
        this.basicAddress = basicAddress;
        this.detailAddress = detailAddress;
    }
}
