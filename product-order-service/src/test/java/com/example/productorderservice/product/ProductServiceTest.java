package com.example.productorderservice.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductServiceTest {

    private ProductService productService;
    private StubProductPort productPort = new StubProductPort();

    @BeforeEach
    void setUp() {
        productService = new ProductService(productPort);
    }

    @Test
    void updateProduct() {
        final Long productId = 1L;
        final String name = "상품 수정";
        int price = 2000;
        final DiscountPolicy discountPolicy = DiscountPolicy.NONE;
        final UpdateProductRequest request = new UpdateProductRequest(name, price, discountPolicy);
        final Product product = new Product("상품명", 1000, DiscountPolicy.NONE);
        productPort.getProduct_will_return = product;

        productService.updateProduct(productId, request);

        Assertions.assertEquals("상품 수정", product.getName());
        Assertions.assertEquals(2000, product.getPrice());
    }

    private class StubProductPort implements ProductPort {

        public Product getProduct_will_return;

        @Override
        public void save(final Product product) {
            throw new UnsupportedOperationException("StubProductPort#save not implemented yet !!");
        }

        @Override
        public Product getProduct(final long productId) {
            return getProduct_will_return;
        }
    }
}
