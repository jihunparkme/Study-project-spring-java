package com.example.productorderservice.payment;

import org.springframework.util.Assert;

record PaymentRequest(long orderId, String cardNumber) {
    PaymentRequest {
        Assert.notNull(orderId, "주문 ID는 필수입니다.");
        Assert.hasText(cardNumber, "카드 번호는 필수입니다.");
    }
}
