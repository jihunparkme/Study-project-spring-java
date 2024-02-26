package com.example.productorderservice.product;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

    @Test
    void 상품조회() {
        ProductSteps.requestAddProduct(ProductSteps.getAddProductRequest());
        Long productId = 1L;

        final ExtractableResponse<Response> response = RestAssured.given().log().all() // 요청 로그 남기기
                .when()
                .get("/products/{productId}", productId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("id")).isEqualTo("1");
        assertThat(response.jsonPath().getString("name")).isEqualTo("상품명");
        assertThat(response.jsonPath().getString("price")).isEqualTo("1000");
    }
}
