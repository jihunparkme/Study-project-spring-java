package com.example.productorderservice.product;

import com.example.productorderservice.product.application.service.AddProductRequest;
import com.example.productorderservice.product.application.service.UpdateProductRequest;
import com.example.productorderservice.product.domain.DiscountPolicy;
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

    public static UpdateProductRequest getUpdateProductRequest() {
        return new UpdateProductRequest("상품 수정", 2000, DiscountPolicy.NONE);
    }

    public static ExtractableResponse<Response> requestUpdateProduct(final long productId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(ProductSteps.getUpdateProductRequest())
                .when()
                .patch("/products/{productId}", productId)
                .then().log().all()
                .extract();
    }
}