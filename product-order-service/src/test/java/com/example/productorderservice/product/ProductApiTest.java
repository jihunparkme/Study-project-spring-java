package com.example.productorderservice.product;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ProductApiTest extends ApiTest {

    @Test
    void 상품등록() {
        final var request = ProductSteps.getAddProductRequest();

        final var response = ProductSteps.requestAddProduct(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}
