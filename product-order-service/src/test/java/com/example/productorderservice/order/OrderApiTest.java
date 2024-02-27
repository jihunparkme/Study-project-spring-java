package com.example.productorderservice.order;

import com.example.productorderservice.product.ApiTest;
import com.example.productorderservice.product.ProductSteps;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class OrderApiTest extends ApiTest {

    @Test
    void 상품주문() {
        ProductSteps.requestAddProduct(ProductSteps.getAddProductRequest());
        final var request = OrderSteps.getCreateOrderRequest();

        final var response = OrderSteps.requestCreateOrder(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }



}
