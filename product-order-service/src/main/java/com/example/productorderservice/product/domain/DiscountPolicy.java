package com.example.productorderservice.product.domain;

public enum DiscountPolicy {
    NONE {
        @Override
        public int applyDiscount(final int price) {
            return price;
        }
    },
    RATE {
        @Override
        public int applyDiscount(final int price) {
            return (int) (price * 0.9);
        }
    },
    FIX_1000_AMOUNT {
        @Override
        public int applyDiscount(final int price) {
            return Math.max(price - 1000, 0);
        }
    };

    public abstract int applyDiscount(final int price);
}
