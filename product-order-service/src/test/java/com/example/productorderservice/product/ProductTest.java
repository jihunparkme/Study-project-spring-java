package com.example.productorderservice.product;

import com.example.productorderservice.product.domain.DiscountPolicy;
import com.example.productorderservice.product.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void update() {
        final Product product = new Product("상품명", 1000, DiscountPolicy.NONE);

        product.update("상품 수정", 2000, DiscountPolicy.NONE);

        Assertions.assertEquals("상품 수정", product.getName());
        Assertions.assertEquals(2000, product.getPrice());
    }

    @Test
    void non_discounted_product() {
        final Product product = new Product("상품명", 1000, DiscountPolicy.NONE);

        final int discountedPrice = product.getDiscountedPrice();

        Assertions.assertEquals(1000, discountedPrice);
    }

    @Test
    void fix_1000_discounted_product() {
        final Product product = new Product("상품명", 1000, DiscountPolicy.FIX_1000_AMOUNT);

        final int discountedPrice = product.getDiscountedPrice();

        Assertions.assertEquals(0, discountedPrice);
    }
}