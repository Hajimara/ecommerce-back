package com.shop.constant.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDER_REQUEST("ORDER_REQUEST", "주문요청"),
    DELIVERY_IN_PROGRESS("DELIVERY_IN_PROGRESS", "배송중"),
    DELIVERY_COMPLETE("DELIVERY_COMPLETE", "배송완료"),
    CANCEL_REQUEST("CANCEL_REQUEST", "취소요청"),
    CANCEL_COMPLETE("CANCEL_COMPLETE", "취소완료"),
    RETURN_REQUEST("RETURN_REQUEST", "반품요청"),
    RETURN_IN_PROGRESS("RETURN_IN_PROGRESS", "반품중"),
    RETURN_INBOUND_COMPLETE("RETURN_INBOUND_COMPLETE", "입고완료"),
    RETURN_DENIED("RETURN_DENIED", "반품거절"),
    RETURN_COMPLETE("RETURN_COMPLETE", "반품완료");

    // 상태값이 변하지 않도록 보장하기 위해 final
    private final String code;
    private final String codeName;

    OrderStatus(
            String code,
            String codeName
    ) {
        this.code = code;
        this.codeName = codeName;
    }
}