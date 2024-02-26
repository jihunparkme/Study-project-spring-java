package com.example.productorderservice.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;


    @Test
    void 상품등록() {
        final AddProductRequest request = getAddProductRequest();

        productService.addProduct(request); // 01. 상품 등록 서비스 호출
    }

    private AddProductRequest getAddProductRequest() {
        final String name = "상품명";
        final int price = 1000;
        final DiscountPolicy discountPolicy = DiscountPolicy.NONE;
        return new AddProductRequest(name, price, discountPolicy); // 02. Request 클래스 생성
    }

}
