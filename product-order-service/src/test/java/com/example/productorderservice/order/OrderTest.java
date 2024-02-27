package com.example.productorderservice.order;

import com.example.productorderservice.product.DiscountPolicy;
import com.example.productorderservice.product.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void non_discounted_product_total_price() {
        final Order order = new Order(new Product("상품명", 1000, DiscountPolicy.NONE), 2);

        final int totalPrice = order.getTotalPrice();

        assertThat(totalPrice).isEqualTo(2000);
    }

    @Test
    void fix_1000_amount_discounted_product_total_price() {
        final Order order = new Order(new Product("상품명", 2000, DiscountPolicy.FIX_1000_AMOUNT), 2);

        final int totalPrice = order.getTotalPrice();

        assertThat(totalPrice).isEqualTo(2000);
    }
}