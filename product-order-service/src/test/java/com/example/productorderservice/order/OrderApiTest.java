package com.example.productorderservice.order;

import com.example.productorderservice.product.ApiTest;
import com.example.productorderservice.product.ProductSteps;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

class OrderApiTest extends ApiTest {

    @Test
    void 상품주문() {
        ProductSteps.requestAddProduct(ProductSteps.getAddProductRequest());
        final CreateOrderRequest request = getCreateOrderRequest();

        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/orders")
                .then()
                .log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static CreateOrderRequest getCreateOrderRequest() {
        final long productId = 1L;
        final int quantity = 2;
        return new CreateOrderRequest(productId, quantity);
    }

}
