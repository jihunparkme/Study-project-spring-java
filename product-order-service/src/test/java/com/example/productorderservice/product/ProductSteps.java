package com.example.productorderservice.product;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class ProductSteps {
    public static ExtractableResponse<Response> requestAddProduct(final AddProductRequest request) {
        return RestAssured.given().log().all() // 요청 로그 남기기
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/products")
                .then()
                .log().all().extract();
    }

    public static AddProductRequest getAddProductRequest() {
        final String name = "상품명";
        final int price = 1000;
        final DiscountPolicy discountPolicy = DiscountPolicy.NONE;
        return new AddProductRequest(name, price, discountPolicy); // 02. Request 클래스 생성
    }

    public static ExtractableResponse<Response> requestInquiryProduct(final Long productId) {
        return RestAssured.given().log().all() // 요청 로그 남기기
                .when()
                .get("/products/{productId}", productId)
                .then().log().all()
                .extract();
    }
}