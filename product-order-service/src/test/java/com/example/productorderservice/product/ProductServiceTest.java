package com.example.productorderservice.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void updateProduct() {
        productService.addProduct(ProductSteps.getAddProductRequest());
        final Long productId = 1L;
        final UpdateProductRequest request = ProductSteps.getUpdateProductRequest();

        productService.updateProduct(productId, request);

        final ResponseEntity<GetProductResponse> response = productService.getProduct(productId);
        final GetProductResponse productResponse = response.getBody();

        Assertions.assertEquals("상품 수정", productResponse.name());
        Assertions.assertEquals(2000, productResponse.price());
    }
}
