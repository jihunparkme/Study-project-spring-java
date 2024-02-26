package com.example.productorderservice.product;

import org.springframework.util.Assert;

/**
 * 03. Request 는 record 로 사용 가능
 */
record AddProductRequest(String name, int price, DiscountPolicy discountPolicy) {
    AddProductRequest {
        // 03-1. Assert 를 활용해서 파라미터 체크
        Assert.hasText(name, "상품명은 필수입니다.");
        Assert.isTrue(price > 0, "상품 가격은 0보다 커야 합니다.");
        Assert.notNull(discountPolicy, "할인 정책은 필수입니다.");
    }
}
