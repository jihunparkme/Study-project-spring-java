package com.example.productorderservice.product;

import com.example.productorderservice.product.adapter.ProductRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ProductApiTest extends ApiTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void 상품등록() {
        final var request = ProductSteps.getAddProductRequest();

        final var response = ProductSteps.requestAddProduct(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 상품조회() {
        ProductSteps.requestAddProduct(ProductSteps.getAddProductRequest());
        final long productId = 1L;

        final var response = ProductSteps.requestInquiryProduct(productId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("id")).isEqualTo("1");
        assertThat(response.jsonPath().getString("name")).isEqualTo("상품명");
        assertThat(response.jsonPath().getString("price")).isEqualTo("1000");
    }

    @Test
    void 상품수정() {
        ProductSteps.requestAddProduct(ProductSteps.getAddProductRequest());
        final long productId = 1L;

        final ExtractableResponse<Response> response = ProductSteps.requestUpdateProduct(productId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertEquals("상품 수정", productRepository.findById(1L).get().getName());
    }
}
